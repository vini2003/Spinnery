package spinnery.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.container.Container;

import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import spinnery.registry.NetworkRegistry;
import spinnery.util.MutablePair;
import spinnery.util.StackUtilities;
import spinnery.widget.WAbstractWidget;
import spinnery.widget.WInterface;
import spinnery.widget.WSlot;
import spinnery.widget.api.Action;
import spinnery.widget.api.WNetworked;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BaseContainer extends Container {
	public static final int PLAYER_INVENTORY = 0;
	protected final WInterface serverInterface;
	public Map<Integer, Inventory> linkedInventories = new HashMap<>();
	public Map<Integer, Map<Integer, ItemStack>> cachedInventories = new HashMap<>();
	protected Set<WSlot> splitSlots = new HashSet<>();
	protected Set<WSlot> singleSlots = new HashSet<>();
	protected Map<Integer, Map<Integer, ItemStack>> previewStacks = new HashMap<>();
	protected ItemStack previewCursorStack = ItemStack.EMPTY;
	protected World world;

	public BaseContainer(int synchronizationID, PlayerInventory linkedPlayerInventory) {
		super(null, synchronizationID);
		getInventories().put(PLAYER_INVENTORY, linkedPlayerInventory);
		setWorld(linkedPlayerInventory.player.world);
		serverInterface = new WInterface(this);
	}

	public Map<Integer, Inventory> getInventories() {
		return linkedInventories;
	}

	public <C extends BaseContainer> C setWorld(World world) {
		this.world = world;
		return (C) this;
	}

	@Environment(EnvType.CLIENT)
	public ItemStack getPreviewCursorStack() {
		return previewCursorStack;
	}

	@Environment(EnvType.CLIENT)
	public <C extends BaseContainer> C setPreviewCursorStack(ItemStack previewCursorStack) {
		this.previewCursorStack = previewCursorStack;
		return (C) this;
	}

	@Environment(EnvType.CLIENT)
	public void flush() {
		getInterface().getContainer().getDragSlots(GLFW.GLFW_MOUSE_BUTTON_1).clear();
		getInterface().getContainer().getDragSlots(GLFW.GLFW_MOUSE_BUTTON_2).clear();
		getInterface().getContainer().getPreviewStacks().clear();
		getInterface().getContainer().setPreviewCursorStack(ItemStack.EMPTY);
	}

	@Environment(EnvType.CLIENT)
	public Set<WSlot> getDragSlots(int mouseButton) {
		switch (mouseButton) {
			case 0:
				return splitSlots;
			case 1:
				return singleSlots;
			default:
				return null;
		}
	}

	public WInterface getInterface() {
		return serverInterface;
	}

	@Environment(EnvType.CLIENT)
	public Map<Integer, Map<Integer, ItemStack>> getPreviewStacks() {
		return previewStacks;
	}

	@Environment(EnvType.CLIENT)
	public boolean isDragging() {
		return getDragSlots(GLFW.GLFW_MOUSE_BUTTON_1).isEmpty() || getDragSlots(GLFW.GLFW_MOUSE_BUTTON_2).isEmpty();
	}

	public void onInterfaceEvent(int widgetSyncId, WNetworked.Event event, CompoundTag payload) {
		Set<WAbstractWidget> checkWidgets = serverInterface.getAllWidgets();
		for (WAbstractWidget widget : checkWidgets) {
			if (!(widget instanceof WNetworked)) continue;
			if (((WNetworked) widget).getSyncId() == widgetSyncId) {
				((WNetworked) widget).onInterfaceEvent(event, payload);
				return;
			}
		}
	}

	public void onSlotDrag(int[] slotNumber, int[] inventoryNumber, Action action) {
		HashMap<Integer, WSlot> slots = new HashMap<>();

		for (int i = 0; i < slotNumber.length; ++i) {
			for (WAbstractWidget widget : serverInterface.getAllWidgets()) {
				if (widget instanceof WSlot && ((WSlot) widget).getSlotNumber() == slotNumber[i] && ((WSlot) widget).getInventoryNumber() == inventoryNumber[i]) {
					slots.put(i, (WSlot) widget);
				}
			}
		}

		if (slots.isEmpty()) {
			return;
		}

		int split;

		if (action.isSplit()) {
			split = getPlayerInventory().getCursorStack().getCount() / slots.size();
		} else {
			split = 1;
		}

		ItemStack stackA;

		if (action.isPreview()) {
			stackA = getPlayerInventory().getCursorStack().copy();
		} else {
			stackA = getPlayerInventory().getCursorStack();
		}

		if (stackA.isEmpty()) {
			return;
		}


		for (Integer number : slots.keySet()) {
			WSlot slotA = slots.get(number);

			if (slotA.refuses(stackA)) continue;

			ItemStack stackB;

			if (action.isPreview()) {
				stackB = slotA.getStack().copy();
			} else {
				stackB = slotA.getStack();
			}

			MutablePair<ItemStack, ItemStack> stacks = StackUtilities.merge(stackA, stackB, split, Math.min(stackA.getMaxCount(), split));

			if (action.isPreview()) {
				slotA.getInterface().getContainer().previewCursorStack = stacks.getFirst().copy();
				slotA.setPreviewStack(stacks.getSecond().copy());
			} else {
				stackA = stacks.getFirst();
				slotA.getInterface().getContainer().previewCursorStack = ItemStack.EMPTY;
				slotA.setStack(stacks.getSecond());
			}
		}
	}

	public PlayerInventory getPlayerInventory() {
		return (PlayerInventory) linkedInventories.get(PLAYER_INVENTORY);
	}

	public void onSlotAction(int slotNumber, int inventoryNumber, int button, Action action, PlayerEntity player) {
		WSlot slotT = null;

		for (WAbstractWidget widget : serverInterface.getAllWidgets()) {
			if (widget instanceof WSlot && ((WSlot) widget).getSlotNumber() == slotNumber && ((WSlot) widget).getInventoryNumber() == inventoryNumber) {
				slotT = (WSlot) widget;
			}
		}

		if (slotT == null) {
			return;
		}

		WSlot slotA = slotT;

		ItemStack stackA = slotA.getStack().copy();
		ItemStack stackB = player.inventory.getCursorStack().copy();

		PlayerInventory inventory = getPlayerInventory();

		switch (action) {
			case PICKUP: {
				if (!StackUtilities.equalItemAndTag(stackA, stackB)) {
					if (button == 0) { // Interact with existing // LMB
						if (slotA.isOverrideMaximumCount()) {
							if (stackA.isEmpty()) {
								if (slotA.refuses(stackB)) return;

								StackUtilities.merge(stackB, stackA, stackB.getMaxCount(), slotA.getMaxCount()).apply(inventory::setCursorStack, slotA::acceptStack);
							} else if (stackB.isEmpty()) {
								if (slotA.refuses(stackB)) return;

								StackUtilities.merge(stackA, stackB, slotA.getInventoryNumber() == PLAYER_INVENTORY ? stackB.getMaxCount() : slotA.getMaxCount(), stackB.getMaxCount()).apply(slotA::acceptStack, inventory::setCursorStack);
							}
						} else {
							if (!stackB.isEmpty() && slotA.refuses(stackB)) return;

							StackUtilities.merge(stackA, stackB, stackA.isEmpty() || slotA.getInventoryNumber() == PLAYER_INVENTORY ? stackB.getMaxCount() : slotA.getMaxCount(), stackB.getMaxCount()).apply(slotA::acceptStack, inventory::setCursorStack);
						}
					} else if (button == 1 && !stackB.isEmpty()) { // Interact with existing // RMB
						StackUtilities.merge(inventory::getCursorStack, slotA::getStack, inventory.getCursorStack()::getMaxCount, () -> (slotA.getStack().getCount() == slotA.getMaxCount() ? 0 : slotA.getStack().getCount() + 1)).apply(inventory::setCursorStack, slotA::setStack);
					} else if (button == 1) { // Split existing // RMB
						StackUtilities.merge(slotA::getStack, inventory::getCursorStack, inventory.getCursorStack()::getMaxCount, () -> Math.max(1, Math.min(slotA.getStack().getMaxCount() / 2, slotA.getStack().getCount() / 2))).apply(slotA::setStack, inventory::setCursorStack);
					}
				} else {
					if (button == 0) {
						StackUtilities.merge(inventory::getCursorStack, slotA::getStack, stackB::getMaxCount, slotA::getMaxCount).apply(inventory::setCursorStack, slotA::setStack); // Add to existing // LMB
					} else {
						StackUtilities.merge(inventory::getCursorStack, slotA::getStack, inventory.getCursorStack()::getMaxCount, () -> (slotA.getStack().getCount() == slotA.getMaxCount() ? 0 : slotA.getStack().getCount() + 1)).apply(inventory::setCursorStack, slotA::setStack); // Add to existing // RMB
					}
				}
				break;
			}
			case CLONE: {
				if (player.isCreative()) {
					stackB = new ItemStack(stackA.getItem(), stackA.getMaxCount()); // Clone existing // MMB
					stackB.setTag(stackA.getTag());
					inventory.setCursorStack(stackB);
				}
				break;
			}
			case QUICK_MOVE: {
				for (WAbstractWidget widget : serverInterface.getAllWidgets()) {
					if (widget instanceof WSlot && ((WSlot) widget).getLinkedInventory() != slotA.getLinkedInventory()) {
						WSlot slotB = ((WSlot) widget);
						ItemStack stackC = slotB.getStack();
						stackA = slotA.getStack();

						if (slotB.refuses(stackA)) continue;

						if ((!slotA.getStack().isEmpty() && stackC.isEmpty()) || (StackUtilities.equalItemAndTag(stackA, stackC) && stackC.getCount() < (slotB.getInventoryNumber() == PLAYER_INVENTORY ? stackA.getMaxCount() : slotB.getMaxCount()))) {
							int maxB = stackC.isEmpty() || slotB.getInventoryNumber() == PLAYER_INVENTORY ? stackA.getMaxCount() : slotB.getMaxCount();
							StackUtilities.merge(slotA::getStack, slotB::getStack, slotA::getMaxCount, () -> maxB).apply(slotA::setStack, slotB::setStack);
							break;
						}
					}
				}
				break;
			}
			case PICKUP_ALL: {
				for (WAbstractWidget widget : getInterface().getAllWidgets()) {
					if (widget instanceof WSlot && StackUtilities.equalItemAndTag(((WSlot) widget).getStack(), stackB)) {
						WSlot slotB = (WSlot) widget;

						if (slotB.isLocked()) continue;

						StackUtilities.merge(slotB::getStack, inventory::getCursorStack, slotB::getMaxCount, stackB::getMaxCount).apply(slotB::setStack, inventory::setCursorStack);
					}
				}
			}
		}
	}

	public World getWorld() {
		return world;
	}

	public Inventory getInventory(int inventoryNumber) {
		return linkedInventories.get(inventoryNumber);
	}

	@Deprecated
	@Override
	public Slot addSlot(Slot slot) {
		return super.addSlot(slot);
	}

	@Deprecated
	@Override
	public ItemStack onSlotClick(int identifier, int button, SlotActionType action, PlayerEntity player) {
		return ItemStack.EMPTY;
	}

	@Override
	public void sendContentUpdates() {
		if (!(this.getPlayerInventory().player instanceof ServerPlayerEntity) || FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) return;

		for (WAbstractWidget widget : serverInterface.getAllWidgets()) {
			if (widget instanceof WSlot) {
				WSlot slotA = ((WSlot) widget);


				if (cachedInventories.get(slotA.getInventoryNumber()) != null && cachedInventories.get(slotA.getInventoryNumber()).get(slotA.getSlotNumber()) != null) {
					ItemStack stackA = slotA.getStack();
					ItemStack stackB = cachedInventories.get(slotA.getInventoryNumber()).get(slotA.getSlotNumber());

					if ((stackA.getTag() != stackB.getTag() || stackA.getItem() != stackB.getItem() || (!stackA.isEmpty() && !stackB.isEmpty() && stackA.getCount() != stackB.getCount()))) {
						ServerSidePacketRegistry.INSTANCE.sendToPlayer(this.getPlayerInventory().player, NetworkRegistry.SLOT_UPDATE_PACKET, NetworkRegistry.createSlotUpdatePacket(syncId, slotA.getSlotNumber(), slotA.getInventoryNumber(), slotA.getStack()));
					}

					cachedInventories.get(slotA.getInventoryNumber()).put(slotA.getSlotNumber(), slotA.getStack().copy());
				} else {
					cachedInventories.computeIfAbsent(slotA.getInventoryNumber(), value -> new HashMap<>());

					ItemStack stackA = slotA.getStack();
					ItemStack stackB = Optional.ofNullable(cachedInventories.get(slotA.getInventoryNumber()).get(slotA.getSlotNumber())).orElse(ItemStack.EMPTY);

					if ((stackA.getTag() != stackB.getTag() || stackA.getItem() != stackB.getItem() || (!stackA.isEmpty() && !stackB.isEmpty() && stackA.getCount() != stackB.getCount()))) {
						ServerSidePacketRegistry.INSTANCE.sendToPlayer(this.getPlayerInventory().player, NetworkRegistry.SLOT_UPDATE_PACKET, NetworkRegistry.createSlotUpdatePacket(syncId, slotA.getSlotNumber(), slotA.getInventoryNumber(), slotA.getStack()));
					}

					cachedInventories.get(slotA.getInventoryNumber()).put(slotA.getSlotNumber(), slotA.getStack().copy());
				}
			}
		}
	}

	@Override
	public void onContentChanged(Inventory inventory) {
		super.onContentChanged(inventory);
	}

	@Deprecated
	@Override
	public boolean canUse(PlayerEntity entity) {
		return true;
	}
}

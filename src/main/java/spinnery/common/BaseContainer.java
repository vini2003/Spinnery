package spinnery.common;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Tickable;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import spinnery.registry.NetworkRegistry;
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

		int split = -1;

		if (action == Action.DRAG_SPLIT || action == Action.DRAG_SPLIT_PREVIEW) {
			split = getPlayerInventory().getCursorStack().getCount() / slots.size();
		} else if (action == Action.DRAG_SINGLE || action == Action.DRAG_SINGLE_PREVIEW) {
			split = 1;
		}

		ItemStack stackA = ItemStack.EMPTY;

		if (action == Action.DRAG_SINGLE || action == Action.DRAG_SPLIT) {
			stackA = getPlayerInventory().getCursorStack();
		} else if (action == Action.DRAG_SINGLE_PREVIEW || action == Action.DRAG_SPLIT_PREVIEW) {
			stackA = getPlayerInventory().getCursorStack().copy();
		}

		if (stackA.isEmpty()) {
			return;
		}


		for (Integer number : slots.keySet()) {
			WSlot slotA = slots.get(number);

			if (slotA.refuses(stackA)) continue;

			ItemStack stackB = ItemStack.EMPTY;

			if (action == Action.DRAG_SINGLE || action == Action.DRAG_SPLIT) {
				stackB = slotA.getStack();
			} else if (action == Action.DRAG_SINGLE_PREVIEW || action == Action.DRAG_SPLIT_PREVIEW) {
				stackB = slotA.getStack().copy();
			}

			Pair<ItemStack, ItemStack> stacks = StackUtilities.clamp(stackA, stackB, split, split);

			if (action == Action.DRAG_SINGLE || action == Action.DRAG_SPLIT) {
				stackA = stacks.getFirst();
				slotA.getInterface().getContainer().previewCursorStack = ItemStack.EMPTY;
				slotA.setStack(stacks.getSecond());
			} else if (action == Action.DRAG_SINGLE_PREVIEW || action == Action.DRAG_SPLIT_PREVIEW) {
				slotA.getInterface().getContainer().previewCursorStack = stacks.getFirst().copy();
				slotA.setPreviewStack(stacks.getSecond().copy());
			}
		}
	}

	public PlayerInventory getPlayerInventory() {
		return (PlayerInventory) linkedInventories.get(PLAYER_INVENTORY);
	}

	public void onSlotAction(int slotNumber, int inventoryNumber, int button, Action action, PlayerEntity player) {
		WSlot slotA = null;

		for (WAbstractWidget widget : serverInterface.getAllWidgets()) {
			if (widget instanceof WSlot && ((WSlot) widget).getSlotNumber() == slotNumber && ((WSlot) widget).getInventoryNumber() == inventoryNumber) {
				slotA = (WSlot) widget;
			}
		}

		if (slotA == null) {
			return;
		}

		ItemStack stackA = slotA.getStack().copy();
		ItemStack stackB = player.inventory.getCursorStack().copy();

		switch (action) {
			case PICKUP: {
				if (!StackUtilities.equal(stackA, stackB)) {
					if (button == 0) { // Swap with existing // LMB
						if (slotA.isOverrideMaximumCount()) {
							if (stackA.isEmpty()) {
								if (slotA.refuses(stackB)) return;

								ItemStack stackC = stackA.copy();
								stackA = stackB.copy();
								stackB = stackC.copy();
							} else if (stackB.isEmpty()) {
								if (slotA.refuses(stackB)) return;

								int maxA = slotA.getMaxCount();
								int maxB = stackB.getMaxCount();

								int countA = stackA.getCount();
								int countB = stackB.getCount();

								int availableA = maxA - countA;
								int availableB = maxB - countB;

								ItemStack stackC = stackA.copy();
								stackC.setCount(Math.min(countA, availableB));
								stackB = stackC.copy();
								stackA.decrement(Math.min(countA, availableB));
							}
						} else {
							if (!stackB.isEmpty() && slotA.refuses(stackB)) return;

							ItemStack stackC = stackA.copy();
							stackA = stackB.copy();
							stackB = stackC.copy();
						}
					} else if (button == 1 && !stackB.isEmpty()) { // Add to existing // RMB
						if (stackA.isEmpty()) { // If existing is empty, initialize it // RMB
							stackA = new ItemStack(stackB.getItem(), 1);
							stackA.setTag(stackB.getTag());
							stackB.decrement(1);
						}
					} else if (button == 1) { // Split existing // RMB
						if (slotA.isOverrideMaximumCount()) {
							ItemStack stackC = stackA.split(Math.min(stackA.getCount(), stackA.getMaxCount()) / 2);
							stackB = stackC.copy();
						} else {
							ItemStack stackC = stackA.split(Math.max(1, stackA.getCount() / 2));
							stackB = stackC.copy();
						}
					}
				} else {
					if (button == 0) {
						StackUtilities.clamp(stackB, stackA, stackB.getMaxCount(), slotA.getMaxCount()); // Add to existing // LMB
					} else {
						boolean canStackTransfer = stackB.getCount() >= 1 && stackA.getCount() < slotA.getMaxCount();
						if (canStackTransfer) { // Add to existing // RMB
							stackA.increment(1);
							stackB.decrement(1);
						}
					}
				}
				break;
			}
			case CLONE: {
				if (player.isCreative()) {
					stackB = new ItemStack(stackA.getItem(), stackA.getMaxCount()); // Clone existing // MMB
					stackB.setTag(stackA.getTag());
				}
				break;
			}
			case QUICK_MOVE: {
				for (WAbstractWidget widget : serverInterface.getAllWidgets()) {
					if (widget instanceof WSlot && ((WSlot) widget).getLinkedInventory() != slotA.getLinkedInventory()) {
						WSlot slotB = ((WSlot) widget);
						ItemStack stackC = slotB.getStack();

						if (slotB.refuses(stackC)) continue;

						if (!stackA.isEmpty() && (stackC.getCount() < slotB.getMaxCount() || stackC.getCount() < stackA.getMaxCount())) {
							if (stackC.isEmpty() || (stackA.getItem() == stackC.getItem() && stackA.getTag() == stackC.getTag())) {
								Pair<ItemStack, ItemStack> result = StackUtilities.clamp(stackA, stackC, slotA.getMaxCount(), slotB.getMaxCount());
								stackA = result.getFirst();
								slotB.setStack(result.getSecond());
								break;

							}
						}
					}
				}
				break;
			}
			case PICKUP_ALL: {
				ItemStack stackC = getInterface().getContainer().getPlayerInventory().getCursorStack();

				for (WAbstractWidget widget : getInterface().getAllWidgets()) {
					if (widget instanceof WSlot && StackUtilities.equal(((WSlot) widget).getStack(), stackC)) {
						if (((WSlot) widget).isLocked()) continue;

						StackUtilities.clamp(((WSlot) widget).getStack(), stackC, ((WSlot) widget).getMaxCount(), stackC.getMaxCount());
					}
				}

				return;
			}
		}

		slotA.setStack(stackA);
		((PlayerInventory) linkedInventories.get(PLAYER_INVENTORY)).setCursorStack(stackB);
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
		if (!(this.getPlayerInventory().player instanceof ServerPlayerEntity)) return;

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

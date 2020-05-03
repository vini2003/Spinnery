package com.github.vini2003.spinnery.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;
import com.github.vini2003.spinnery.registry.NetworkRegistry;
import com.github.vini2003.spinnery.util.MutablePair;
import com.github.vini2003.spinnery.util.StackUtilities;
import com.github.vini2003.spinnery.widget.WAbstractWidget;
import com.github.vini2003.spinnery.widget.WInterface;
import com.github.vini2003.spinnery.widget.WSlot;
import com.github.vini2003.spinnery.widget.api.Action;
import com.github.vini2003.spinnery.widget.api.WNetworked;
import static com.github.vini2003.spinnery.registry.NetworkRegistry.*;

import java.util.*;

/**
 * A BaseContainer is a class responsible for
 * handling the data needed for the backend of
 * a BaseContainerScreen. It holds data that
 * needs to be synchronized with the server,
 * and is necessary for any widgets that
 * implement WNetworked to be added to it
 * and the BaseContainerScreen.
 * <p>
 * Note, however, that the widget added to
 * the BaseContainer should NOT contain
 * a Position, or a Size.
 */
public class BaseContainer extends Container {
	public static final int PLAYER_INVENTORY = 0;
	protected final WInterface serverInterface;
	public Map<Integer, IInventory> inventories = new HashMap<>();
	public Map<Integer, Map<Integer, ItemStack>> cachedInventories = new HashMap<>();
	protected Set<WSlot> splitSlots = new HashSet<>();
	protected Set<WSlot> singleSlots = new HashSet<>();
	protected Map<Integer, Map<Integer, ItemStack>> previewStacks = new HashMap<>();
	protected ItemStack previewCursorStack = ItemStack.EMPTY;
	protected World world;

	/**
	 * Instantiates a BaseContainer.
	 *
	 * @param synchronizationID ID to be used for synchronization of this container.
	 * @param playerInventory   PlayerInventory of Player associated with this container.
	 */
	public BaseContainer(int synchronizationID, PlayerInventory playerInventory) {
		super(null, synchronizationID);
		addInventory(PLAYER_INVENTORY, playerInventory);
		setWorld(playerInventory.player.world);
		serverInterface = new WInterface(this);
	}

	/**
	 * Retrieves map of inventories associated with this container.
	 *
	 * @return All inventories associated with this container, whose key is the inventory number.
	 */
	public Map<Integer, IInventory> getInventories() {
		return inventories;
	}

	/**
	 * Sets the World of this container.
	 *
	 * @param world World to be associated with this container.
	 */
	public <C extends BaseContainer> C setWorld(World world) {
		this.world = world;
		return (C) this;
	}

	/**
	 * Gets the preview ItemStack to be rendered instead of the default cursor ItemStack.
	 *
	 * @return previewCursorStack ItemStack to be rendered instead of the default cursor ItemStack.
	 */
	@OnlyIn(Dist.CLIENT)
	public ItemStack getPreviewCursorStack() {
		return previewCursorStack;
	}

	/**
	 * Sets the preview ItemStack to be rendered instead of the default cursor ItemStack.
	 *
	 * @param previewCursorStack ItemStack to be rendered instead of the default cursor ItemStack.
	 */
	@OnlyIn(Dist.CLIENT)
	public <C extends BaseContainer> C setPreviewCursorStack(ItemStack previewCursorStack) {
		this.previewCursorStack = previewCursorStack;
		return (C) this;
	}

	/**
	 * Flushes all WSlot drag information.
	 */
	@OnlyIn(Dist.CLIENT)
	public void flush() {
		getInterface().getContainer().getDragSlots(GLFW.GLFW_MOUSE_BUTTON_1).clear();
		getInterface().getContainer().getDragSlots(GLFW.GLFW_MOUSE_BUTTON_2).clear();
		getInterface().getContainer().getPreviewStacks().clear();
		getInterface().getContainer().setPreviewCursorStack(ItemStack.EMPTY);
	}

	/**
	 * Retrieves the set of WSlots on which the mouse has been dragged, given a mouse button.
	 *
	 * @param mouseButton Mouse button used for dragging.
	 * @return Set of WSlots on which the mouse has been dragged, given the mouse button.
	 */
	@OnlyIn(Dist.CLIENT)
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

	/**
	 * Retrieves the WInterface attached with this container.
	 *
	 * @return The WInterface attached with this container.
	 */
	public WInterface getInterface() {
		return serverInterface;
	}

	/**
	 * Retrieves the preview ItemStacks associated with all WSlots inventory numbers and numbers.
	 *
	 * @return ItemStacks of all WSlots, whose key is the inventory number, and value key is the number.
	 */
	@OnlyIn(Dist.CLIENT)
	public Map<Integer, Map<Integer, ItemStack>> getPreviewStacks() {
		return previewStacks;
	}

	/**
	 * Verifies if the mouse is currently being dragged through WSlots.
	 *
	 * @return Whether the mouse is currently being dragged through WSlots.
	 */
	@OnlyIn(Dist.CLIENT)
	public boolean isDragging() {
		return getDragSlots(GLFW.GLFW_MOUSE_BUTTON_1).isEmpty() || getDragSlots(GLFW.GLFW_MOUSE_BUTTON_2).isEmpty();
	}

	/**
	 * Method called on the server when a WNetworked widget
	 * WNetworked.Event happens.
	 *
	 * @param widgetSyncId Synchronization ID of the WNetworked widget; must match between client and server.
	 * @param event        WNetworked.Event which was sent.
	 * @param payload      CompoundNBT payload sent alongside the event.
	 */
	public void onInterfaceEvent(int widgetSyncId, WNetworked.Event event, CompoundNBT payload) {
		Set<WAbstractWidget> checkWidgets = serverInterface.getAllWidgets();
		for (WAbstractWidget widget : checkWidgets) {
			if (!(widget instanceof WNetworked)) continue;
			if (((WNetworked) widget).getSyncId() == widgetSyncId) {
				((WNetworked) widget).onInterfaceEvent(event, payload);
				return;
			}
		}
	}

	/**
	 * Method called when a drag Action is performed on a WSlot, both on the client and server.
	 *
	 * @param slotNumber      Number of WSlot in which the Action happened.
	 * @param inventoryNumber Inventory number of WSlot in which the Action happened.
	 * @param action          Action which was performed.
	 */
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
			split = getPlayerInventory().getItemStack().getCount() / slots.size();
		} else {
			split = 1;
		}

		ItemStack stackA;

		if (action.isPreview()) {
			stackA = getPlayerInventory().getItemStack().copy();
		} else {
			stackA = getPlayerInventory().getItemStack();
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

			MutablePair<ItemStack, ItemStack> stacks = StackUtilities.merge(stackA, stackB, split, Math.min(stackA.getMaxStackSize(), split));

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

	/**
	 * Method called when an Action is performed on a WSlot, both on the client and server.
	 *
	 * @param slotNumber      Number of WSlot in which the Action happened.
	 * @param inventoryNumber Inventory number of WSlot in which the Action happened.
	 * @param button          Button with which the action was performed.
	 * @param action          Action which was performed.
	 * @param player          Player whom performed the action.
	 *                        <p>
	 *                        As a warning, it just works. Any modifications are likely to cause
	 *                        severe brain damage to the poor individual attempting to change this.
	 */
	public void onSlotAction(int slotNumber, int inventoryNumber, int button, Action action, PlayerEntity player) {
		WSlot slotT = null;

		for (WAbstractWidget widget : serverInterface.getAllWidgets()) {
			if (widget instanceof WSlot && ((WSlot) widget).getSlotNumber() == slotNumber && ((WSlot) widget).getInventoryNumber() == inventoryNumber) {
				slotT = (WSlot) widget;
			}
		}

		if (slotT == null || slotT.isLocked()) {
			return;
		}

		WSlot slotA = slotT;

		ItemStack stackA = slotA.getStack().copy();
		ItemStack stackB = player.inventory.getItemStack().copy();

		PlayerInventory inventory = getPlayerInventory();

		switch (action) {
			case PICKUP: {

				if (!StackUtilities.equalItemAndTag(stackA, stackB)) {
					if (button == 0) { // Interact with existing // LMB
						if (slotA.isOverrideMaximumCount()) {
							if (stackA.isEmpty()) {
								if (slotA.refuses(stackB)) return;

								slotA.consume(action, Action.Subtype.FROM_CURSOR_TO_SLOT_CUSTOM_FULL_STACK);
								StackUtilities.merge(stackB, stackA, stackB.getMaxStackSize(), slotA.getMaxStackSize()).apply(inventory::setItemStack, slotA::acceptStack);
							} else if (stackB.isEmpty()) {
								if (slotA.refuses(stackB)) return;

								slotA.consume(action, Action.Subtype.FROM_SLOT_TO_CURSOR_CUSTOM_FULL_STACK);
								StackUtilities.merge(stackA, stackB, slotA.getInventoryNumber() == PLAYER_INVENTORY ? stackB.getMaxStackSize() : slotA.getMaxStackSize(), stackB.getMaxStackSize()).apply(slotA::acceptStack, inventory::setItemStack);
							}
						} else {
							if (!stackB.isEmpty() && slotA.refuses(stackB)) return;

							slotA.consume(action, Action.Subtype.FROM_CURSOR_TO_SLOT_DEFAULT_FULL_STACK);

							if (!StackUtilities.equalItemAndTag(stackA, stackB)) {
								slotA.setStack(stackB);
								player.inventory.setItemStack(stackA);
							} else {
								StackUtilities.merge(stackA, stackB, stackA.isEmpty() || slotA.getInventoryNumber() == PLAYER_INVENTORY ? stackB.getMaxStackSize() : slotA.getMaxStackSize(), stackB.getMaxStackSize()).apply(slotA::acceptStack, inventory::setItemStack);
							}
						}
					} else if (button == 1 && !stackB.isEmpty()) { // Interact with existing // RMB
						slotA.consume(action, Action.Subtype.FROM_CURSOR_TO_SLOT_CUSTOM_SINGLE_ITEM);
						StackUtilities.merge(inventory::getItemStack, slotA::getStack, inventory.getItemStack()::getMaxStackSize, () -> (slotA.getStack().getCount() == slotA.getMaxStackSize() ? 0 : slotA.getStack().getCount() + 1)).apply(inventory::setItemStack, slotA::setStack);
					} else if (button == 1) { // Split existing // RMB
						slotA.consume(action, Action.Subtype.FROM_SLOT_TO_CURSOR_DEFAULT_HALF_STACK);
						StackUtilities.merge(slotA::getStack, inventory::getItemStack, inventory.getItemStack()::getMaxStackSize, () -> Math.max(1, Math.min(slotA.getStack().getMaxStackSize() / 2, slotA.getStack().getCount() / 2))).apply(slotA::setStack, inventory::setItemStack);
					}
				} else {
					if (button == 0) {
						if (slotA.refuses(stackB)) return;

						slotA.consume(action, Action.Subtype.FROM_CURSOR_TO_SLOT_CUSTOM_FULL_STACK);
						StackUtilities.merge(inventory::getItemStack, slotA::getStack, stackB::getMaxStackSize, slotA::getMaxStackSize).apply(inventory::setItemStack, slotA::setStack); // Add to existing // LMB
					} else {
						if (slotA.refuses(stackB)) return;

						slotA.consume(action, Action.Subtype.FROM_CURSOR_TO_SLOT_CUSTOM_SINGLE_ITEM);
						StackUtilities.merge(inventory::getItemStack, slotA::getStack, inventory.getItemStack()::getMaxStackSize, () -> (slotA.getStack().getCount() == slotA.getMaxStackSize() ? 0 : slotA.getStack().getCount() + 1)).apply(inventory::setItemStack, slotA::setStack); // Add to existing // RMB
					}
				}
				break;
			}
			case CLONE: {
				if (player.isCreative()) {
					stackB = new ItemStack(stackA.getItem(), stackA.getMaxStackSize()); // Clone existing // MMB
					stackB.setTag(stackA.getTag());
					inventory.setItemStack(stackB);
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
						if (slotB.isLocked()) continue;

						if ((!slotA.getStack().isEmpty() && stackC.isEmpty()) || (StackUtilities.equalItemAndTag(stackA, stackC) && stackC.getCount() < (slotB.getInventoryNumber() == PLAYER_INVENTORY ? stackA.getMaxStackSize() : slotB.getMaxStackSize()))) {
							int maxB = stackC.isEmpty() || slotB.getInventoryNumber() == PLAYER_INVENTORY ? stackA.getMaxStackSize() : slotB.getMaxStackSize();
							slotA.consume(action, Action.Subtype.FROM_SLOT_TO_SLOT_CUSTOM_FULL_STACK);
							StackUtilities.merge(slotA::getStack, slotB::getStack, slotA::getMaxStackSize, () -> maxB).apply(slotA::setStack, slotB::setStack);
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

						slotB.consume(action, Action.Subtype.FROM_SLOT_TO_CURSOR_CUSTOM_FULL_STACK);
						StackUtilities.merge(slotB::getStack, inventory::getItemStack, slotB::getMaxStackSize, stackB::getMaxStackSize).apply(slotB::setStack, inventory::setItemStack);
					}
				}
			}
		}
	}

	/**
	 * Retrieves the World associated with this container.
	 *
	 * @return World associated with this container.
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Retrieves the PlayerInventory of the Player associated with this container.
	 *
	 * @return PlayerInventory of player associated with this container.
	 */
	public PlayerInventory getPlayerInventory() {
		return (PlayerInventory) inventories.get(PLAYER_INVENTORY);
	}

	/**
	 * Retrieves an inventory from the BaseContainer.
	 *
	 * @param inventoryNumber Inventory number associated with the inventory.
	 * @return Inventory associated with the inventory number.
	 */
	public IInventory getInventory(int inventoryNumber) {
		return inventories.get(inventoryNumber);
	}

	/**
	 * Adds an inventory to the BaseContainer.
	 *
	 * @param inventoryNumber Inventory number associated with the inventory.
	 * @param inventory       Inventory associated with the inventory number.
	 */
	public <C extends BaseContainer> C addInventory(int inventoryNumber, IInventory inventory) {
		this.inventories.put(inventoryNumber, inventory);
		return (C) this;
	}

	/**
	 * Dispatches packets for WSlots whose contents
	 * have changed since the last call.
	 */
	@Override
	public void detectAndSendChanges() {
		if (!(this.getPlayerInventory().player instanceof ServerPlayerEntity))
			return;

		for (WAbstractWidget widget : serverInterface.getAllWidgets()) {
			if (widget instanceof WSlot) {
				WSlot slotA = ((WSlot) widget);


				if (cachedInventories.get(slotA.getInventoryNumber()) != null && cachedInventories.get(slotA.getInventoryNumber()).get(slotA.getSlotNumber()) != null) {
					ItemStack stackA = slotA.getStack();
					ItemStack stackB = cachedInventories.get(slotA.getInventoryNumber()).get(slotA.getSlotNumber());

					if (stackA.getItem() != stackB.getItem() || stackA.getCount() != stackB.getCount() || (stackA.hasTag() && !stackA.getTag().equals(stackB.getOrCreateTag()))) {
						INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) this.getPlayerInventory().player), NetworkRegistry.createSlotUpdatePacket(windowId, slotA.getSlotNumber(), slotA.getInventoryNumber(), slotA.getStack()));
					}

					cachedInventories.get(slotA.getInventoryNumber()).put(slotA.getSlotNumber(), slotA.getStack().copy());
				} else {
					cachedInventories.computeIfAbsent(slotA.getInventoryNumber(), value -> new HashMap<>());

					ItemStack stackA = slotA.getStack();
					ItemStack stackB = Optional.ofNullable(cachedInventories.get(slotA.getInventoryNumber()).get(slotA.getSlotNumber())).orElse(ItemStack.EMPTY);

					if (stackA.getItem() != stackB.getItem() || stackA.getCount() != stackB.getCount() || (stackA.hasTag() && !stackA.getTag().equals(stackB.getOrCreateTag()))) {
						INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) this.getPlayerInventory().player), NetworkRegistry.createSlotUpdatePacket(windowId, slotA.getSlotNumber(), slotA.getInventoryNumber(), slotA.getStack()));
					}

					cachedInventories.get(slotA.getInventoryNumber()).put(slotA.getSlotNumber(), slotA.getStack().copy());
				}
			}
		}
	}

	/**
	 * Method deprecated and unsupported by Spinnery.
	 */
	@Deprecated
	@Override
	public Slot addSlot(Slot slot) {
		throw new UnsupportedOperationException(Slot.class.getName() + " cannot be added to a Spinnery " + BaseContainer.class.getName() + "!");
	}

	/**
	 * Method deprecated and unsupported by Spinnery.
	 */
	@Deprecated
	@Override
	public ItemStack slotClick(int identifier, int button, ClickType action, PlayerEntity player) {
		throw new UnsupportedOperationException(Container.class.getName() + "::onSlotClick cannot happen in a Spinnery " + BaseContainer.class.getName() + "!");
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}
}

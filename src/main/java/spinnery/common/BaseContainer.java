package spinnery.common;

import com.mojang.datafixers.util.Pair;
import net.minecraft.container.CraftingContainer;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.util.Tickable;
import net.minecraft.world.World;
import spinnery.widget.WCollection;
import spinnery.widget.WInterfaceHolder;
import spinnery.widget.WSlot;
import spinnery.widget.WWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseContainer extends CraftingContainer<Inventory> implements Tickable {
	public static final int PLAYER_INVENTORY = 0;

	public Map<Integer, Inventory> linkedInventories = new HashMap<>();
	public int positionY = 0;
	public int positionX = 0;

	protected World linkedWorld;
	protected WInterfaceHolder interfaceHolder = new WInterfaceHolder();

	public BaseContainer(int synchronizationID, PlayerInventory linkedPlayerInventory) {
		super(null, synchronizationID);
		getInventories().put(PLAYER_INVENTORY, linkedPlayerInventory);
		setLinkedWorld(linkedPlayerInventory.player.world);
	}

	public WInterfaceHolder getInterfaces() {
		return interfaceHolder;
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

	@Deprecated
	@Override
	public boolean canUse(PlayerEntity entity) {
		return true;
	}

	public static void mergeStacks(ItemStack stackA, ItemStack stackB, int maxA, int maxB) {
		int countA = stackA.getCount();
		int countB = stackB.getCount();

		int availableA = maxA - countA;
		int availableB = maxB - countB;

		stackB.increment(Math.min(countA, availableB));
		stackA.setCount(Math.max(countA - availableB, 0));
	}

	/**
	 * @param stackA Source ItemStack
	 * @param stackB Destination ItemStack
	 * @param maxA   Max. count of stackA
	 * @param maxB   Max. count of stackB
	 * @return Results
	 */
	public static Pair<ItemStack, ItemStack> clamp(ItemStack stackA, ItemStack stackB, int maxA, int maxB) {
		Item itemA = stackA.getItem();
		Item itemB = stackB.getItem();

		if (stackA.isItemEqual(stackB)) {
			mergeStacks(stackA, stackB, maxA, maxB);
		} else {
			if (stackA.isEmpty() && !stackB.isEmpty()) {
				int countA = stackA.getCount();
				int availableA = maxA - countA;

				int countB = stackB.getCount();

				stackA = new ItemStack(itemB, Math.min(countB, availableA));
				stackB.decrement(Math.min(countB, availableA));
			} else if (stackB.isEmpty() && !stackA.isEmpty()) {
				int countB = stackB.getCount();
				int availableB = maxB - countB;

				int countA = stackA.getCount();

				stackB = new ItemStack(itemA, Math.min(countA, availableB));
				stackA.decrement(Math.min(countA, availableB));
			}
		}

		return new Pair<>(stackA, stackB);
	}

	public void onSlotClicked(int slotNumber, int inventoryNumber, int button, SlotActionType action, PlayerEntity player) {
		Optional<WWidget> optionalWSlot = getInterfaces().getWidgets().stream().filter((widget) ->
				(widget instanceof WSlot && ((WSlot) widget).getSlotNumber() == slotNumber && ((WSlot) widget).getInventoryNumber() == inventoryNumber)
						|| (widget instanceof WCollection && ((WCollection) widget).getWidgets().stream().anyMatch(slot -> widget instanceof  WSlot && ((WSlot) slot).getSlotNumber() == slotNumber && ((WSlot) slot).getInventoryNumber() == inventoryNumber))).findFirst();

		WSlot slotA;
		if (optionalWSlot.isPresent()) {
			slotA = (WSlot) optionalWSlot.get();
		} else {
			return;
		}

		ItemStack stackA = slotA.getStack();
		ItemStack stackB = player.inventory.getCursorStack();

		switch (action) {
			case PICKUP: {
				if (!stackA.isItemEqual(stackB)) {
					if (button == 0) { // Swap with existing // LMB
						if (slotA.isOverrideMaximumCount()) {
							if (stackA.isEmpty()) {
								ItemStack stackC = stackA.copy();
								stackA = stackB.copy();
								stackB = stackC.copy();
							} else if (stackB.isEmpty()) {
								int maxA = slotA.getMaximumCount();
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
							ItemStack stackC = stackA.copy();
							stackA = stackB.copy();
							stackB = stackC.copy();
						}
					} else if (button == 1 && !stackB.isEmpty()) { // Add to existing // RMB
						if (stackA.isEmpty()) { // If existing is empty, initialize it // RMB
							stackA = new ItemStack(stackB.getItem(), 1);
							stackB.decrement(1);
						}
					} else if (button == 1) { // Split existing // RMB
						if (slotA.isOverrideMaximumCount()) {
							ItemStack stackC = stackA.split(Math.min(stackA.getCount(), stackA.getMaxCount()) / 2);
							stackB = stackC.copy();
						} else {
							ItemStack stackC = stackA.split(stackA.getCount() / 2);
							stackB = stackC.copy();
						}
					}
				} else {
					if (button == 0) {
						mergeStacks(stackB, stackA, stackB.getMaxCount(), slotA.getMaximumCount()); // Add to existing // LMB
					} else {
						boolean canStackTransfer = stackB.getCount() >= 1 && stackA.getCount() < slotA.getMaximumCount();
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
				}
				break;
			}
			case QUICK_MOVE: {
				List<WWidget> widgets = getInterfaces().getWidgets();
				List<WWidget> newWidgets = new ArrayList<>();
				for (WWidget widget : widgets) {
					if (widget instanceof WCollection) {
						newWidgets.addAll(((WCollection) widget).getWidgets());
					}
				}
				widgets.addAll(newWidgets);

				for (WWidget widget : widgets) {
					if (widget != slotA && widget instanceof WSlot && ((WSlot) widget).getLinkedInventory() != slotA.getLinkedInventory()) {
						ItemStack stackC = ((WSlot) widget).getStack();
						if ((stackC.getCount() < ((WSlot) widget).getMaximumCount() || stackC.getCount() < ((WSlot) widget).getStack().getMaxCount()) && !slotA.getStack().isEmpty() && (((WSlot) widget).getStack().isEmpty() || slotA.getStack().isItemEqual(((WSlot) widget).getStack()))) { // Merge with existing // LFSHIFT + LMB
							Pair<ItemStack, ItemStack> result = clamp(slotA.getStack(), ((WSlot) widget).getStack(), slotA.getMaximumCount(), ((WSlot) widget).getMaximumCount());
							stackA = result.getFirst();
							((WSlot) widget).setStack(result.getSecond());
							break;
						}
					}
				}
				break;
			}
		}
		slotA.setStack(stackA);
		((PlayerInventory) linkedInventories.get(PLAYER_INVENTORY)).setCursorStack(stackB);
	}


	public World getLinkedWorld() {
		return linkedWorld;
	}

	public void setLinkedWorld(World linkedWorld) {
		this.linkedWorld = linkedWorld;
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	public PlayerInventory getLinkedPlayerInventory() {
		return (PlayerInventory) linkedInventories.get(PLAYER_INVENTORY);
	}

	public Map<Integer, Inventory> getInventories() {
		return linkedInventories;
	}

	public Inventory getInventory(int inventoryNumber) {
		return linkedInventories.get(inventoryNumber);
	}

	@Deprecated
	@Override
	public void populateRecipeFinder(RecipeFinder recipeFinder) {
		return;
	}

	@Deprecated
	@Override
	public void clearCraftingSlots() {
	}

	@Deprecated
	@Override
	public boolean matches(Recipe<? super Inventory> recipe) {
		return false;
	}

	@Deprecated
	@Override
	public int getCraftingResultSlotIndex() {
		return -1;
	}

	@Deprecated
	@Override
	public int getCraftingWidth() {
		return 0;
	}

	@Deprecated
	@Override
	public int getCraftingHeight() {
		return 0;
	}

	@Deprecated
	@Override
	public int getCraftingSlotCount() {
		return 0;
	}

	@Override
	public void tick() {
	}
}

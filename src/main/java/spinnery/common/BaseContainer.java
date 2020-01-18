package spinnery.common;

import net.minecraft.container.CraftingContainer;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.util.Tickable;
import net.minecraft.world.World;
import spinnery.widget.WList;
import spinnery.widget.WPanel;
import spinnery.widget.WSlot;
import spinnery.widget.WWidget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseContainer extends CraftingContainer<Inventory> implements Tickable {
	public static final int PLAYER_INVENTORY = 0;
	public Map<Integer, WSlot> dragSlots = new HashMap<>();
	public Map<Integer, Inventory> linkedInventories = new HashMap<>();
	public int positionY = 0;
	public int positionX = 0;
	public int totalID = 0;
	protected World linkedWorld;
	protected WPanel linkedPanel;

	public BaseContainer(int synchronizationID, PlayerInventory linkedPlayerInventory) {
		super(null, synchronizationID);
		linkedInventories.put(PLAYER_INVENTORY, linkedPlayerInventory);
		setLinkedWorld(linkedPlayerInventory.player.world);
	}

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

	public void mergeStacks(ItemStack stackA, ItemStack stackB) {
		int maxA = stackA.getMaxCount();
		int maxB = stackB.getMaxCount();

		int countA = stackA.getCount();
		int countB = stackB.getCount();

		int availableA = maxA - countA;
		int availableB = maxB - countB;

		stackB.increment(Math.min(countA, availableB));
		stackA.setCount(Math.max(countA - availableB, 0));
	}

	public void onSlotClicked(int slotNumber, int inventoryNumber, int button, SlotActionType action, PlayerEntity player) {
		Optional<WWidget> optionalWSlot = getLinkedPanel().getLinkedWidgets().stream().filter((widget) -> (widget instanceof WSlot && ((WSlot) widget).getSlotNumber() == slotNumber && ((WSlot) widget).getInventoryNumber() == inventoryNumber)).findFirst();

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
						ItemStack stackC = stackA.copy();
						stackA = stackB.copy();
						stackB = stackC.copy();
					} else if (button == 1 && !stackB.isEmpty()) { // Add to existing // RMB
						if (stackA.isEmpty()) { // If existing is empty, initialize it // RMB
							stackA = new ItemStack(stackB.getItem(), 1);
							stackB.decrement(1);
						}
					} else if (button == 1) { // Split existing // RMB
						ItemStack stackC = stackA.split(stackA.getCount() / 2);
						stackB = stackC.copy();
					}
				} else {
					if (button == 0) {
						mergeStacks(stackB, stackA); // Add to existing // LMB
					} else {
						boolean canStackTransfer = stackB.getCount() >= 1 && stackA.getCount() < stackA.getMaxCount();
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
				for (WWidget widget : getLinkedPanel().getLinkedWidgets()) {
					if (widget != slotA && widget instanceof WSlot && ((WSlot) widget).getLinkedInventory() != slotA.getLinkedInventory()) {
						ItemStack stackC = ((WSlot) widget).getStack();
						if (!(stackC.getCount() == stackC.getMaxCount())) {
							if (stackA.isItemEqual(stackC)) { // Merge with existing // LFSHIFT + LMB
								mergeStacks(stackA, stackC);
								((WSlot) widget).setStack(stackC);
								break;
							} else if (stackC.isEmpty()) { // Swap with existing // LSHIFT + LMB
								((WSlot) widget).setStack(stackA.copy());
								stackA = ItemStack.EMPTY;
								break;
							}
						}
					}
				}
				break;
			}
		}
		slotA.setStack(stackA);
		((PlayerInventory) linkedInventories.get(PLAYER_INVENTORY)).setCursorStack(stackB);
	}

	public WPanel getLinkedPanel() {
		return linkedPanel;
	}

	public void setLinkedPanel(WPanel linkedPanel) {
		this.linkedPanel = linkedPanel;
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

	public Map<Integer, Inventory> getLinkedInventories() {
		return linkedInventories;
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
		for (WWidget widgetA : getLinkedPanel().getLinkedWidgets()) {
			if (!(widgetA instanceof WList)) {
				widgetA.tick();
			} else {
				for (List<WWidget> widgetB : ((WList) widgetA).getListWidgets()) {
					for (WWidget widgetC : widgetB) {
						widgetC.tick();
					}
				}
			}
		}
	}
}

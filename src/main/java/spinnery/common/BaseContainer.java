package spinnery.common;

import com.mojang.datafixers.util.Pair;
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
import spinnery.util.StackUtilities;
import spinnery.widget.WCollection;
import spinnery.widget.WInterfaceHolder;
import spinnery.widget.WSlot;
import spinnery.widget.WWidget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BaseContainer extends CraftingContainer<Inventory> implements Tickable {
	public static final int PLAYER_INVENTORY = 0;

	public Map<Integer, Inventory> linkedInventories = new HashMap<>();

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

	public void onSlotClicked(int slotNumber, int inventoryNumber, int button, SlotActionType action, PlayerEntity player) {
		WSlot slotA = null;

		List<WWidget> checkWidgets = getInterfaces().getWidgets();

		for (int i = 0; i < checkWidgets.size(); ++i) {
			WWidget widget = checkWidgets.get(i);
			if (widget instanceof WCollection) {
				checkWidgets.addAll(((WCollection) widget).getWidgets());
			} else if (widget instanceof WSlot && ((WSlot) widget).getSlotNumber() == slotNumber && ((WSlot) widget).getInventoryNumber() == inventoryNumber) {
				slotA = (WSlot) widget;
			}
		}

		if (slotA == null) {
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
				}
				break;
			}
			case QUICK_MOVE: {
				List<WWidget> widgets = getInterfaces().getWidgets();

				for (int i = 0; i < widgets.size(); ++i) {
					WWidget widget = widgets.get(i);
					if (widget instanceof WCollection) {
						widgets.addAll(((WCollection) widget).getWidgets());
					} else if (widget instanceof WSlot && ((WSlot) widget).getLinkedInventory() != slotA.getLinkedInventory()) {
						WSlot slotB = ((WSlot) widget);
						ItemStack stackC = slotB.getStack();

						if (!stackA.isEmpty() && (stackC.getCount() < slotB.getMaxCount() || stackC.getCount() < stackA.getMaxCount())) {
							if (stackC.isEmpty() || stackA.isItemEqual(stackC)) {
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

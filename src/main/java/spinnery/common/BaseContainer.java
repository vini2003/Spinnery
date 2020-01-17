package spinnery.common;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.container.CraftingContainer;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.util.Tickable;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import spinnery.widget.WList;
import spinnery.widget.WPanel;
import spinnery.widget.WSlot;
import spinnery.widget.WWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BaseContainer extends CraftingContainer<Inventory> implements Tickable {
	public List<WSlot> dragSlots = new ArrayList<>();
	public int positionY = 0;
	public int positionX = 0;
	protected PlayerInventory linkedPlayerInventory;
	protected Inventory linkedInventory;
	protected World linkedWorld;
	protected WPanel linkedPanel;

	public BaseContainer(int synchronizationID, PlayerInventory newLinkedPlayerInventory) {
		super(null, synchronizationID);
		setLinkedPlayerInventory(newLinkedPlayerInventory);
		setLinkedWorld(newLinkedPlayerInventory.player.world);
	}

	public Slot addSlot(Slot slot) {
		return super.addSlot(slot);
	}

	public void mergeStacks(ItemStack stackA, ItemStack stackB) {
		int maxA = stackA.getMaxCount();
		int maxB = stackB.getMaxCount();

		int countA = stackA.getCount();
		int countB = stackB.getCount();

		int availableB = maxB - countB;

		stackA.setCount(Math.min(countA - availableB, 0));
		stackB.increment(Math.min(countA, availableB));
	}

	@Deprecated
	@Override
	public ItemStack onSlotClick(int slot, int button, SlotActionType action, PlayerEntity player) {
		WSlot slotA = null;
		for (WWidget widget : getLinkedPanel().getLinkedWidgets()) {
			if (widget instanceof WSlot && ((WSlot) widget).getSlotNumber() == slot) {
				slotA = ((WSlot) widget);
				break;
			}
		}

		ItemStack stackA = slotA.getStack();
		ItemStack stackB = player.inventory.getCursorStack();

		switch (action) {
			case PICKUP: {
				if (!stackA.isItemEqual(stackB)) {
					ItemStack stackC = stackA.copy();
					stackA = stackB.copy();
					stackB = stackC.copy();
				} else {
					if (button == 0) {
						mergeStacks(stackB, stackA);
					} else {
						boolean canStackTransfer = stackA.getCount() >= 1 && stackB.getCount() < stackB.getMaxCount();
						if (canStackTransfer) {
							stackA.decrement(1);
							stackB.increment(1);
						}
					}
				}
			}
			case CLONE: {
				stackB = new ItemStack(stackA.getItem(), stackA.getMaxCount());
			}
			case QUICK_MOVE: {
				for (WWidget widget : getLinkedPanel().getLinkedWidgets()) {
					if (widget instanceof WSlot && ((WSlot) widget).getStack().isItemEqual(stackA)) {
						mergeStacks(stackA, stackB);
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}

	@Deprecated
	@Override
	public boolean canUse(PlayerEntity entity) {
		return true;
	}

	public WPanel getLinkedPanel() {
		return linkedPanel;
	}

	public void setLinkedPanel(WPanel linkedWPanel) {
		this.linkedPanel = linkedWPanel;
	}

	public Inventory getLinkedInventory() {
		return linkedInventory;
	}

	public void setLinkedInventory(Inventory linkedInventory) {
		this.linkedInventory = linkedInventory;
	}

	public PlayerInventory getLinkedPlayerInventory() {
		return linkedPlayerInventory;
	}

	public void setLinkedPlayerInventory(PlayerInventory linkedPlayerInventory) {
		this.linkedPlayerInventory = linkedPlayerInventory;
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

	public List<WSlot> getDragSlots() {
		return dragSlots;
	}

	public void setDragSlots(List<WSlot> dragSlots) {
		this.dragSlots = dragSlots;
	}

	@Deprecated
	@Override
	public void populateRecipeFinder(RecipeFinder recipeFinder) {
		if (linkedInventory instanceof RecipeInputProvider) {
			((RecipeInputProvider) linkedInventory).provideRecipeInputs(recipeFinder);
		}
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
		return - 1;
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
			if (! (widgetA instanceof WList)) {
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

package spinnery.container.common;

import spinnery.container.common.widget.WList;
import spinnery.container.common.widget.WPanel;
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
import spinnery.container.common.widget.WSlot;
import spinnery.container.common.widget.WWidget;

import java.util.ArrayList;
import java.util.List;

public class BaseContainer extends CraftingContainer<Inventory> implements Tickable {
	protected PlayerInventory linkedPlayerInventory;
	protected Inventory linkedInventory;
	protected World linkedWorld;
	protected WPanel linkedPanel;
	public List<WSlot> dragSlots = new ArrayList<>();

	public int positionY = 0;
	public int positionX = 0;

	public Slot addSlot(Slot slot) {
		return super.addSlot(slot);
	}

	public BaseContainer(int synchronizationID, PlayerInventory newLinkedPlayerInventory) {
		super(null, synchronizationID);
		setLinkedPlayerInventory(newLinkedPlayerInventory);
		setLinkedWorld(newLinkedPlayerInventory.player.world);
	}

	/**
	 * Set linked WPanel.
	 * @param linkedWPanel WPanel to bet set.
	 */
	public void setLinkedPanel(WPanel linkedWPanel) {
		this.linkedPanel = linkedWPanel;
	}

	/**
	 * Get linked WPanel.
	 * @return Retrieved WPanel.
	 */
	public WPanel getLinkedPanel() {
		return linkedPanel;
	}

	/**
	 * Set linked Inventory.
	 * @param linkedInventory Inventory to be set.
	 */
	public void setLinkedInventory(Inventory linkedInventory) {
		this.linkedInventory = linkedInventory;
	}

	/**
	 * Get linked Inventory.
	 * @return Retrieved Inventory.
	 */
	public Inventory getLinkedInventory() {
		return linkedInventory;
	}

	/**
	 * Set linked PlayerInventory.
	 * @param linkedPlayerInventory PlayerInventory to be set.
	 */
	public void setLinkedPlayerInventory(PlayerInventory linkedPlayerInventory) {
		this.linkedPlayerInventory = linkedPlayerInventory;
	}

	/**
	 * Get linked PlayerInventory.
	 * @return Retrieved PlayerInventory.
	 */
	public PlayerInventory getLinkedPlayerInventory() {
		return linkedPlayerInventory;
	}

	public void setLinkedWorld(World linkedWorld) {
		this.linkedWorld = linkedWorld;
	}

	public World getLinkedWorld() {
		return linkedWorld;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	public int getPositionY() {
		return positionY;
	}

	public List<WSlot> getDragSlots() {
		return dragSlots;
	}

	public void setDragSlots(List<WSlot> dragSlots) {
		this.dragSlots = dragSlots;
	}

	/**
	 * Defines custom behavior for 'onSlotClick'.
	 * @param slot Slot clicked.
	 * @param button WToggle clicked.
	 * @param action Action type.
	 * @param player Player entity which did action.
	 * @return ItemStack remaining after click.
	 */
	@Deprecated
	@Override
	public ItemStack onSlotClick(int slot, int button, SlotActionType action, PlayerEntity player) {
		return super.onSlotClick(slot, button, action, player);
	}

	/**
	 * Define custom behavior for 'canUse'.
	 * @return Boolean of can use result.
	 */
	@Deprecated
	@Override
	public boolean canUse(PlayerEntity entity) {
		return true;
	}

	/**
	 * Defines custom behavior for 'populateRecipeFinder'.
	 * @param recipeFinder ReciperFinder to be manipulated.
	 */
	@Deprecated
	@Override
	public void populateRecipeFinder(RecipeFinder recipeFinder) {
		if (linkedInventory instanceof RecipeInputProvider) {
			((RecipeInputProvider) linkedInventory).provideRecipeInputs(recipeFinder);
		}
	}

	/**
	 * Defines custom behavior for 'clearCraftingSlots'.
	 */
	@Deprecated
	@Override
	public void clearCraftingSlots() {
	}

	/**
	 * Defines custom behavior for 'matches'.
	 * @param recipe Recipe to find a match for.
	 * @return Boolean of match result.
	 */
	@Deprecated
	@Override
	public boolean matches(Recipe<? super Inventory> recipe) {
		return false;
	}

	/**
	 * Defines custom behavior for 'getCraftingResultSlotIndex'.
	 * @return Integer of slot index.
	 */
	@Deprecated
	@Override
	public int getCraftingResultSlotIndex() {
		return -1;
	}

	/**
	 * Defines custom behavior for 'getCraftingWidth'.
	 * @return Integer of crafting grid width.
	 */
	@Deprecated
	@Override
	public int getCraftingWidth() {
		return 0;
	}

	/**
	 * Defines custom behavior for 'getCraftingHeight'.
	 * @return Integer of crafting grid height.
	 */
	@Deprecated
	@Override
	public int getCraftingHeight() {
		return 0;
	}

	/**
	 * Defines custom behavior for 'getCraftingSlotCount'.
	 * @return Integer of crafting grid slot count.
	 */
	@Deprecated
	@Override
	public int getCraftingSlotCount() {
		return 0;
	}

	/**
	 * Defines custom behavior for 'tick'.
	 */
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

package spinnery.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryListener;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.util.DefaultedList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A BaseInventory is a class responsible for
 * effectively handling what a BasicInventory
 * does, however, allowing stack sizes
 * higher than the default of 64.
 */
public class BaseInventory implements Inventory, RecipeInputProvider {
	protected int size;
	protected DefaultedList<ItemStack> stacks;
	protected List<InventoryListener> listeners = new ArrayList<>();

	public BaseInventory(int size) {
		this.size = size;
		this.stacks = DefaultedList.ofSize(size, ItemStack.EMPTY);
	}

	public BaseInventory(ItemStack... items) {
		this.size = items.length;
		this.stacks = DefaultedList.copyOf(ItemStack.EMPTY, items);
	}

	public void addListener(InventoryListener... listeners) {
		this.listeners.addAll(Arrays.asList(listeners));
	}

	public void removeListener(InventoryListener... listeners) {
		this.listeners.removeAll(Arrays.asList(listeners));
	}

	@Override
	public int getInvSize() {
		return this.size;
	}

	@Override
	public boolean isInvEmpty() {
		return this.stacks.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getInvStack(int slot) {
		return slot >= 0 && slot < this.stacks.size() ? this.stacks.get(slot) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack takeInvStack(int slot, int amount) {
		ItemStack itemStack = Inventories.splitStack(this.stacks, slot, amount);
		if (!itemStack.isEmpty()) {
			this.markDirty();
		}

		return itemStack;
	}

	@Override
	public ItemStack removeInvStack(int slot) {
		ItemStack itemStack = this.stacks.get(slot);
		if (itemStack.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			this.stacks.set(slot, ItemStack.EMPTY);
			this.markDirty();
			return itemStack;
		}
	}

	@Override
	public void setInvStack(int slot, ItemStack stack) {
		this.stacks.set(slot, stack);

		this.markDirty();
	}

	@Override
	public void markDirty() {
		for (InventoryListener listener : listeners) {
			listener.onInvChange(this);
		}
	}

	@Override
	public boolean canPlayerUseInv(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		this.stacks.clear();
		this.markDirty();
	}

	public String toString() {
		return (this.stacks.stream().filter((itemStack) -> !itemStack.isEmpty()).collect(Collectors.toList())).toString();
	}

	@Override
	public void provideRecipeInputs(RecipeFinder recipeFinder) {
	}
}

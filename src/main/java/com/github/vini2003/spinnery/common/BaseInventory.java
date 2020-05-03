package com.github.vini2003.spinnery.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

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
public class BaseInventory implements IInventory {
	protected int size;
	protected NonNullList<ItemStack> stacks;
	protected List<IInventoryChangedListener> listeners = new ArrayList<>();

	public BaseInventory(int size) {
		this.size = size;
		this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	public BaseInventory(ItemStack... items) {
		this.size = items.length;
		this.stacks = NonNullList.from(ItemStack.EMPTY, items);
	}

	public void addListener(IInventoryChangedListener... listeners) {
		this.listeners.addAll(Arrays.asList(listeners));
	}

	public void removeListener(IInventoryChangedListener... listeners) {
		this.listeners.removeAll(Arrays.asList(listeners));
	}

	@Override
	public int getSizeInventory() {
		return this.size;
	}

	@Override
	public boolean isEmpty() {
		return this.stacks.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return slot >= 0 && slot < this.stacks.size() ? this.stacks.get(slot) : ItemStack.EMPTY;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack itemStack = ItemStackHelper.getAndSplit(this.stacks, slot, amount);
		if (!itemStack.isEmpty()) {
			this.markDirty();
		}

		return itemStack;
	}

	@Override
	public ItemStack removeStackFromSlot(int slot) {
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
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.stacks.set(slot, stack);

		this.markDirty();
	}

	@Override
	public void markDirty() {
		for (IInventoryChangedListener listener : listeners) {
			listener.onInventoryChanged(this);
		}
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
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
}

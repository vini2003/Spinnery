package com.github.vini2003.spinnery.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import org.apache.logging.log4j.Level;
import com.github.vini2003.spinnery.Spinnery;
import com.github.vini2003.spinnery.common.BaseInventory;

public class InventoryUtilities {
	/**
	 * Write inventory contents to a CompoundNBT with support for ItemStacks greater than 64 in size.
	 *
	 * @param inventory Inventory CompoundNBT will be written from.
	 * @return Tag from inventory.
	 */
	@Deprecated
	public static <T extends IInventory> CompoundNBT writeUnsafe(T inventory, CompoundNBT tag) {
		for (int i = 0; i < inventory.getSizeInventory(); ++i) {
			tag.put(String.valueOf(i), inventory.getStackInSlot(i).write(new CompoundNBT()));
		}

		return tag;
	}

	/**
	 * Read inventory contents from a CompoundNBT with support for ItemStacks greater than 64 in size.
	 *
	 * @param tag Tag Inventory will be read from.
	 * @return Inventory from tag.
	 */
	public static <T extends IInventory> T readUnsafe(T inventory, CompoundNBT tag) {
		for (int i = 0; i < inventory.getSizeInventory(); ++i) {
			try {
				inventory.setInventorySlotContents(i, ItemStack.read((CompoundNBT) tag.get(String.valueOf(i))));
			} catch (IndexOutOfBoundsException exception) {
				Spinnery.LOGGER.log(Level.ERROR, "[Spinnery] Inventory contents failed to be written: inventory size smaller than necessary!");
				return inventory;
			}
		}

		return inventory;
	}

	/**
	 * Write inventory contents to a CompoundNBT with support for ItemStacks greater than 64 in size.
	 */
	public static CompoundNBT write(IInventory inventory) {
		return write(inventory, null);
	}

	/**
	 * Write inventory contents to a CompoundNBT with support for ItemStacks greater than 64 in size.
	 */
	public static CompoundNBT write(IInventory inventory, CompoundNBT tag) {
		if (inventory == null || inventory.getSizeInventory() <= 0) return StackUtilities.TAG_EMPTY;

		if (tag == null) tag = new CompoundNBT();

		CompoundNBT inventoryTag = new CompoundNBT();

		CompoundNBT stacksTag = new CompoundNBT();

		inventoryTag.putInt("size", inventory.getSizeInventory());

		for (int position = 0; position < inventory.getSizeInventory(); ++position) {
			if (inventory.getStackInSlot(position) != null && inventory.getStackInSlot(position) != ItemStack.EMPTY) {
				ItemStack stack = inventory.getStackInSlot(position);

				if (stack != null && !stack.isEmpty()) {
					CompoundNBT stackTag = inventory.getStackInSlot(position).write(new CompoundNBT());

					if (stackTag != StackUtilities.TAG_EMPTY) {
						stacksTag.put(String.valueOf(position), stackTag);
					}
				}
			}
		}

		inventoryTag.put("stacks", stacksTag);

		tag.put("inventory", inventoryTag);

		return tag;
	}

	/**
	 * Read inventory contents from a CompoundNBT with support for ItemStacks greater than 64 in size.
	 */
	public static <T extends BaseInventory> T read(CompoundNBT tag) {
		if (tag == null) return null;

		if (!tag.contains("inventory")) {
			Spinnery.LOGGER.log(Level.ERROR, "[Spinnery] Inventory contents failed to be read: " + CompoundNBT.class.getName() + " does not contain 'inventory' subtag!");
			return null;
		} else {
			INBT rawTag = tag.get("inventory");

			if (!(rawTag instanceof CompoundNBT)) {
				Spinnery.LOGGER.log(Level.ERROR, "[Spinnery] Inventory contents failed to be read: " + rawTag.getClass().getName() + " is not instance of " + CompoundNBT.class.getName() + "!");
				return null;
			} else {
				CompoundNBT compoundTag = (CompoundNBT) rawTag;

				if (!compoundTag.contains("size")) {
					Spinnery.LOGGER.log(Level.ERROR, "[Spinnery] Inventory contents failed to be read: " + CompoundNBT.class.getName() + " does not contain 'size' value!");
					return null;
				} else {
					int size = compoundTag.getInt("size");

					if (size == 0)
						Spinnery.LOGGER.log(Level.WARN, "[Spinnery] Inventory contents size successfully read, but with size of zero. This may indicate a non-integer 'size' value!");

					if (!compoundTag.contains("stacks")) {
						Spinnery.LOGGER.log(Level.ERROR, "[Spinnery] Inventory contents failed to be read: " + CompoundNBT.class.getName() + " does not contain 'stacks' subtag!");
						return null;
					} else {
						INBT rawStacksTag = compoundTag.get("stacks");

						if (!(rawStacksTag instanceof CompoundNBT)) {
							Spinnery.LOGGER.log(Level.ERROR, "[Spinnery] Inventory contents failed to be read: " + rawStacksTag.getClass().getName() + " is not instance of " + CompoundNBT.class.getName() + "!");
							return null;
						} else {
							CompoundNBT stacksTag = (CompoundNBT) rawStacksTag;

							BaseInventory inventory = new BaseInventory(size);

							for (int position = 0; position < size; ++position) {
								if (stacksTag.contains(String.valueOf(position))) {
									INBT rawStackTag = stacksTag.get(String.valueOf(position));

									if (!(rawStackTag instanceof CompoundNBT)) {
										Spinnery.LOGGER.log(Level.ERROR, "[Spinnery] Inventory stack skipped: stored tag not instance of " + CompoundNBT.class.getName() + "!");
										return null;
									} else {
										CompoundNBT stackTag = (CompoundNBT) rawStackTag;

										ItemStack stack = ItemStack.read(stackTag);

										if (stack == ItemStack.EMPTY) {
											Spinnery.LOGGER.log(Level.WARN, "[Spinnery] Inventory stack skipped: stack was empty!");
										} else {
											inventory.setInventorySlotContents(position, stack);
										}
									}
								}
							}

							return (T) inventory;
						}
					}
				}
			}
		}
	}
}

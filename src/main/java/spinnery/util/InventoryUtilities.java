package spinnery.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundTag;

public class InventoryUtilities {
	/**
	 * @param inventory Inventory CompoundTag will be written from
	 * @return Tag from inventory
	 * @reason Support >64 ItemStack#count
	 */
	public static CompoundTag write(Inventory inventory) {
		CompoundTag tag = new CompoundTag();

		for (int i = 0; i < inventory.getInvSize(); ++i) {
			tag.put(String.valueOf(i), StackUtilities.write(inventory.getInvStack(i)));
		}

		return tag;
	}

	/**
	 * @param tag Tag Inventory will be read from
	 * @return Inventory from tag
	 * @reeason Support >64 ItemStack#count
	 */
	public static Inventory read(Inventory inventory, CompoundTag tag) {
		for (int i = 0; i < inventory.getInvSize(); ++i) {
			inventory.setInvStack(i, StackUtilities.read((CompoundTag) tag.get(String.valueOf(i))));
		}

		return inventory;
	}
}

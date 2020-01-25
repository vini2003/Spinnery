package spinnery.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundTag;
import spinnery.common.BaseInventory;

public class InventoryUtilities {
	/**
	 * @param inventory Inventory CompoundTag will be written from
	 * @return Tag from inventory
	 * @reason Support >64 ItemStack#count
	 */
	public static <T extends BaseInventory> CompoundTag write(T inventory) {
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
	public static <T extends BaseInventory> T read(T inventory, CompoundTag tag) {
		for (int i = 0; i < inventory.getInvSize(); ++i) {
			inventory.setInvStack(i, StackUtilities.read((CompoundTag) tag.get(String.valueOf(i))));
		}

		return inventory;
	}
}

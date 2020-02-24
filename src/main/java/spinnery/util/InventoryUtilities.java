package spinnery.util;

import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundTag;

public class InventoryUtilities {
	/**
	 * Write inventory contents to a compound NBT with support for item stacks greater than 64 in size.
	 * @param inventory Inventory CompoundTag will be written from
	 * @return Tag from inventory
	 */
	public static <T extends Inventory> CompoundTag write(T inventory, CompoundTag tag) {
		for (int i = 0; i < inventory.getInvSize(); ++i) {
			tag.put(String.valueOf(i), StackUtilities.write(inventory.getInvStack(i)));
		}

		return tag;
	}

	/**
	 * Read inventory contents from a compound NBT with support for item stacks greater than 64 in size.
	 * @param tag Tag Inventory will be read from
	 * @return Inventory from tag
	 */
	public static <T extends Inventory> T read(T inventory, CompoundTag tag) {
		for (int i = 0; i < inventory.getInvSize(); ++i) {
			inventory.setInvStack(i, StackUtilities.read((CompoundTag) tag.get(String.valueOf(i))));
		}

		return inventory;
	}
}

package spinnery.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class StackSerializer {
	public static CompoundTag write(ItemStack stack) {
		Identifier identifier = Registry.ITEM.getId(stack.getItem());

		CompoundTag tag = new CompoundTag();

		tag.putString("id", identifier == null ? "minecraft:air" : identifier.toString());
		tag.putInt("Count", stack.getCount());
		if (stack.getTag() != null) {
			tag.put("tag", stack.getTag().copy());
		}

		return tag;
	}

	public static ItemStack read(CompoundTag tag) {
		Item item = Registry.ITEM.get(new Identifier(tag.getString("id")));
		int count = tag.getInt("Count");

		ItemStack stack = new ItemStack(item, count);

		if (tag.contains("tag", 10)) {
			stack.setTag(tag.getCompound("tag"));
			stack.getItem().postProcessTag(tag);
		}

		if (item.isDamageable()) {
			stack.setDamage(stack.getDamage());
		}

		return stack;
	}
}

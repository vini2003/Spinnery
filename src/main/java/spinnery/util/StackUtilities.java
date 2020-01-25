package spinnery.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class StackUtilities {
	/**
	 * @param stack ItemStack CompoundTag will be written from
	 * @return ItemStack from tag
	 * @reason Support >64 ItemStack#count.
	 */
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

	/**
	 * @param tag CompoundTag ItemStack will be read from
	 * @return ItemStack from tag
	 * @reason Support >64 ItemStack#count.
	 */
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

	/**
	 * @param stackA Source ItemStack
	 * @param stackB Destination ItemStack
	 * @param maxA   Max. count of stackA
	 * @param maxB   Max. count of stackB
	 * @return Results
	 * @reason Support merge of stacks with customized maximum count.
	 */
	public static Pair<ItemStack, ItemStack> clamp(ItemStack stackA, ItemStack stackB, int maxA, int maxB) {
		Item itemA = stackA.getItem();
		Item itemB = stackB.getItem();

		if (stackA.isItemEqual(stackB)) {
			int countA = stackA.getCount();
			int countB = stackB.getCount();

			int availableA = maxA - countA;
			int availableB = maxB - countB;

			stackB.increment(Math.min(countA, availableB));
			stackA.setCount(Math.max(countA - availableB, 0));
		} else {
			if (stackA.isEmpty() && !stackB.isEmpty()) {
				int countA = stackA.getCount();
				int availableA = maxA - countA;

				int countB = stackB.getCount();

				stackA = new ItemStack(itemB, Math.min(countB, availableA));
				stackB.decrement(Math.min(countB, availableA));
			} else if (stackB.isEmpty() && !stackA.isEmpty()) {
				int countB = stackB.getCount();
				int availableB = maxB - countB;

				int countA = stackA.getCount();

				stackB = new ItemStack(itemA, Math.min(countA, availableB));
				stackA.decrement(Math.min(countA, availableB));
			}
		}

		return new Pair<>(stackA, stackB);
	}
}

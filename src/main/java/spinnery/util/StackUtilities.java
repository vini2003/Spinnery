package spinnery.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import spinnery.common.BaseContainer;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class StackUtilities {
	/**
	 * Write item stack to compound NBT with support for counts greater than 64.
	 *
	 * @param stack ItemStack CompoundTag will be written from
	 * @return ItemStack from tag
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
	 * Read item stack from compound NBT with support for counts greater than 64.
	 *
	 * @param tag CompoundTag ItemStack will be read from
	 * @return ItemStack from tag
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
	 * Support merging stacks with customized maximum count.
	 * You may be wondering why use Supplier<T>. I ask you,
	 * instead, why not?
	 * In fact, it is the best idea I have had up to this
	 * very day.
	 * What {@link BaseContainer} does,
	 * is chain a {@link MutablePair#apply(Consumer, Consumer)},
	 * right afterwards, which applies the ItemStack to
	 * whichever method the Consumer is linked to. In that
	 * case, a method to set something's ItemStack.
	 * I hope you thoroughly enjoy this addition. If any
	 * WSlot code breaks, it's either my fault, or my fault.
	 * @param supplierA Source ItemStack supplier
	 * @param supplierB Destination ItemStack supplier
	 * @param sA Max. count of stackA supplier
	 * @param sB Max. count of stackB supplier
	 * @return Resulting ItemStacks
	 */
	public static MutablePair<ItemStack, ItemStack> merge(Supplier<ItemStack> supplierA, Supplier<ItemStack> supplierB, Supplier<Integer> sA, Supplier<Integer> sB) {
		return merge(supplierA.get(), supplierB.get(), sA.get(), sB.get());
	}

	/**
	 * Support merging stacks with customized maximum count.
	 * @param stackA Source ItemStack
	 * @param stackB Destination ItemStack
	 * @param maxA   Max. count of stackA
	 * @param maxB   Max. count of stackB
	 * @return Resulting ItemStacks
	 */
	public static MutablePair<ItemStack, ItemStack> merge(ItemStack stackA, ItemStack stackB, int maxA, int maxB) {
		Item itemA = stackA.getItem();
		Item itemB = stackB.getItem();

		if (stackA.isItemEqual(stackB)) {
			int countA = stackA.getCount();
			int countB = stackB.getCount();

			int availableA = Math.max(0, maxA - countA);
			int availableB = Math.max(0, maxB - countB);

			stackB.increment(Math.min(countA, availableB));
			stackA.setCount(Math.max(countA - availableB, 0));
		} else {
			if (stackA.isEmpty() && !stackB.isEmpty()) {
				int countA = stackA.getCount();
				int availableA = maxA - countA;

				int countB = stackB.getCount();

				stackA = new ItemStack(itemB, Math.min(countB, availableA));
				stackA.setTag(stackB.getTag());
				stackB.decrement(Math.min(countB, availableA));
			} else if (stackB.isEmpty() && !stackA.isEmpty()) {
				int countB = stackB.getCount();
				int availableB = maxB - countB;

				int countA = stackA.getCount();

				stackB = new ItemStack(itemA, Math.min(countA, availableB));
				stackB.setTag(stackA.getTag());
				stackA.decrement(Math.min(countA, availableB));
			}
		}

		return MutablePair.of(stackA, stackB);
	}

	public static boolean equal(ItemStack stackA, ItemStack stackB) {
		return ItemStack.areItemsEqual(stackA, stackB);
	}
}

package spinnery.common.utilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import spinnery.common.screenhandler.BaseScreenHandler;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Stacks {
	public static final CompoundTag TAG_EMPTY = new CompoundTag();


	  You may be wondering why use Suppliers. I ask you,
	  In fact, it is the best idea I have had up to this
	  What {@link BaseScreenHandler} does,
	  right afterwards, which applies the ItemStack to
	  case, a method to set something's ItemStack.
	  WSlot code breaks, it's either my fault, or my fault.
	 *
	  @param supplierB Destination ItemStack supplier
	  @param sB        Max. count of stackB supplier

	public static MutablePair<ItemStack, ItemStack> merge(Supplier<ItemStack> supplierA, Supplier<ItemStack> supplierB, Supplier<Integer> sA, Supplier<Integer> sB) {
		return merge(supplierA.get(), supplierB.get(), sA.get(), sB.get());
	}



	  @param stackB Destination ItemStack
	  @param maxB   Max. count of stackB

	public static MutablePair<ItemStack, ItemStack> merge(ItemStack stackA, ItemStack stackB, int maxA, int maxB) {
		if (equalItemAndTag(stackA, stackB)) {
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

				stackA = stackB.copy();
				stackA.setCount(Math.min(countB, availableA));

				stackA.setTag(stackB.getTag());
				stackB.decrement(Math.min(countB, availableA));
			} else if (stackB.isEmpty() && !stackA.isEmpty()) {
				int countB = stackB.getCount();
				int availableB = maxB - countB;

				int countA = stackA.getCount();

				stackB = stackA.copy();
				stackB.setCount(Math.min(countA, availableB));
				stackB.setTag(stackA.getTag());
				stackA.decrement(Math.min(countA, availableB));
			}
		}

		return MutablePair.of(stackA, stackB);
	}



	  @param stackB Stack two.

	public static boolean equalItemAndTag(ItemStack stackA, ItemStack stackB) {
		return ItemStack.areTagsEqual(stackA, stackB) && ItemStack.areItemsEqual(stackA, stackB);
	}
}

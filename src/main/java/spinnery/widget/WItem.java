package spinnery.widget;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import spinnery.client.utilities.Drawings;

public class WItem extends WAbstractWidget {
	protected ItemStack stack = ItemStack.EMPTY;

	public ItemStack getStack() {
		return stack;
	}

	public <W extends WItem> W setStack(ItemStack stack) {
		this.stack = stack;
		return (W) this;
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		Drawings.getItemRenderer().renderInGui(stack, (int) getX(), (int) getY());
	}
}

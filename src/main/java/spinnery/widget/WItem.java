package spinnery.widget;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import spinnery.client.render.BaseRenderer;

public class WItem extends WAbstractWidget {
	private static final int Z_ITEM_OFFSET = 3;

	protected ItemStack stack = ItemStack.EMPTY;

	public ItemStack getStack() {
		return stack;
	}

	public <W extends WItem> W setStack(ItemStack stack) {
		this.stack = stack;
		return (W) this;
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
		if (isHidden()) {
			return;
		}

		BaseRenderer.getAdvancedItemRenderer().renderInGui(matrices, provider, stack, getX(), getY(), getZ() + Z_ITEM_OFFSET);
	}
}

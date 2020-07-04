package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import spinnery.client.render.BaseRenderer;

public class WItem extends WAbstractWidget {
	ItemStack stack = ItemStack.EMPTY;

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

		RenderSystem.translatef(0, 0, getZ() * 400f);
  		matrices.translate(0, 0, getZ() * 400f);

		BaseRenderer.getExposedItemRenderer().renderInGui(matrices, provider, stack, getX(), getY(), getZ());

  		matrices.translate(0, 0, getZ() * -400f);
		RenderSystem.translatef(0, 0, getZ() * -400f);
	}
}

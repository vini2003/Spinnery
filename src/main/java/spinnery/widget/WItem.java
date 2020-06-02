package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.item.ItemStack;
import spinnery.client.BaseRenderer;

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
	public void draw() {
		if (isHidden()) return;

		RenderSystem.translatef(0, 0, getZ());
		BaseRenderer.getItemRenderer().renderGuiItem(stack, (int) getX(), (int) getY());
	}
}

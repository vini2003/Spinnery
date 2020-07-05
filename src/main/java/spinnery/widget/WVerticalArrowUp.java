package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import spinnery.client.render.BaseRenderer;

public class WVerticalArrowUp extends WButton {
	public static final Identifier IMAGE = new Identifier("spinnery", "textures/vertical_arrow_up.png");

	WVerticalScrollableContainer scrollable;

	public WVerticalScrollableContainer getScrollable() {
		return scrollable;
	}

	public <W extends WVerticalArrowUp> W setScrollable(WVerticalScrollableContainer scrollable) {
		this.scrollable = scrollable;
		return (W) this;
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
		if (isHidden()) {
			return;
		}

		if (!isLowered()) {
			BaseRenderer.drawTexturedQuad(matrices, provider, getX(), getY(), getZ(), getWidth(), getHeight(), IMAGE);
		}
	}

	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		if (isWithinBounds(mouseX, mouseY)) {
			if (scrollable.hasSmoothing()) {
				scrollable.kineticScrollDelta += 2.5;
			} else {
				scrollable.scroll(0, 25);
			}
		}

		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void tick() {
		if (isHeld() && System.currentTimeMillis() - isHeldSince() > 500) {
			if (scrollable.hasSmoothing()) {
				scrollable.kineticScrollDelta += 0.4;
			} else {
				scrollable.scroll(0, 0.25);
			}
		}

		super.tick();
	}
}

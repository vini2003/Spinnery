package spinnery.widget.implementation;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class WVerticalArrowUp extends WButton {
	public static final Identifier IMAGE = new Identifier("spinnery", "textures/vertical_arrow_up.png");

	WVerticalList scrollable;

	public WVerticalList getScrollable() {
		return scrollable;
	}

	public <W extends WVerticalArrowUp> W setScrollable(WVerticalList scrollable) {
		this.scrollable = scrollable;
		return (W) this;
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		super.draw(matrices, provider);
	}

	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		if (isWithinBounds(mouseX, mouseY)) {
			if (scrollable.hasSmoothing()) {
				scrollable.setKineticScrollDelta(scrollable.getKineticScrollDelta() + 2.5F);
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
				scrollable.setKineticScrollDelta(scrollable.getKineticScrollDelta() + 0.4F);
			} else {
				scrollable.scroll(0, 0.25);
			}
		}

		super.tick();
	}
}

package spinnery.widget;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class WTooltipItem extends WItem {
	WTooltip tooltip;
	WText tooltipText;

	@Override
	public void onFocusGained() {
		super.onFocusGained();
		updateHidden(false);
	}

	@Override
	public void onFocusReleased() {
		super.onFocusReleased();
		updateHidden(true);
	}

	@Override
	public void onMouseMoved(float mouseX, float mouseY) {
		updateWidgets();
		updateText();
		updatePositions(mouseX, mouseY);
		updateSizes();
	}

	@Override
	public void onMouseScrolled(float mouseX, float mouseY, double deltaY) {
		super.onMouseScrolled(mouseX, mouseY, deltaY);
		updateWidgets();
		updateText();
		updatePositions(mouseX, mouseY);
		updateSizes();
	}

	public void updateWidgets() {
		if (tooltip == null)
			tooltip = getInterface().createChild(WTooltip::new, Position.of(this), Size.of(this)).setHidden(true);
		if (tooltipText == null)
			tooltipText = getInterface().createChild(WText::new, Position.of(tooltip).add(0, 0), Size.of(this)).setHidden(true);
	}

	public void updateText() {
		tooltipText.setText(stack.getName());
	}

	public void updatePositions(float mouseX, float mouseY) {
		tooltip.setPosition(Position.of(mouseX + 12, mouseY - 4));
	}

	public void updateSizes() {
		tooltip.setSize(Size.of(tooltipText.getWidth() - 1, tooltipText.getHeight() - 1));
	}

	public void updateHidden(boolean hidden) {
		tooltip.setHidden(hidden);
		tooltipText.setHidden(hidden);
	}

	@Override
	public void tick() {
		updateWidgets();
		updateText();
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden() || tooltip == null || tooltipText == null) return;

		tooltip.draw(matrices, provider);
		tooltipText.draw(matrices, provider);

		super.draw(matrices, provider);
	}
}

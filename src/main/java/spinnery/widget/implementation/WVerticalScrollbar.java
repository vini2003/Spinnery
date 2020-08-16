package spinnery.widget.implementation;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.client.utilities.Drawings;
import spinnery.common.utilities.Positions;
import spinnery.common.utilities.miscellaneous.Color;
import spinnery.widget.declaration.scroll.WVerticalScrolled;

public class WVerticalScrollbar extends WAbstractWidget {
	protected WVerticalScrolled scrollable;
	protected float clickMouseY;
	protected boolean dragging = false;
	protected boolean hasArrows = true;

	public WVerticalScrollbar setScrollable(WVerticalScrolled scrollable) {
		this.scrollable = scrollable;
		return this;
	}

	public WVerticalScrolled getScrollable() {
		return scrollable;
	}

	public boolean hasArrows() {
		return hasArrows;
	}

	public void setHasArrows(boolean hasArrows) {
		this.hasArrows = hasArrows;
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		Drawings.drawBeveledPanel(matrices, provider, getX(), getY(), getWidth(), getHeight(), getStyle().asColor("scroll_line.top_left"), getStyle().asColor("scroll_line.background"), getStyle().asColor("scroll_line.bottom_right"));

		Color scrollerColor = getStyle().asColor("scroller.background_default");

		if (Positions.mouseX > getX() + 1 && Positions.mouseX < getWideX() - 1
				&& Positions.mouseY > getScrollerY() + 1 && Positions.mouseY < getScrollerY() + getScrollerHeight() - 1 && !isHeld()) {
			scrollerColor = getStyle().asColor("scroller.background_hovered");
		} else if (isHeld()) {
			scrollerColor = getStyle().asColor("scroller.background_held");
		}

		Drawings.drawBeveledPanel(matrices, provider, getX() + 1, getScrollerY() + 1, getWidth() - 2, Math.min(getHighY() - getScrollerY(), getScrollerHeight()) - 2, getStyle().asColor("scroller.top_left"), scrollerColor, getStyle().asColor("scroller.bottom_right"));
	}

	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		if (mouseButton == 0) {
			if (isWithinBounds(mouseX, mouseY)) {
				if (mouseY >= getScrollerY() && mouseY <= getScrollerY() + getScrollerHeight()) {
					dragging = true;
					clickMouseY = mouseY - getScrollerY();
				} else {
					dragging = false;

					if (mouseY > getScrollerY()) {
						if (((WVerticalList) scrollable).hasSmoothing()) {
							((WVerticalList) scrollable).setKineticScrollDelta((float) (((WVerticalList) scrollable).getKineticScrollDelta() - 3.5));
						} else {
							scrollable.scroll(0, -50);
						}
					} else {
						if (((WVerticalList) scrollable).hasSmoothing()) {
							((WVerticalList) scrollable).setKineticScrollDelta((float) (((WVerticalList) scrollable).getKineticScrollDelta() + 3.5));
						} else {
							scrollable.scroll(0, +50);
						}
					}
				}
			} else {
				dragging = false;
			}
		}

		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	public float getScrollerY() {
		float outerHeight = scrollable.getVisibleHeight();
		float innerHeight = scrollable.getUnderlyingHeight();
		float topOffset = scrollable.getStartOffsetY();
		float percentToEnd = topOffset / (innerHeight - outerHeight);
		float maximumOffset = getHeight() - getScrollerHeight();
		return getY() + (maximumOffset * percentToEnd);
	}

	public float getScrollerHeight() {
		float outerHeight = getHeight();
		float innerHeight = scrollable.getUnderlyingHeight();
		float calculated = (outerHeight * (outerHeight / Math.max(innerHeight, outerHeight)));
		return Math.max(calculated, 4);
	}

	@Override
	public void onMouseDragged(float mouseX, float mouseY, int mouseButton, double deltaX, double deltaY) {
		if (mouseButton == 0) {
			if (dragging) {
				double scrollerOffsetY = getScrollerY() + clickMouseY - mouseY;

				scrollable.scroll(0, scrollerOffsetY);
			}
		}

		super.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
	}
}

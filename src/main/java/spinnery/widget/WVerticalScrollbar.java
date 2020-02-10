package spinnery.widget;

import spinnery.client.BaseRenderer;
import spinnery.widget.api.WVerticalScrollable;

public class WVerticalScrollbar extends WAbstractWidget {
	protected WVerticalScrollable scrollable;
	protected double clickMouseY;
	protected boolean dragging = false;

	public WVerticalScrollbar scrollable(WVerticalScrollable scrollable) {
		this.scrollable = scrollable;
		return this;
	}

	@Override
	public void draw() {
		if (isHidden()) return;
		BaseRenderer.drawBeveledPanel(getX(), getY(), getZ(), getWidth(), getHeight(), getStyle().asColor("scroll_line.top_left"), getStyle().asColor("scroll_line.background"), getStyle().asColor("scroll_line.bottom_right"));
		drawScroller();
	}

	public void drawScroller() {
		BaseRenderer.drawBeveledPanel(getX(), getScrollerY(), getZ(), getWidth(), getScrollerHeight(), getStyle().asColor("scroller.top_left"), getStyle().asColor("scroller.background"), getStyle().asColor("scroller.bottom_right"));
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0) {
			if (isWithinBounds(mouseX, mouseY)) {
				if (mouseY >= getScrollerY() && mouseY <= getScrollerY() + getScrollerHeight()) {
					dragging = true;
					clickMouseY = mouseY - getScrollerY();
				} else {
					dragging = false;
					if (mouseY > getScrollerY()) {
						scrollable.scroll(0, -50);
					} else {
						scrollable.scroll(0, 50);
					}
				}
			} else {
				dragging = false;
			}
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	public int getScrollerY() {
		double outerHeight = getHeight();
		double innerHeight = scrollable.getUnderlyingHeight();
		double topOffset = scrollable.getStartOffsetY();
		double percentToEnd = topOffset / (innerHeight - outerHeight);
		double maximumOffset = getHeight() - getScrollerHeight();
		return getY() + (int) (maximumOffset * percentToEnd);
	}

	public int getScrollerHeight() {
		double outerHeight = getHeight();
		double innerHeight = scrollable.getUnderlyingHeight();
		int calculated = (int) (outerHeight * (outerHeight / Math.max(innerHeight, outerHeight)));
		return Math.max(calculated, 4);
	}

	@Override
	public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
		if (mouseButton == 0) {
			if (dragging) {
				double scrollerOffsetY = getScrollerY() + clickMouseY - mouseY;
				((WVerticalScrollableContainer) scrollable).scrollKineticDelta += -deltaY;
				scrollable.scroll(0, scrollerOffsetY);
			}
		}
		super.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
	}

	public WVerticalScrollable getScrollableParent() {
		return scrollable;
	}

	public <W extends WVerticalScrollbar> W setScrollableParent(WVerticalScrollable parent) {
		this.scrollable = parent;
		return (W) this;
	}
}

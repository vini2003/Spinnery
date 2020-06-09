package spinnery.widget;

import spinnery.client.render.BaseRenderer;
import spinnery.widget.api.WHorizontalScrollable;

@Deprecated // Will be fixed soon!
public class WHorizontalScrollbar extends WAbstractWidget {
	protected WHorizontalScrollable scrollable;
	protected double clickMouseX;
	protected boolean dragging = false;

	public WHorizontalScrollbar scrollable(WHorizontalScrollable scrollable) {
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
		BaseRenderer.drawBeveledPanel(getScrollerX(), getY(), getZ(), getScrollerWidth(), getHeight(), getStyle().asColor("scroller.top_left"), getStyle().asColor("scroller.background"), getStyle().asColor("scroller.bottom_right"));
	}

	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		if (mouseButton == 0) {
			if (isWithinBounds(mouseX, mouseY)) {
				if (mouseX >= getScrollerX() && mouseX <= getScrollerX() + getScrollerWidth()) {
					dragging = true;
					clickMouseX = mouseX - getScrollerX();
				} else {
					dragging = false;
					if (mouseX > getScrollerX()) {
						scrollable.scroll(-50, 0);
					} else {
						scrollable.scroll(50, 0);
					}
				}
			} else {
				dragging = false;
			}
		}
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	public float getScrollerX() {
		float outerWidth = getWidth();
		float innerWidth = scrollable.getUnderlyingWidth();
		float leftOffset = scrollable.getStartOffsetX();
		float percentToEnd = leftOffset / (innerWidth - outerWidth);
		float maximumOffset = getWidth() - getScrollerWidth();
		return getX() + (maximumOffset * percentToEnd);
	}

	public float getScrollerWidth() {
		float outerWidth = getWidth();
		float innerWidth = scrollable.getUnderlyingWidth();
		float calculated = (outerWidth * (outerWidth / (Math.max(innerWidth, outerWidth))));
		return Math.max(calculated, 4);
	}

	@Override
	public void onMouseDragged(float mouseX, float mouseY, int mouseButton, double deltaX, double deltaY) {
		if (mouseButton == 0) {
			if (dragging) {
				double scrollerOffsetX = getScrollerX() + clickMouseX - mouseX;
				((WHorizontalScrollableContainer) scrollable).scrollKineticDelta += -deltaX;
				scrollable.scroll(scrollerOffsetX, 0);
			}
		}
		super.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
	}
}

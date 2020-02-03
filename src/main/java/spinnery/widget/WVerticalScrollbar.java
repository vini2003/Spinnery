package spinnery.widget;

import spinnery.client.BaseRenderer;
import spinnery.widget.api.WVerticalScrollable;

public class WVerticalScrollbar extends WWidget {
    protected WVerticalScrollable parent;
    protected double clickMouseY;
    protected boolean dragging = false;

    public WVerticalScrollbar parent(WVerticalScrollable parent) {
        this.parent = parent;
        return this;
    }

    public WVerticalScrollbar build() {
        return this;
    }

    public int getScrollerHeight() {
        double outerHeight = getHeight();
        double innerHeight = parent.getInnerSize().getY();
        return (int) (outerHeight * (outerHeight / Math.max(innerHeight, outerHeight)));
    }

    public int getScrollerY() {
        double outerHeight = getHeight();
        double innerHeight = parent.getInnerSize().getY();
        double topOffset = parent.getStartOffsetY();
        double percentToEnd = topOffset / (innerHeight - outerHeight);
        double maximumOffset = getHeight() - getScrollerHeight();
        return getY() + (int) (maximumOffset * percentToEnd);
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
                        parent.scroll(0, -50);
                    } else {
                        parent.scroll(0, 50);
                    }
                }
            } else {
                dragging = false;
            }
        }
        super.onMouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
        if (mouseButton == 0) {
            if (dragging) {
                double scrollerOffsetY = getScrollerY() + clickMouseY - mouseY;
                ((WVerticalScrollableContainer) parent).scrollKineticDelta += -deltaY;
                parent.scroll(0, scrollerOffsetY);
            }
        }
        super.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
    }

    @Override
    public void draw() {
        if (isHidden()) return;
        BaseRenderer.drawBeveledPanel(getX(), getY(), getZ(), getWidth(), getHeight(), getStyle().asColor("scroll_line.top_left"), getStyle().asColor("scroll_line.background"), getStyle().asColor("scroll_line.bottom_right"));
        drawScroller();
    }
}

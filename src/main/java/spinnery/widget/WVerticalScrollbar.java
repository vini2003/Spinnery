package spinnery.widget;

import spinnery.client.BaseRenderer;

public class WVerticalScrollbar extends WWidget implements WClient {
    protected WVerticalScrollable parent;
    protected double clickMouseY;
    protected boolean dragging = false;

    public WVerticalScrollbar(WInterface linkedInterface, WVerticalScrollable parent) {
        setInterface(linkedInterface);
        this.parent = parent;
    }

    public int getScrollerHeight() {
        double outerHeight = getHeight();
        double innerHeight = parent.getInnerSize().getY();
        return (int) (outerHeight * (outerHeight / innerHeight));
    }

    public int getScrollerY() {
        double outerHeight = getHeight();
        double innerHeight = parent.getInnerSize().getY();
        double topOffset = parent.getTopOffset();
        double percentToEnd = topOffset / (innerHeight - outerHeight);
        double maximumOffset = getHeight() - getScrollerHeight();
        return getY() + (int) (maximumOffset * percentToEnd);
    }

    public void drawScroller() {
        BaseRenderer.drawRectangle(getX(), getScrollerY(), getZ(), getWidth(), getScrollerHeight(), WColor.of("0x808080"));
        BaseRenderer.drawRectangle(getX(), getScrollerY(), getZ(), getWidth() - 1, getScrollerHeight() - 1, WColor.of("0xC0C0C0"));
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
                parent.scroll(0, scrollerOffsetY);
            }
        }
        super.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
    }

    @Override
    public void draw() {
        if (isHidden()) return;
        BaseRenderer.drawRectangle(getX(), getY(), getZ(), getWidth(), getHeight(), WColor.of("0x000000"));
        drawScroller();
    }
}

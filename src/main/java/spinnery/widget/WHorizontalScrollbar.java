package spinnery.widget;

import spinnery.client.BaseRenderer;

public class WHorizontalScrollbar extends WWidget implements WClient {
    protected WHorizontalScrollable parent;
    protected double clickMouseX;
    protected boolean dragging = false;

    public WHorizontalScrollbar(WInterface linkedInterface, WHorizontalScrollable parent) {
        setInterface(linkedInterface);
        this.parent = parent;
    }

    public int getScrollerWidth() {
        double outerWidth = getWidth();
        double innerWidth = parent.getInnerWidth();
        return (int) (outerWidth * (outerWidth / (Math.max(innerWidth, outerWidth))));
    }

    public int getScrollerX() {
        double outerWidth = getWidth();
        double innerWidth = parent.getInnerWidth();
        double leftOffset = parent.getStartOffsetX();
        double percentToEnd = leftOffset / (innerWidth - outerWidth);
        double maximumOffset = getWidth() - getScrollerWidth();
        return getX() + (int) (maximumOffset * percentToEnd);
    }

    public void drawScroller() {
        BaseRenderer.drawBeveledPanel(getScrollerX(), getY(), getZ(), getScrollerWidth(), getHeight(), getStyle().asColor("scroller.top_left"), getStyle().asColor("scroller.background"), getStyle().asColor("scroller.bottom_right"));
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            if (isWithinBounds(mouseX, mouseY)) {
                if (mouseX >= getScrollerX() && mouseX <= getScrollerX() + getScrollerWidth()) {
                    dragging = true;
                    clickMouseX = mouseX - getScrollerX();
                } else {
                    dragging = false;
                    if (mouseX > getScrollerX()) {
                        parent.scroll(-50, 0);
                    } else {
                        parent.scroll(50, 0);
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
                double scrollerOffsetX = getScrollerX() + clickMouseX - mouseX;
                ((WHorizontalScrollableContainer) parent).scrollKineticDelta += -deltaX;
                parent.scroll(scrollerOffsetX, 0);
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

package spinnery.widget;

import spinnery.client.BaseRenderer;
import spinnery.widget.api.WHorizontalScrollable;

public class WHorizontalScrollbar extends WWidget {
    protected WHorizontalScrollable scrollable;
    protected double clickMouseX;
    protected boolean dragging = false;

    public WHorizontalScrollbar scrollable(WHorizontalScrollable scrollable) {
        this.scrollable = scrollable;
        return this;
    }

    public int getScrollerWidth() {
        double outerWidth = getWidth();
        double innerWidth = scrollable.getInnerWidth();
        int calculated = (int) (outerWidth * (outerWidth / (Math.max(innerWidth, outerWidth))));
        return Math.max(calculated, 4);
    }

    public int getScrollerX() {
        double outerWidth = getWidth();
        double innerWidth = scrollable.getInnerWidth();
        double leftOffset = scrollable.getStartOffsetX();
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

    @Override
    public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
        if (mouseButton == 0) {
            if (dragging) {
                double scrollerOffsetX = getScrollerX() + clickMouseX - mouseX;
                ((WHorizontalScrollableContainer) scrollable).scrollKineticDelta += -deltaX;
                scrollable.scroll(scrollerOffsetX, 0);
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

package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;

import java.util.Map;

public class WHorizontalScrollbar extends WWidget implements WClient {
    protected WHorizontalScrollable parent;
    protected double clickMouseX;
    protected boolean dragging = false;

    protected static final int SCROLLER_TOP_LEFT = 0;
    protected static final int SCROLLER_BOTTOM_RIGHT = 1;
    protected static final int SCROLLER_BACKGROUND = 2;
    protected static final int SCROLL_LINE_TOP_LEFT = 3;
    protected static final int SCROLL_LINE_BOTTOM_RIGHT = 4;
    protected static final int SCROLL_LINE_BACKGROUND = 5;

    public WHorizontalScrollbar(WInterface linkedInterface, WHorizontalScrollable parent) {
        setInterface(linkedInterface);
        this.parent = parent;
    }

    public int getScrollerWidth() {
        double outerWidth = getWidth();
        double innerWidth = parent.getInnerWidth();
        return (int) (outerWidth * (outerWidth / innerWidth));
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
        BaseRenderer.drawBeveledPanel(getScrollerX(), getY(), getZ(), getScrollerWidth(), getHeight(), getResourceAsColor(SCROLLER_TOP_LEFT), getResourceAsColor(SCROLLER_BACKGROUND), getResourceAsColor(SCROLLER_BOTTOM_RIGHT));
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
        BaseRenderer.drawBeveledPanel(getX(), getY(), getZ(), getWidth(), getHeight(), getResourceAsColor(SCROLL_LINE_TOP_LEFT), getResourceAsColor(SCROLL_LINE_BACKGROUND), getResourceAsColor(SCROLLER_BOTTOM_RIGHT));
        drawScroller();
    }

    @Environment(EnvType.CLIENT)
    public static WWidget.Theme of(Map<String, String> rawTheme) {
        WWidget.Theme theme = new WWidget.Theme();
        theme.put(SCROLLER_TOP_LEFT, WColor.of(rawTheme.get("scroller_top_left")));
        theme.put(SCROLLER_BOTTOM_RIGHT, WColor.of(rawTheme.get("scroller_bottom_right")));
        theme.put(SCROLLER_BACKGROUND, WColor.of(rawTheme.get("scroller_background")));
        theme.put(SCROLL_LINE_TOP_LEFT, WColor.of(rawTheme.get("scroll_line_top_left")));
        theme.put(SCROLL_LINE_BOTTOM_RIGHT, WColor.of(rawTheme.get("scroll_line_bottom_right")));
        theme.put(SCROLL_LINE_BACKGROUND, WColor.of(rawTheme.get("scroll_line_background")));
        return theme;
    }
}

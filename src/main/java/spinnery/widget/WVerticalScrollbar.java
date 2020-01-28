package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import spinnery.client.BaseRenderer;

import java.util.Map;

public class WVerticalScrollbar extends WWidget implements WClient {
    protected WVerticalScrollable parent;
    protected double clickMouseY;
    protected boolean dragging = false;

    protected static final int SCROLLER_TOP_LEFT = 0;
    protected static final int SCROLLER_BOTTOM_RIGHT = 1;
    protected static final int SCROLLER_BACKGROUND = 2;
    protected static final int SCROLL_LINE_TOP_LEFT = 3;
    protected static final int SCROLL_LINE_BOTTOM_RIGHT = 4;
    protected static final int SCROLL_LINE_BACKGROUND = 5;

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
        double topOffset = parent.getStartOffsetY();
        double percentToEnd = topOffset / (innerHeight - outerHeight);
        double maximumOffset = getHeight() - getScrollerHeight();
        return getY() + (int) (maximumOffset * percentToEnd);
    }

    public void drawScroller() {
        BaseRenderer.drawBeveledPanel(getX(), getScrollerY(), getZ(), getWidth(), getScrollerHeight(), getResourceAsColor(SCROLLER_TOP_LEFT), getResourceAsColor(SCROLLER_BACKGROUND), getResourceAsColor(SCROLLER_BOTTOM_RIGHT));
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

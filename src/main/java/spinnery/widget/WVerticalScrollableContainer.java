package spinnery.widget;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;
import spinnery.widget.api.WModifiableCollection;
import spinnery.widget.api.WPosition;
import spinnery.widget.api.WSize;
import spinnery.widget.api.WVerticalScrollable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class WVerticalScrollableContainer extends WAbstractWidget implements WModifiableCollection, WVerticalScrollable {
    public List<List<WAbstractWidget>> listWidgets = new ArrayList<>();

    protected WVerticalScrollbar scrollbar;

    protected float scrollKineticDelta = 0;

    public WVerticalScrollableContainer() {
        scrollbar = WWidgetFactory.buildDetached(WVerticalScrollbar.class).scrollable(this).setParent(this);
    }

    @Override
    public void onLayoutChange() {
        scrollToStart();
        updateHidden();
        updateScrollbar();
    }

    public void updateScrollbar() {
        int scrollBarWidth = 6;
        int scrollBarHeight = getHeight();
        int scrollBarOffsetX = getWidth() - scrollBarWidth;
        int scrollBarOffsetY = 0;
        scrollbar.setPosition(WPosition.of(this, scrollBarOffsetX, scrollBarOffsetY, 0));
        scrollbar.setSize(WSize.of(scrollBarWidth, scrollBarHeight));
    }

    @Override
    public void onMouseScrolled(int mouseX, int mouseY, double deltaY) {
        if (isWithinBounds(mouseX, mouseY)) {
            scrollKineticDelta += deltaY;
            scroll(0, deltaY);
            super.onMouseScrolled(mouseX, mouseY, deltaY);
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        scrollbar.onMouseClicked(mouseX, mouseY, mouseButton);
        super.onMouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
        scrollbar.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
        super.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
    }

    @Override
    public void scroll(double deltaX, double deltaY) {
        if (getListWidgets().size() == 0) {
            return;
        }

        boolean hitTop = getListWidgets().get(0).stream().anyMatch(widget ->
                widget.getY() + deltaY > getStartAnchorY()
        );

        boolean hitBottom = getListWidgets().get(getListWidgets().size() - 1).stream().anyMatch(widget ->
                widget.getY() + widget.getHeight() + deltaY <= getStartAnchorY() + getVisibleHeight()
        );

        if (hitBottom && scrollKineticDelta < -2.5) {
            scrollKineticDelta = 0;
        }

        if (hitTop && scrollKineticDelta > 2.5) {
            scrollKineticDelta = 0;
        }

        if (deltaY > 0 && hitTop) {
            scrollToStart();
        } else if (deltaY < 0 && hitBottom) {
            scrollToEnd();
        } else {
            for (WAbstractWidget widget : getWidgets()) {
                widget.setY(widget.getY() + (int) deltaY);
            }
        }

        updateHidden();
    }

    public List<List<WAbstractWidget>> getListWidgets() {
        return listWidgets;
    }

    @Override
    public Set<WAbstractWidget> getWidgets() {
        Set<WAbstractWidget> widgets = new LinkedHashSet<>();
        for (List<WAbstractWidget> widgetA : getListWidgets()) {
            widgets.addAll(widgetA);
        }
        return widgets;
    }

    public boolean hasScrollbar() {
        return !scrollbar.isHidden();
    }

    public <W extends WVerticalScrollableContainer> W setScrollbarVisible(boolean visible) {
        scrollbar.setHidden(!visible);
        return (W) this;
    }

    @Override
    public int getStartAnchorY() {
        return getY();
    }

    @Override
    public int getEndAnchorY() {
        if (getVisibleHeight() > getInnerHeight()) return getStartAnchorY();
        return getStartAnchorY() - (getInnerHeight() - getVisibleHeight());
    }

    @Override
    public WSize getVisibleSize() {
        return WSize.of(getWidth() - (!scrollbar.isHidden() ? scrollbar.getWidth() : 0), getHeight());
    }

    @Override
    public WSize getInnerSize() {
        List<List<WAbstractWidget>> widgetLists = getListWidgets();

        // Topmost widget (lower Y)
        int topmostY = getStartAnchorY();
        for (WAbstractWidget widget : widgetLists.get(0)) {
            if (widget.getY() < topmostY) {
                topmostY = widget.getY();
            }
        }

        // Bottommost widget (higher Y)
        int bottommostY = topmostY;
        for (WAbstractWidget widget : widgetLists.get(widgetLists.size() - 1)) {
            if (widget.getY() + widget.getHeight() > bottommostY) {
                bottommostY = widget.getY() + widget.getHeight();
            }
        }

        return WSize.of(getVisibleWidth(), bottommostY - topmostY);
    }

    @Override
    public int getStartOffsetY() {
        int topY = getStartAnchorY();
        int topmostY = topY;
        for (WAbstractWidget widget : getListWidgets().get(0)) {
            if (widget.getY() < topmostY) {
                topmostY = widget.getY();
            }
        }
        return topY - topmostY;
    }

    @Override
    public void align() {
        super.align();
        scrollToStart();
    }

    @Override
    public boolean updateFocus(int mouseX, int mouseY) {
        setFocus(isWithinBounds(mouseX, mouseY) && getWidgets().stream().noneMatch((WAbstractWidget::getFocus)));
        return getFocus();
    }

    @Override
    public void center() {
        int oldX = getX();
        int oldY = getY();

        super.center();

        int newX = getX();
        int newY = getY();

        int offsetX = newX - oldX;
        int offsetY = newY - oldY;

        for (WAbstractWidget widget : getWidgets()) {
            widget.setX(widget.getX() + offsetX);
            widget.setY(widget.getY() + offsetY);
        }
    }

    @Override
    public void draw() {
        if (isHidden()) {
            return;
        }

        int x = getX();
        int y = getY();

        int sX = getWidth();
        int sY = getHeight();

        int rawHeight = MinecraftClient.getInstance().getWindow().getHeight();
        double scale = MinecraftClient.getInstance().getWindow().getScaleFactor();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        GL11.glScissor((int) (x * scale), (int) (rawHeight - (y * scale + sY * scale)), (int) (sX * scale), (int) (sY * scale));

        for (List<WAbstractWidget> widgetB : getListWidgets()) {
            for (WAbstractWidget widgetC : widgetB) {
                widgetC.draw();
            }
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        scrollbar.draw();
    }

    public void scrollToStart() {
        int y = getStartAnchorY();

        for (List<WAbstractWidget> widgetA : getListWidgets()) {
            int x = getX();
            for (WAbstractWidget widgetB : widgetA) {
                widgetB.setX(x);
                widgetB.setY(y);
                x += widgetB.getWidth() + 2;
            }
            y += widgetA.get(0).getHeight() + 2;
        }
    }

    public void scrollToEnd() {
        int y = getEndAnchorY();

        for (List<WAbstractWidget> widgetA : getListWidgets()) {
            int x = getX();
            for (WAbstractWidget widgetB : widgetA) {
                widgetB.setX(x);
                widgetB.setY(y);
                x += widgetB.getWidth() + 2;
            }
            y += widgetA.get(0).getHeight() + 2;
        }
    }

    public void updateHidden() {
        for (List<WAbstractWidget> widgetList : getListWidgets()) {
            for (WAbstractWidget w : widgetList) {
                boolean startContained = isWithinBounds(w.getX(), w.getY(), 1)
                        || isWithinBounds(w.getX() + w.getWidth(), w.getY() + w.getHeight(), 1);
                w.setHidden(!startContained);
            }
        }
    }

    @Override
    public void add(WAbstractWidget... widgetArray) {
        getListWidgets().add(Arrays.asList(widgetArray));
        scrollToStart();
        updateHidden();
    }

    @Override
    public void remove(WAbstractWidget... widgetArray) {
        getListWidgets().remove(Arrays.asList(widgetArray));
        scrollToStart();
        updateHidden();
    }

    @Override
    public boolean contains(WAbstractWidget... widgetArray) {
        for (List<WAbstractWidget> widgetList : getListWidgets()) {
            if (widgetList.containsAll(Arrays.asList(widgetArray))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick() {
        if (scrollKineticDelta > 0.05 || scrollKineticDelta < -0.05) {
            scrollKineticDelta = (float) (scrollKineticDelta / 1.25);
            scroll(0, scrollKineticDelta);
        } else {
            scrollKineticDelta = 0;
        }
    }
}

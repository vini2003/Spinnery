package spinnery.widget;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;
import spinnery.widget.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class WHorizontalScrollableContainer extends WAbstractWidget implements WModifiableCollection, WHorizontalScrollable {
    public List<List<WAbstractWidget>> listWidgets = new ArrayList<>();

    protected WHorizontalScrollbar scrollbar;

    protected float scrollKineticDelta = 0;

    public WHorizontalScrollableContainer() {
        scrollbar = WWidgetFactory.buildDetached(WHorizontalScrollbar.class).scrollable(this).setParent(this);
    }

    @Override
    public void onLayoutChange() {
        scrollToStart();
        updateHidden();
        updateScrollbar();
    }

    public void updateScrollbar() {
        int scrollBarWidth = getVisibleWidth();
        int scrollBarHeight = 6;
        int scrollBarOffsetX = 0;
        int scrollBarOffsetY = getHeight() - scrollBarHeight;
        scrollbar.setPosition(WPosition.of(this, scrollBarOffsetX, scrollBarOffsetY, scrollbar.getPosition().getRelativeZ()));
        scrollbar.setSize(WSize.of(scrollBarWidth, scrollBarHeight));
    }

    @Override
    public void onMouseScrolled(int mouseX, int mouseY, double deltaY) {
        if (isWithinBounds(mouseX, mouseY)) {
            scrollKineticDelta += deltaY;
            scroll(deltaY * 5, 0);
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

        boolean hitLeft = getListWidgets().get(0).stream().anyMatch(widget ->
                widget.getX() + deltaX > getStartAnchorX()
        );

        boolean hitRight = getListWidgets().get(getListWidgets().size() - 1).stream().anyMatch(widget ->
                widget.getX() + widget.getWidth() + deltaX <= getStartAnchorX() + getVisibleWidth()
        );

        if (hitRight && scrollKineticDelta < -2.5) {
            scrollKineticDelta = 0;
        }

        if (hitLeft && scrollKineticDelta > 2.5) {
            scrollKineticDelta = 0;
        }


        if (deltaX > 0 && hitLeft) {
            scrollToStart();
        } else if (deltaX < 0 && hitRight) {
            scrollToEnd();
        } else {
            for (WAbstractWidget widget : getWidgets()) {
                widget.setX(widget.getX() + (int) deltaX);
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

    public <W extends WHorizontalScrollableContainer> W setScrollbarVisible(boolean visible) {
        scrollbar.setHidden(!visible);
        return (W) this;
    }

    @Override
    public int getStartAnchorX() {
        return getX();
    }

    @Override
    public int getEndAnchorX() {
        if (getWidth() > getInnerWidth()) return getStartAnchorX();
        return getStartAnchorX() - (getInnerWidth() - getVisibleWidth());
    }

    @Override
    public WSize getVisibleSize() {
        return WSize.of(getWidth(), getHeight() - (!scrollbar.isHidden() ? scrollbar.getHeight() : 0));
    }

    @Override
    public WSize getInnerSize() {
        List<List<WAbstractWidget>> widgetLists = getListWidgets();

        // Leftmost widget (lower X)
        int leftmostX = getStartAnchorX();
        for (WAbstractWidget widget : widgetLists.get(0)) {
            if (widget.getX() < leftmostX) {
                leftmostX = widget.getX();
            }
        }

        // Bottommost widget (higher Y)
        int rightmostX = leftmostX;
        for (WAbstractWidget widget : widgetLists.get(widgetLists.size() - 1)) {
            if (widget.getX() + widget.getWidth() > rightmostX) {
                rightmostX = widget.getX() + widget.getWidth();
            }
        }

        return WSize.of(rightmostX - leftmostX, getVisibleHeight());
    }

    @Override
    public int getStartOffsetX() {
        int leftX = getStartAnchorX();
        int leftmostX = leftX;
        for (WAbstractWidget widget : getListWidgets().get(0)) {
            if (widget.getX() < leftmostX) {
                leftmostX = widget.getX();
            }
        }
        return leftX - leftmostX;
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
        int x = getStartAnchorX();

        for (List<WAbstractWidget> widgetA : getListWidgets()) {
            int y = getY();
            for (WAbstractWidget widgetB : widgetA) {
                widgetB.setX(x);
                widgetB.setY(y + 4);
                y += widgetB.getHeight() + 2;
            }
            x += widgetA.get(0).getWidth() + 2;
        }
    }

    public void scrollToEnd() {
        int x = getEndAnchorX();

        for (List<WAbstractWidget> widgetA : getListWidgets()) {
            int y = getY();
            for (WAbstractWidget widgetB : widgetA) {
                widgetB.setX(x);
                widgetB.setY(y + 4);
                y += widgetB.getHeight() + 2;
            }
            x += widgetA.get(0).getWidth() + 2;
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
        onLayoutChange();
    }

    @Override
    public void remove(WAbstractWidget... widgetArray) {
        getListWidgets().remove(Arrays.asList(widgetArray));
        onLayoutChange();
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
            scrollKineticDelta = (float) (scrollKineticDelta / 1.10);
            scroll(scrollKineticDelta, 0);
        } else {
            scrollKineticDelta = 0;
        }
    }
}

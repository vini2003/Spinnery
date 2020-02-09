package spinnery.widget;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;
import spinnery.widget.api.*;

import java.util.*;

@SuppressWarnings("unchecked")
public class WHorizontalScrollableContainer extends WAbstractWidget implements WModifiableCollection, WHorizontalScrollable,
        WDelegatedEventListener {
    public Set<WAbstractWidget> widgets = new HashSet<>();

    protected WHorizontalScrollbar scrollbar;

    protected int xOffset = 0;
    protected float scrollKineticDelta = 0;
    protected int rightSpace = 0;

    public WHorizontalScrollableContainer() {
        scrollbar = WWidgetFactory.buildDetached(WHorizontalScrollbar.class).scrollable(this).setParent(this);
    }

    public int getRightSpace() {
        return rightSpace;
    }

    public <W extends WHorizontalScrollableContainer> W setRightSpace(int rightSpace) {
        this.rightSpace = rightSpace;
        return (W) this;
    }

    @Override
    public void onLayoutChange() {
        scrollToStart();
        updateScrollbar();
    }

    public void updateScrollbar() {
        int scrollBarWidth = getVisibleWidth();
        int scrollBarHeight = 6;
        int scrollBarOffsetX = 0;
        int scrollBarOffsetY = getHeight() - scrollBarHeight;
        scrollbar.setPosition(Position.of(this, scrollBarOffsetX, scrollBarOffsetY, scrollbar.getPosition().getRelativeZ()));
        scrollbar.setSize(Size.of(scrollBarWidth, scrollBarHeight));
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
    public void scroll(double deltaX, double deltaY) {
        if (getWidgets().size() == 0) {
            return;
        }

        boolean hitLeft = xOffset - deltaX < 0;
        boolean hitRight = xOffset - deltaX > getMaxOffsetX();

        if (hitRight || hitLeft) {
            scrollKineticDelta = 0;
        }

        if (deltaX > 0 && hitLeft) {
            scrollToStart();
        } else if (deltaX < 0 && hitRight) {
            scrollToEnd();
        } else {
            xOffset -= deltaX;
        }

        updateChildren();
    }

    @Override
    public Set<WAbstractWidget> getWidgets() {
        return widgets;
    }

    @Override
    public Collection<? extends WEventListener> getEventDelegates() {
        Set<WAbstractWidget> delegates = new HashSet<>(widgets);
        delegates.add(scrollbar);
        return ImmutableSet.copyOf(delegates);
    }

    public boolean getScrollbarVisible() {
        return !scrollbar.isHidden();
    }

    public <W extends WHorizontalScrollableContainer> W setScrollbarVisible(boolean visible) {
        scrollbar.setHidden(!visible);
        return (W) this;
    }

    protected int getMaxX() {
        int max = widgets.stream().mapToInt(w -> w.getPosition().getRelativeX() + w.getWidth()).max().orElse(0);
        if (max == 0) return getStartAnchorX();
        return getStartAnchorX() + max - getVisibleWidth() + rightSpace;
    }

    @Override
    public int getStartAnchorX() {
        return getX();
    }

    @Override
    public int getEndAnchorX() {
        if (getWidth() > getUnderlyingWidth()) return getStartAnchorX();
        return getStartAnchorX() - (getUnderlyingWidth() - getVisibleWidth());
    }

    @Override
    public Size getVisibleSize() {
        return Size.of(getWidth(), getHeight() - (!scrollbar.isHidden() ? scrollbar.getHeight() : 0));
    }

    @Override
    public Size getUnderlyingSize() {
        Set<WAbstractWidget> widgets = getWidgets();

        int leftmostX = getStartAnchorX();
        int rightmostX = leftmostX;
        for (WAbstractWidget widget : widgets) {
            if (widget.getX() < leftmostX) {
                leftmostX = widget.getX();
            }
            if (widget.getX() + widget.getWidth() > rightmostX) {
                rightmostX = widget.getX() + widget.getWidth();
            }
        }

        return Size.of(rightmostX - leftmostX, getVisibleHeight());
    }

    @Override
    public int getStartOffsetX() {
        return xOffset;
    }

    public int getMaxOffsetX() {
        return getMaxX() - getStartAnchorX();
    }

    @Override
    public boolean updateFocus(int mouseX, int mouseY) {
        setFocus(isWithinBounds(mouseX, mouseY) && getWidgets().stream().noneMatch((WAbstractWidget::isFocused)));
        return isFocused();
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

        for (WAbstractWidget widget : widgets) {
            widget.draw();
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        scrollbar.draw();
    }

    public void scrollToStart() {
        xOffset = 0;
        updateChildren();
    }

    public void scrollToEnd() {
        xOffset = getMaxOffsetX();
        updateChildren();
    }

    public void updateChildren() {
        for (WAbstractWidget w : getWidgets()) {
            w.getPosition().setOffsetX(-xOffset);
            boolean startContained = isWithinBounds(w.getX(), w.getY(), 1)
                    || isWithinBounds(w.getX() + w.getWidth(), w.getY() + w.getHeight(), 1);
            w.setHidden(!startContained);
        }
    }

    @Override
    public void add(WAbstractWidget... widgetArray) {
        widgets.addAll(Arrays.asList(widgetArray));
        onLayoutChange();
    }

    @Override
    public void remove(WAbstractWidget... widgetArray) {
        widgets.removeAll(Arrays.asList(widgetArray));
        onLayoutChange();
    }

    @Override
    public boolean contains(WAbstractWidget... widgetArray) {
        return widgets.containsAll(Arrays.asList(widgetArray));
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

package spinnery.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WHorizontalScrollableContainer extends WWidget implements WClient, WModifiableCollection, WHorizontalScrollable {
    public List<List<WWidget>> listWidgets = new ArrayList<>();

    protected WHorizontalScrollbar wScrollbar;

    protected float scrollKineticDelta = 0;

    public WHorizontalScrollableContainer(WPosition position, WSize size, WInterface linkedInterface) {
        setInterface(linkedInterface);
        setPosition(position);
        setSize(size);

        wScrollbar = new WHorizontalScrollbar(linkedInterface, this);
        wScrollbar.setHidden(true);
        linkedInterface.add(wScrollbar);
        updateScrollbar();
    }

    public void updateScrollbar() {
        int scrollBarWidth = getVisibleWidth();
        int scrollBarHeight = 6;
        int scrollBarX = getX();
        int scrollBarY = getY() + getHeight() - scrollBarHeight;
        wScrollbar.setPosition(WPosition.of(WType.FREE, scrollBarX, scrollBarY, getZ() + 1));
        wScrollbar.setSize(WSize.of(scrollBarWidth, scrollBarHeight));
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
            for (WWidget widget : getWidgets()) {
                widget.setX(widget.getX() + (int) deltaX);
            }
        }

        updateHidden();
    }

    public List<List<WWidget>> getListWidgets() {
        return listWidgets;
    }

    @Override
    public List<WWidget> getWidgets() {
        List<WWidget> widgets = new ArrayList<>();
        for (List<WWidget> widgetA : getListWidgets()) {
            widgets.addAll(widgetA);
        }
        return widgets;
    }

    @Override
    public List<WWidget> getAllWidgets() {
        List<WWidget> widgets = new ArrayList<>();
        for (List<WWidget> widgetA : getListWidgets()) {
            widgets.addAll(widgetA);
            if (widgetA instanceof WCollection) {
                widgets.addAll(((WCollection) widgetA).getAllWidgets());
            }
        }
        return widgets;
    }

    @Override
    public void setLabel(Text label) {
        super.setLabel(label);
        scrollToStart();
        updateHidden();
        updateScrollbar();
    }

    public boolean hasScrollbar() {
        return !wScrollbar.isHidden();
    }

    public void setScrollbarVisible(boolean visible) {
        wScrollbar.setHidden(!visible);
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
        return WSize.of(getWidth(), getHeight() - (!wScrollbar.isHidden() ? wScrollbar.getHeight() : 0));
    }

    @Override
    public WSize getInnerSize() {
        List<List<WWidget>> widgetLists = getListWidgets();

        // Leftmost widget (lower X)
        int leftmostX = getStartAnchorX();
        for (WWidget widget : widgetLists.get(0)) {
            if (widget.getX() < leftmostX) {
                leftmostX = widget.getX();
            }
        }

        // Bottommost widget (higher Y)
        int rightmostX = leftmostX;
        for (WWidget widget : widgetLists.get(widgetLists.size() - 1)) {
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
        for (WWidget widget : getListWidgets().get(0)) {
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
        setFocus(isWithinBounds(mouseX, mouseY) && getWidgets().stream().noneMatch((WWidget::getFocus)));
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

        for (WWidget widget : getWidgets()) {
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

        for (List<WWidget> widgetB : getListWidgets()) {
            for (WWidget widgetC : widgetB) {
                widgetC.draw();
            }
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        wScrollbar.draw();

        //BaseRenderer.drawRectangle(getX() + 4, getStartAnchorY() - getStartOffset(), 100, getInnerWidth(), getInnerHeight(), WColor.of("0x4000ff00"));
        //BaseRenderer.drawRectangle(getX() + 4, getStartAnchorY(), 101, getVisibleWidth(), getVisibleHeight(), WColor.of("0x80ff0000"));
    }

    public void scrollToStart() {
        int x = getStartAnchorX();

        for (List<WWidget> widgetA : getListWidgets()) {
            int y = getY();
            for (WWidget widgetB : widgetA) {
                widgetB.setX(x);
                widgetB.setY(y);
                y += widgetB.getHeight() + 2;
            }
            x += widgetA.get(0).getWidth() + 2;
        }
    }

    public void scrollToEnd() {
        int x = getEndAnchorX();

        for (List<WWidget> widgetA : getListWidgets()) {
            int y = getY();
            for (WWidget widgetB : widgetA) {
                widgetB.setX(x);
                widgetB.setY(y);
                y += widgetB.getHeight() + 2;
            }
            x += widgetA.get(0).getWidth() + 2;
        }
    }

    public void updateHidden() {
        for (List<WWidget> widgetList : getListWidgets()) {
            for (WWidget w : widgetList) {
                boolean startContained = isWithinBounds(w.getX(), w.getY(), 1)
                        || isWithinBounds(w.getX() + w.getWidth(), w.getY() + w.getHeight(), 1);
                w.setHidden(!startContained);
            }
        }
    }

    @Override
    public void add(WWidget... widgetArray) {
        getListWidgets().add(Arrays.asList(widgetArray));
        scrollToStart();
        updateHidden();
    }

    @Override
    public void remove(WWidget... widgetArray) {
        getListWidgets().remove(Arrays.asList(widgetArray));
        scrollToStart();
        updateHidden();
    }

    @Override
    public boolean contains(WWidget... widgetArray) {
        for (List<WWidget> widgetList : getListWidgets()) {
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

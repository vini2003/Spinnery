package spinnery.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.opengl.GL11;
import spinnery.client.BaseRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WVerticalScrollableContainer extends WWidget implements WClient, WModifiableCollection, WVerticalScrollable {
    public List<List<WWidget>> listWidgets = new ArrayList<>();

    protected WVerticalScrollbar wScrollbar;

    public WVerticalScrollableContainer(WPosition position, WSize size, WInterface linkedInterface) {
        setInterface(linkedInterface);
        setPosition(position);
        setSize(size);

        wScrollbar = new WVerticalScrollbar(linkedInterface, this);
        wScrollbar.setHidden(true);
        linkedInterface.add(wScrollbar);
        updateScrollbar();
    }

    public void updateScrollbar() {
        int scrollBarWidth = 6;
        int scrollBarHeight = getHeight();
        int scrollBarX = getX() + getWidth() - scrollBarWidth;
        int scrollBarY = getY();
        wScrollbar.setPosition(WPosition.of(WType.FREE, scrollBarX, scrollBarY, getZ()));
        wScrollbar.setSize(WSize.of(scrollBarWidth, scrollBarHeight));
    }

    @Override
    public void onMouseScrolled(int mouseX, int mouseY, double deltaY) {
        if (isWithinBounds(mouseX, mouseY)) {
            scroll(0, deltaY * 5);
            super.onMouseScrolled(mouseX, mouseY, deltaY);
        }
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

        if (deltaY > 0 && hitTop) {
            scrollToStart();
        } else if (deltaY < 0 && hitBottom) {
            scrollToEnd();
        } else {
            for (WWidget widget : getWidgets()) {
                widget.setY(widget.getY() + (int) deltaY);
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
        return WSize.of(getWidth() - (!wScrollbar.isHidden() ? wScrollbar.getWidth() : 0), getHeight());
    }

    @Override
    public WSize getInnerSize() {
        List<List<WWidget>> widgetLists = getListWidgets();

        // Topmost widget (lower Y)
        int topmostY = getStartAnchorY();
        for (WWidget widget : widgetLists.get(0)) {
            if (widget.getY() < topmostY) {
                topmostY = widget.getY();
            }
        }

        // Bottommost widget (higher Y)
        int bottommostY = topmostY;
        for (WWidget widget : widgetLists.get(widgetLists.size() - 1)) {
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
        for (WWidget widget : getListWidgets().get(0)) {
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
        int y = getStartAnchorY();

        for (List<WWidget> widgetA : getListWidgets()) {
            int x = getX();
            for (WWidget widgetB : widgetA) {
                widgetB.setX(x);
                widgetB.setY(y);
                x += widgetB.getWidth() + 2;
            }
            y += widgetA.get(0).getHeight() + 2;
        }
    }

    public void scrollToEnd() {
        int y = getEndAnchorY();

        for (List<WWidget> widgetA : getListWidgets()) {
            int x = getX();
            for (WWidget widgetB : widgetA) {
                widgetB.setX(x);
                widgetB.setY(y);
                x += widgetB.getWidth() + 2;
            }
            y += widgetA.get(0).getHeight() + 2;
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
}

package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public class WDraggableContainer extends WWidget implements WClient, WModifiableCollection,
        WVerticalScrollable, WHorizontalScrollable {
    private List<WWidget> widgets = new ArrayList<>();

    public WDraggableContainer(WPosition position, WSize size, WInterface linkedInterface) {
        setPosition(position);
        setSize(size);
        setInterface(linkedInterface);
    }

    @Override
    public int getStartAnchorX() {
        return getX();
    }

    @Override
    public int getStartAnchorY() {
        return getY();
    }

    @Override
    public int getEndAnchorX() {
        if (getVisibleWidth() > getInnerWidth()) return getStartAnchorX();
        return getStartAnchorX() - (getInnerWidth() - getVisibleWidth());
    }

    @Override
    public int getEndAnchorY() {
        if (getVisibleHeight() > getInnerHeight()) return getStartAnchorY();
        return getStartAnchorY() - (getInnerHeight() - getVisibleHeight());
    }

    @Override
    public int getStartOffsetX() {
        int leftX = getStartAnchorX();
        int leftmostX = leftX;
        for (WWidget widget : getWidgets()) {
            if (widget.getX() < leftmostX) {
                leftmostX = widget.getX();
            }
        }
        return leftX - leftmostX;
    }

    @Override
    public int getStartOffsetY() {
        int topY = getStartAnchorY();
        int topmostY = topY;
        for (WWidget widget : getWidgets()) {
            if (widget.getY() < topmostY) {
                topmostY = widget.getY();
            }
        }
        return topY - topmostY;
    }

    @Override
    public void scroll(double deltaX, double deltaY) {
        for (WWidget widget : getWidgets()) {
            widget.setX(widget.getX() + (int) deltaX);
            widget.setY(widget.getY() + (int) deltaY);
        }
        updateHidden();
    }

    @Override
    public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
        if (mouseButton == 0 && isWithinBounds(mouseX, mouseY)) {
            scroll(deltaX, deltaY);
        }
        super.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
    }

    @Override
    public WSize getInnerSize() {
        // Leftmost widget (lower X)
        int leftmostX = getStartAnchorX();
        int rightmostX = leftmostX;
        int topmostY = getStartAnchorY();
        int bottommostY = topmostY;
        for (WWidget widget : getWidgets()) {
            if (widget.getX() < leftmostX) {
                leftmostX = widget.getX();
            }
            if (widget.getX() + widget.getWidth() > rightmostX) {
                rightmostX = widget.getX() + widget.getWidth();
            }
            if (widget.getY() < topmostY) {
                topmostY = widget.getY();
            }
            if (widget.getY() + widget.getHeight() > bottommostY) {
                bottommostY = widget.getY() + widget.getHeight();
            }
        }
        return WSize.of(rightmostX - leftmostX, bottommostY - topmostY);
    }

    @Override
    public WSize getVisibleSize() {
        return getSize();
    }

    // Layout

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

    public void updateHidden() {
        for (WWidget w : getWidgets()) {
            boolean startContained = isWithinBounds(w.getX(), w.getY(), 1)
                    || isWithinBounds(w.getX() + w.getWidth(), w.getY() + w.getHeight(), 1);
            w.setHidden(!startContained);
        }
    }

    @Override
    public void draw() {
        if (isHidden()) return;

        int x = getX();
        int y = getY();
        int sX = getWidth();
        int sY = getHeight();

        int rawHeight = MinecraftClient.getInstance().getWindow().getHeight();
        double scale = MinecraftClient.getInstance().getWindow().getScaleFactor();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (x * scale), (int) (rawHeight - (y * scale + sY * scale)), (int) (sX * scale), (int) (sY * scale));

        for (WWidget w : getWidgets()) {
            w.draw();
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    // Collection

    @Override
    public void add(WWidget... widgets) {
        this.widgets.addAll(Arrays.asList(widgets));
    }

    @Override
    public void remove(WWidget... widgets) {
        this.widgets.removeAll(Arrays.asList(widgets));
    }

    @Override
    public boolean contains(WWidget... widgets) {
        return this.widgets.containsAll(Arrays.asList(widgets));
    }

    @Override
    public List<WWidget> getWidgets() {
        return widgets;
    }
}

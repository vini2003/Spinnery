package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;
import spinnery.widget.api.WHorizontalScrollable;
import spinnery.widget.api.WModifiableCollection;
import spinnery.widget.api.WSize;
import spinnery.widget.api.WVerticalScrollable;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class WDraggableContainer extends WWidget implements WModifiableCollection,
        WVerticalScrollable, WHorizontalScrollable {
    private Set<WWidget> widgets = new LinkedHashSet<>();

    protected boolean dragging = false;
    protected float scrollKineticDeltaX = 0;
    protected float scrollKineticDeltaY = 0;

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
    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isWithinBounds(mouseX, mouseY)) {
            dragging = true;
        } else {
            dragging = false;
        }
    }

    @Override
    public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
        if (mouseButton == 0 && dragging) {
            scrollKineticDeltaX += (float) deltaX;
            scrollKineticDeltaY += (float) deltaY;
            if (Math.abs(scrollKineticDeltaX) > 5) {
                scrollKineticDeltaX = (scrollKineticDeltaX / Math.abs(scrollKineticDeltaX)) * 5F;
            }
            if (Math.abs(scrollKineticDeltaY) > 5) {
                scrollKineticDeltaY = (scrollKineticDeltaY / Math.abs(scrollKineticDeltaY)) * 5F;
            }
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
            boolean hidden = (w.getX() + w.getWidth() < getX())
                    || (w.getX() > getX() + getWidth())
                    || (w.getY() + w.getHeight() < getY())
                    || (w.getY() > getY() + getHeight());
            w.setHidden(hidden);
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
    public Set<WWidget> getWidgets() {
        return widgets;
    }

    @Override
    public void tick() {
        if (scrollKineticDeltaX > 0.05 || scrollKineticDeltaX < -0.05 || scrollKineticDeltaY > 0.05 || scrollKineticDeltaY < -0.05) {
            scrollKineticDeltaX = (float) (scrollKineticDeltaX / 1.50);
            scrollKineticDeltaY = (float) (scrollKineticDeltaY / 1.50);
            scroll(scrollKineticDeltaX, scrollKineticDeltaY);
        } else {
            scrollKineticDeltaX = 0;
            scrollKineticDeltaY = 0;
        }
    }
}

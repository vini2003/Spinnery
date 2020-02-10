package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL11;
import spinnery.util.MutablePair;
import spinnery.widget.api.WDrawableCollection;
import spinnery.widget.api.WHorizontalScrollable;
import spinnery.widget.api.WLayoutElement;
import spinnery.widget.api.WModifiableCollection;
import spinnery.widget.api.Size;
import spinnery.widget.api.WVerticalScrollable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class WDraggableContainer extends WAbstractWidget implements WDrawableCollection, WModifiableCollection, WVerticalScrollable, WHorizontalScrollable {
    protected Set<WAbstractWidget> widgets = new LinkedHashSet<>();
    protected List<WLayoutElement> orderedWidgets = new ArrayList<>();

    protected boolean dragging = false;
    protected float scrollKineticDeltaX = 0;
    protected float scrollKineticDeltaY = 0;
    protected int xOffset;
    protected int yOffset;
    protected MutablePair<Integer, Integer> clickPosition = MutablePair.of(0, 0);

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
        if (getVisibleWidth() > getUnderlyingWidth()) return getStartAnchorX();
        return getStartAnchorX() - (getUnderlyingWidth() - getVisibleWidth());
    }

    @Override
    public int getEndAnchorY() {
        if (getVisibleHeight() > getUnderlyingHeight()) return getStartAnchorY();
        return getStartAnchorY() - (getUnderlyingHeight() - getVisibleHeight());
    }

    @Override
    public int getStartOffsetX() {
        return xOffset;
    }

    @Override
    public int getStartOffsetY() {
        return yOffset;
    }

    @Override
    public void scroll(double deltaX, double deltaY) {
        xOffset -= deltaX;
        yOffset -= deltaY;
        updateChildren();
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        scrollKineticDeltaX = 0;
        scrollKineticDeltaY = 0;
        dragging = mouseButton == 0 && isWithinBounds(mouseX, mouseY);
        if (dragging) {
            clickPosition.setFirst(mouseX);
            clickPosition.setSecond(mouseY);
        }
    }

    @Override
    public void onMouseDragged(int mouseX, int mouseY, int mouseButton, double deltaX, double deltaY) {
        if (mouseButton == 0 && dragging) {
            scrollKineticDeltaX += deltaX;
            scrollKineticDeltaY += deltaY;
            scroll(mouseX - clickPosition.getFirst(), mouseY - clickPosition.getSecond());
            clickPosition.setFirst(mouseX);
            clickPosition.setSecond(mouseY);
        }
        super.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
    }

    @Override
    public Size getUnderlyingSize() {
        // Leftmost widget (lower X)
        int leftmostX = getStartAnchorX();
        int rightmostX = leftmostX;
        int topmostY = getStartAnchorY();
        int bottommostY = topmostY;
        for (WAbstractWidget widget : getWidgets()) {
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
        return Size.of(rightmostX - leftmostX, bottommostY - topmostY);
    }

    @Override
    public Size getVisibleSize() {
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

        for (WAbstractWidget widget : getWidgets()) {
            widget.setX(widget.getX() + offsetX);
            widget.setY(widget.getY() + offsetY);
        }
    }

    public void updateChildren() {
        for (WAbstractWidget w : getWidgets()) {
            w.getPosition().setOffset(-xOffset, -yOffset, 0);
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

        for (WLayoutElement widget : getOrderedWidgets()) {
            widget.draw();
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    // Collection

    @Override
    public void onLayoutChange() {
        recalculateCache();
    }

    @Override
    public void recalculateCache() {
        orderedWidgets = new ArrayList<>(getWidgets());
        Collections.sort(orderedWidgets);
        Collections.reverse(orderedWidgets);
    }

    @Override
    public List<WLayoutElement> getOrderedWidgets() {
        return orderedWidgets;
    }

    @Override
    public void add(WAbstractWidget... widgets) {
        this.widgets.addAll(Arrays.asList(widgets));
        onLayoutChange();
    }

    @Override
    public void remove(WAbstractWidget... widgets) {
        this.widgets.removeAll(Arrays.asList(widgets));
        onLayoutChange();
    }

    @Override
    public boolean contains(WAbstractWidget... widgets) {
        return this.widgets.containsAll(Arrays.asList(widgets));
    }

    @Override
    public Set<WAbstractWidget> getWidgets() {
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

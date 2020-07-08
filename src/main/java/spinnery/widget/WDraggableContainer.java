package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.client.utility.ScissorArea;
import spinnery.common.utility.MutablePair;
import spinnery.widget.api.*;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

// TODO: Add smoothing.
@Environment(EnvType.CLIENT)
public class WDraggableContainer extends WAbstractWidget implements WModifiableCollection, WVerticalScrollable, WHorizontalScrollable {
    protected Set<WAbstractWidget> widgets = new LinkedHashSet<>();


    protected boolean dragging = false;
    protected float scrollKineticDeltaX = 0;
    protected float scrollKineticDeltaY = 0;
    protected int xOffset;
    protected int yOffset;
    protected MutablePair<Float, Float> clickPosition = MutablePair.of(0f, 0f);

    @Override
    public Size getUnderlyingSize() {
        // Leftmost widget (lower X)
        float leftmostX = getStartAnchorX();
        float rightmostX = leftmostX;
        float topmostY = getStartAnchorY();
        float bottommostY = topmostY;
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
    public float getStartAnchorX() {
        return getX();
    }

    @Override
    public Size getVisibleSize() {
        return getSize();
    }

    @Override
    public float getStartAnchorY() {
        return getY();
    }

    @Override
    public void add(WAbstractWidget... widgets) {
        this.widgets.addAll(Arrays.asList(widgets));
        onLayoutChange();
    }

    @Override
    public float getEndAnchorX() {
        if (getVisibleWidth() > getUnderlyingWidth()) return getStartAnchorX();
        return getStartAnchorX() - (getUnderlyingWidth() - getVisibleWidth());
    }


    @Override
    public float getEndAnchorY() {
        if (getVisibleHeight() > getUnderlyingHeight()) return getStartAnchorY();
        return getStartAnchorY() - (getUnderlyingHeight() - getVisibleHeight());
    }

    @Override
    public float getStartOffsetX() {
        return xOffset;
    }

    @Override
    public float getStartOffsetY() {
        return yOffset;
    }

    @Override
    public void remove(WAbstractWidget... widgets) {
        this.widgets.removeAll(Arrays.asList(widgets));
        onLayoutChange();
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

    @Override
    public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
        if (isHidden()) {
            return;
        }

        ScissorArea area = new ScissorArea(this);

        for (WLayoutElement widget : widgets) {
            widget.draw(matrices, provider);
        }

        area.destroy();

        super.draw(matrices, provider);
    }

    @Override
    public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
        scrollKineticDeltaX = 0;
        scrollKineticDeltaY = 0;
        dragging = mouseButton == 0 && isWithinBounds(mouseX, mouseY);
        if (dragging) {
            clickPosition.setFirst(mouseX);
            clickPosition.setSecond(mouseY);
        }
        super.onMouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onMouseDragged(float mouseX, float mouseY, int mouseButton, double deltaX, double deltaY) {
        if (mouseButton == 0 && dragging) {
            scrollKineticDeltaX += deltaX;
            scrollKineticDeltaY += deltaY;
            scroll(mouseX - clickPosition.getFirst(), mouseY - clickPosition.getSecond());
            clickPosition.setFirst(mouseX);
            clickPosition.setSecond(mouseY);
        }
        super.onMouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
    }

    // Layout

    @Override
    public void scroll(double deltaX, double deltaY) {
        xOffset -= deltaX;
        yOffset -= deltaY;
        updateChildren();
    }

    public void updateChildren() {
        for (WAbstractWidget w : getWidgets()) {
            w.getPosition().setOffset(-xOffset, -yOffset, 0);
            boolean hidden = (w.getX() + w.getWidth() < getX())
                    || (w.getX() > getWideX())
                    || (w.getY() + w.getHeight() < getY())
                    || (w.getY() > getHighY());
            w.setHidden(hidden);
        }
    }

    // Collection

    @Override
    public Set<WAbstractWidget> getWidgets() {
        return widgets;
    }

    @Override
    public boolean contains(WAbstractWidget... widgets) {
        return this.widgets.containsAll(Arrays.asList(widgets));
    }
}

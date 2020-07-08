package spinnery.widget;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.client.render.BaseRenderer;
import spinnery.client.utility.ScissorArea;
import spinnery.widget.api.*;

import java.util.*;

public class WFormContainer extends WAbstractWidget implements WModifiableCollection, WDelegatedEventListener {
    protected Set<WAbstractWidget> widgets = new HashSet<>();


    protected Map<WAbstractWidget, WAbstractWidget> widgetMap = new LinkedHashMap<>();

    public float topBottomPadding = 0f;
    public boolean topBottomOverride = false;

    public float leftRightPadding = 0f;
    public boolean leftRightOverride = false;

    public float verticalIntermediaryPadding = 0f;
    public boolean verticalIntermediaryOverride = false;

    public float horizontalIntermediaryPadding = 0f;
    public boolean horizontalIntermediaryOverride = false;

    public float outerBorderWidth = 1f;

    public boolean hasBorder;

    public <W extends WFormContainer> W setBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
        return (W) this;
    }

    public boolean hasBorder() {
        return hasBorder;
    }

    public <W extends WFormContainer> W setTopBottomPadding(float topBottomPadding) {
        this.topBottomPadding = topBottomPadding;
        this.topBottomOverride = true;
        return (W) this;
    }

    public float getTopBottomPadding() {
        return topBottomPadding;
    }

    public <W extends WFormContainer> W clearTopBottomPadding() {
        topBottomPadding = 0;
        topBottomOverride = false;
        return (W) this;
    }

    public <W extends WFormContainer> W setLeftRightPadding(float leftRightPadding) {
        this.leftRightPadding = leftRightPadding;
        this.leftRightOverride = true;
        return (W) this;
    }

    public float getLeftRightPadding() {
        return leftRightPadding;
    }

    public <W extends WFormContainer> W clearLeftRightPadding() {
        leftRightPadding = 0;
        leftRightOverride = false;
        return (W) this;
    }

    public <W extends WFormContainer> W setVerticalIntermediaryPadding(float verticalIntermediaryPadding) {
        this.verticalIntermediaryPadding = verticalIntermediaryPadding;
        this.verticalIntermediaryOverride = true;
        return (W) this;
    }

    public float getVerticalIntermediaryPadding() {
        return verticalIntermediaryPadding;
    }

    public <W extends WFormContainer> W clearVerticalIntermediaryPadding() {
        verticalIntermediaryPadding = 0;
        verticalIntermediaryOverride = false;
        return (W) this;
    }

    public <W extends WFormContainer> W setHorizontalIntermediaryPadding(float horizontalIntermediaryPadding) {
        this.horizontalIntermediaryPadding = horizontalIntermediaryPadding;
        this.horizontalIntermediaryOverride = true;
        return (W) this;
    }

    public float getHorizontalIntermediaryPadding() {
        return horizontalIntermediaryPadding;
    }

    public <W extends WFormContainer> W clearHorizontalIntermediaryPadding() {
        horizontalIntermediaryPadding = 0;
        horizontalIntermediaryOverride = false;
        return (W) this;
    }

    public <W extends WFormContainer> W setOuterBorderWidth(float outerBorderWidth) {
        this.outerBorderWidth = outerBorderWidth;
        return (W) this;
    }

    public float getOuterBorderWidth() {
        return outerBorderWidth;
    }

    @Override
    public Collection<? extends WEventListener> getEventDelegates() {
        Set<WAbstractWidget> delegates = new HashSet<>(widgets);
        return ImmutableSet.copyOf(delegates);
    }

    @Override
    @Deprecated
    public void add(WAbstractWidget... widgets) {
        if (widgets.length != 2) {
            throw new RuntimeException(getClass().getName() + " only supports two " + WAbstractWidget.class.getName() + " additions at a time, in pairs!");
        } else {
            addRow(widgets[0], widgets[1]);
        }
    }

    public <W extends WFormContainer> W addRow(WAbstractWidget left, WAbstractWidget right) {
        widgets.addAll(Arrays.asList(left, right));
        widgetMap.put(left, right);

        updateContents();

        return (W) this;
    }

    @Override
    @Deprecated
    public void remove(WAbstractWidget... widgets) {
        if (widgets.length != 2) {
            throw new RuntimeException(getClass().getName() + " only supports two " + WAbstractWidget.class.getName() + " removals at a time, in pairs!");
        } else {
            removeRow(widgets[0], widgets[1]);
        }
    }

    public <W extends WFormContainer> W removeRow(WAbstractWidget left, WAbstractWidget right) {
        widgets.removeAll(Arrays.asList(left, right));
        widgetMap.remove(left, right);

        updateContents();

        return (W) this;
    }

    @Override
    public Set<WAbstractWidget> getWidgets() {
        return widgets;
    }

    @Override
    public boolean contains(WAbstractWidget... widgets) {
        return this.widgets.containsAll(Arrays.asList(widgets));
    }

    public void updateContents() {
        if (!leftRightOverride) leftRightPadding = ((0.1f) * getWidth());
        if (!topBottomOverride) topBottomPadding = ((0.05f) * getHeight());
        if (!verticalIntermediaryOverride) verticalIntermediaryPadding = ((0.025f) * getHeight());
        if (!horizontalIntermediaryOverride) horizontalIntermediaryPadding = ((0.05f) * getWidth());

        float totalWidgetSizeX = getWidth() - (2 * leftRightPadding) - horizontalIntermediaryPadding;
        float totalWidgetSizeY = getHeight() - (2 * topBottomPadding) - ((this.widgets.size() > 1 ? this.widgets.size() / 2 - 2 : 0) * verticalIntermediaryPadding);

        float lastPositionY = topBottomPadding;
        float positionX = leftRightPadding;
        float widgetSizeX = totalWidgetSizeX;
        float widgetSizeY = totalWidgetSizeY / Math.max(this.widgets.size() / 2, 1);

        for (Map.Entry<WAbstractWidget, WAbstractWidget> entry : widgetMap.entrySet()) {
            Size newSizeKey = Size.of(widgetSizeX / 2, widgetSizeY);
            Size newSizeValue = Size.of(widgetSizeX / 2, widgetSizeY);

            WAbstractWidget leftWidget = entry.getKey();
            WAbstractWidget rightWidget = entry.getValue();

            if (entry.getKey().getMaximumAutoSize().isSmallerInWidthOrHeight(newSizeKey)) {
                newSizeKey = entry.getKey().getMaximumAutoSize();
            }
            if (entry.getValue().getMaximumAutoSize().isSmallerInWidthOrHeight(newSizeValue)) {
                newSizeValue = entry.getValue().getMaximumAutoSize();
            }

            Position newKeyPosition = Position.of(this, positionX, lastPositionY);
            Position newValuePosition = Position.of(this, positionX + widgetSizeX / 2 + horizontalIntermediaryPadding, lastPositionY);

            leftWidget.setSize(newSizeKey);
            rightWidget.setSize(newSizeValue);
            leftWidget.setPosition(newKeyPosition);
            rightWidget.setPosition(newValuePosition);
            lastPositionY += Math.max(newSizeKey.getHeight(), newSizeValue.getHeight()) + verticalIntermediaryPadding;
        }
    }

    @Override
    public void onLayoutChange() {
        super.onLayoutChange();

        updateContents();
    }

    @Override
    public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
        if (isHidden()) {
            return;
        }

        ScissorArea area = new ScissorArea(this);

        for (WAbstractWidget widget : widgets) {
            widget.draw(matrices, provider);
        }

        area.destroy();

        if (hasBorder()) {
            BaseRenderer.drawQuad(matrices, provider, getX(), getY(), getZ(), getWidth(), outerBorderWidth, getStyle().asColor("border"));
            BaseRenderer.drawQuad(matrices, provider, getX(), getY(), getZ(), outerBorderWidth, getHeight(), getStyle().asColor("border"));
            BaseRenderer.drawQuad(matrices, provider, getX(), getHighY() - 1, getZ(), getWidth(), outerBorderWidth, getStyle().asColor("border"));
            BaseRenderer.drawQuad(matrices, provider, getWideX() - 1, getY(), getZ(), outerBorderWidth, getHeight(), getStyle().asColor("border"));
        }

        super.draw(matrices, provider);
    }
}

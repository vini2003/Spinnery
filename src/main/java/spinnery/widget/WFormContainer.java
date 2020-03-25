package spinnery.widget;

import com.google.common.collect.ImmutableSet;
import spinnery.client.BaseRenderer;
import spinnery.widget.api.*;

import java.util.*;

public class WFormContainer extends WAbstractWidget implements WDrawableCollection, WModifiableCollection, WDelegatedEventListener {
    protected Set<WAbstractWidget> widgets = new HashSet<>();
    protected List<WLayoutElement> orderedWidgets = new ArrayList<>();

    protected Map<WAbstractWidget, WAbstractWidget> widgetMap = new LinkedHashMap<>();

    public int topBottomPadding = 0;
    public boolean topBottomOverride = false;

    public int leftRightPadding = 0;
    public boolean leftRightOverride = false;

    public int verticalIntermediaryPadding = 0;
    public boolean verticalIntermediaryOverride = false;

    public int horizontalIntermediaryPadding = 0;
    public boolean horizontalIntermediaryOverride = false;

    public int outerBorderWidth = 1;

    public boolean hasBorder;

    public <W extends WFormContainer> W setBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
        return (W) this;
    }

    public boolean hasBorder() {
        return hasBorder;
    }

    public <W extends WFormContainer> W setTopBottomPadding(int topBottomPadding) {
        this.topBottomPadding = topBottomPadding;
        this.topBottomOverride = true;
        return (W) this;
    }

    public int getTopBottomPadding() {
        return topBottomPadding;
    }

    public <W extends WFormContainer> W clearTopBottomPadding() {
        topBottomPadding = 0;
        topBottomOverride = false;
        return (W) this;
    }

    public <W extends WFormContainer> W setLeftRightPadding(int leftRightPadding) {
        this.leftRightPadding = leftRightPadding;
        this.leftRightOverride = true;
        return (W) this;
    }

    public int getLeftRightPadding() {
        return leftRightPadding;
    }

    public <W extends WFormContainer> W clearLeftRightPadding() {
        leftRightPadding = 0;
        leftRightOverride = false;
        return (W) this;
    }

    public <W extends WFormContainer> W setVerticalIntermediaryPadding(int verticalIntermediaryPadding) {
        this.verticalIntermediaryPadding = verticalIntermediaryPadding;
        this.verticalIntermediaryOverride = true;
        return (W) this;
    }

    public int getVerticalIntermediaryPadding() {
        return verticalIntermediaryPadding;
    }

    public <W extends WFormContainer> W clearVerticalIntermediaryPadding() {
        verticalIntermediaryPadding = 0;
        verticalIntermediaryOverride = false;
        return (W) this;
    }

    public <W extends WFormContainer> W setHorizontalIntermediaryPadding(int horizontalIntermediaryPadding) {
        this.horizontalIntermediaryPadding = horizontalIntermediaryPadding;
        this.horizontalIntermediaryOverride = true;
        return (W) this;
    }

    public int getHorizontalIntermediaryPadding() {
        return horizontalIntermediaryPadding;
    }

    public <W extends WFormContainer> W clearHorizontalIntermediaryPadding() {
        horizontalIntermediaryPadding = 0;
        horizontalIntermediaryOverride = false;
        return (W) this;
    }

    public <W extends WFormContainer> W setOuterBorderWidth(int outerBorderWidth) {
        this.outerBorderWidth = outerBorderWidth;
        return (W) this;
    }

    public int getOuterBorderWidth() {
        return outerBorderWidth;
    }

    @Override
    public Collection<? extends WEventListener> getEventDelegates() {
        Set<WAbstractWidget> delegates = new HashSet<>(widgets);
        return ImmutableSet.copyOf(delegates);
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
        recalculateCache();

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
        recalculateCache();

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
        if (!leftRightOverride) leftRightPadding = (int) ((0.1) * (float) getWidth());
        if (!topBottomOverride) topBottomPadding = (int) ((0.05) * (float) getHeight());
        if (!verticalIntermediaryOverride) verticalIntermediaryPadding = (int) ((0.025) * (float) getHeight());
        if (!horizontalIntermediaryOverride) horizontalIntermediaryPadding = (int) ((0.05) * (float) getWidth());

        int totalWidgetSizeX = getWidth() - (2 * leftRightPadding) - horizontalIntermediaryPadding;
        int totalWidgetSizeY = getHeight() - (2 * topBottomPadding) - ((this.widgets.size() > 1 ? this.widgets.size() / 2 - 2 : 0) * verticalIntermediaryPadding);

        int lastPositionY = topBottomPadding;
        int positionX = leftRightPadding;
        int widgetSizeX = totalWidgetSizeX;
        int widgetSizeY = totalWidgetSizeY / Math.max(this.widgets.size() / 2, 1);

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
    public void draw() {
        if (isHidden()) {
            return;
        }

        BaseRenderer.enableCropping();

        for (WAbstractWidget widget : widgets) {
            BaseRenderer.crop(this);
            widget.draw();
        }

        BaseRenderer.crop(this);

        if (hasBorder()) {
            BaseRenderer.drawRectangle(getX(), getY(), getZ(), getWidth(), outerBorderWidth, getStyle().asColor("border"));
            BaseRenderer.drawRectangle(getX(), getY(), getZ(), outerBorderWidth, getHeight(), getStyle().asColor("border"));
            BaseRenderer.drawRectangle(getX(), getY() + getHeight() - 1, getZ(), getWidth(), outerBorderWidth, getStyle().asColor("border"));
            BaseRenderer.drawRectangle(getX() + getWidth() - 1, getY(), getZ(), outerBorderWidth, getHeight(), getStyle().asColor("border"));
        }

        BaseRenderer.disableCropping();
    }
}

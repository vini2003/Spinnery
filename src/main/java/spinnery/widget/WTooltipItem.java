package spinnery.widget;

import spinnery.widget.api.Position;
import spinnery.widget.api.Size;

public class WTooltipItem extends WItem {
    WTooltip tooltip;
    WStaticText tooltipText;

    @Override
    public void onFocusGained() {
        super.onFocusGained();
        updateHidden(false);
    }

    @Override
    public void onFocusReleased() {
        super.onFocusReleased();
        updateHidden(true);
    }

    @Override
    public void onMouseMoved(int mouseX, int mouseY) {
        updateWidgets();
        updateText();
        updatePositions(mouseX, mouseY);
        updateSizes();
    }

    public void updateWidgets() {
        if (tooltip == null) tooltip = getInterface().createChild(WTooltip.class, Position.of(this), Size.of(this)).setHidden(true);
        if (tooltipText == null) tooltipText = getInterface().createChild(WStaticText.class, Position.of(tooltip).add(0, 0, 1), Size.of(this)).setHidden(true);
    }

    public void updateText() {
        tooltipText.setText(stack.getName());
    }

    public void updatePositions(int mouseX, int mouseY) {
        tooltip.setPosition(Position.of(mouseX + 12, mouseY - 4, 1));
    }

    public void updateSizes() {
        tooltip.setSize(Size.of(tooltipText.getWidth() - 1, tooltipText.getHeight() - 1));
    }

    public void updateHidden(boolean hidden) {
        tooltip.setHidden(hidden);
        tooltipText.setHidden(hidden);
    }

    @Override
    public void tick() {
        updateWidgets();
        updateText();
    }

    @Override
    public void draw() {
        super.draw();
        tooltip.draw();
        tooltipText.draw();
    }
}

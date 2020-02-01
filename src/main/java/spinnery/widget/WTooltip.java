package spinnery.widget;

import spinnery.client.BaseRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class WTooltip extends WWidget implements WModifiableCollection {
    private Set<WWidget> widgets = new LinkedHashSet<>();

    public WTooltip(WPosition position, WSize size, WInterface linkedInterface) {
        setPosition(position);
        setSize(size);
        setInterface(linkedInterface);
    }

    @Override
    public void draw() {
        if (isHidden()) return;

        int x = position.getX();
        int y = position.getY();
        int z = position.getRawZ();
        int width = size.getX();
        int height = size.getY();

        WColor backgroundStart = getStyle().asColor("background.start");
        WColor backgroundEnd = getStyle().asColor("background.end");
        WColor colorStart = getStyle().asColor("outline.start");
        WColor colorEnd = getStyle().asColor("outline.end");
        WColor shadowStart = getStyle().asColor("shadow.start");
        WColor shadowEnd = getStyle().asColor("shadow.end");

        // Vanilla drawing process
        BaseRenderer.drawGradient(x - 3, y - 4, x + width + 3, y - 3, z, shadowStart, shadowStart); // top border
        BaseRenderer.drawGradient(x - 3, y + height + 3, x + width + 3, y + height + 4, z, shadowEnd, shadowEnd); // bottom border
        BaseRenderer.drawGradient(x - 3, y - 3, x + width + 3, y + height + 3, z, backgroundStart, backgroundEnd); // body
        BaseRenderer.drawGradient(x - 4, y - 3, x - 3, y + height + 3, z, shadowStart, shadowEnd); // left border
        BaseRenderer.drawGradient(x + width + 3, y - 3, x + width + 4, y + height + 3, z, shadowStart, shadowEnd); // right border

        BaseRenderer.drawGradient(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, z, colorStart, colorEnd); // left outline
        BaseRenderer.drawGradient(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, z, colorStart, colorEnd); // right outline
        BaseRenderer.drawGradient(x - 3, y - 3, x + width + 3, y - 3 + 1, z, colorStart, colorStart); // top outline
        BaseRenderer.drawGradient(x - 3, y + height + 2, x + width + 3, y + height + 3, z, colorEnd, colorEnd); // bottom outline

        for (WWidget widget : widgets) {
            widget.draw();
        }
    }

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
}

package spinnery.widget;

import spinnery.client.BaseRenderer;
import spinnery.widget.api.WColor;
import spinnery.widget.api.WModifiableCollection;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class WTooltip extends WAbstractWidget implements WModifiableCollection {
    private Set<WAbstractWidget> widgets = new LinkedHashSet<>();

    @Override
    public void draw() {
        if (isHidden()) return;

        int x = position.getX();
        int y = position.getY();
        int z = position.getRelativeZ();
        int width = size.getWidth();
        int height = size.getHeight();

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

        for (WAbstractWidget widget : widgets) {
            widget.draw();
        }
    }

    @Override
    public void add(WAbstractWidget... widgets) {
        this.widgets.addAll(Arrays.asList(widgets));
    }

    @Override
    public void remove(WAbstractWidget... widgets) {
        this.widgets.removeAll(Arrays.asList(widgets));
    }

    @Override
    public boolean contains(WAbstractWidget... widgets) {
        return this.widgets.containsAll(Arrays.asList(widgets));
    }

    @Override
    public Set<WAbstractWidget> getWidgets() {
        return widgets;
    }
}

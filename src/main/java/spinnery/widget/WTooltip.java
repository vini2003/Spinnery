package spinnery.widget;

import spinnery.client.BaseRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WTooltip extends WWidget implements WModifiableCollection {
    public static final int BACKGROUND_START = 0;
    public static final int BACKGROUND_END = 1;
    public static final int OUTLINE_START = 2;
    public static final int OUTLINE_END = 3;
    public static final int SHADOW_START = 4;
    public static final int SHADOW_END = 5;
    private List<WWidget> widgets = new ArrayList<>();

    public WTooltip(WPosition position, WSize size, WInterface linkedInterface) {
        setPosition(position);
        setSize(size);
        setInterface(linkedInterface);
        setTheme("light");
    }

    @Override
    public void setTheme(String theme) {
        if (getInterface().isClient()) {
            super.setTheme(theme);
        }
    }

    public static WWidget.Theme of(Map<String, String> rawTheme) {
        WWidget.Theme theme = new WWidget.Theme();
        theme.add(BACKGROUND_START, WColor.of(rawTheme.get("background_start")));
        theme.add(BACKGROUND_END, WColor.of(rawTheme.get("background_end")));
        theme.add(OUTLINE_START, WColor.of(rawTheme.get("outline_start")));
        theme.add(OUTLINE_END, WColor.of(rawTheme.get("outline_end")));
        theme.add(SHADOW_START, WColor.of(rawTheme.get("shadow_start")));
        theme.add(SHADOW_END, WColor.of(rawTheme.get("shadow_end")));
        return theme;
    }

    @Override
    public void draw() {
        if (isHidden()) return;

        int x = position.getX();
        int y = position.getY();
        int z = position.getRawZ();
        int width = size.getX();
        int height = size.getY();

        WColor backgroundStart = getColor(BACKGROUND_START);
        WColor backgroundEnd = getColor(BACKGROUND_END);
        WColor colorStart = getColor(OUTLINE_START);
        WColor colorEnd = getColor(OUTLINE_END);
        WColor shadowStart = getColor(SHADOW_START);
        WColor shadowEnd = getColor(SHADOW_END);

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
    public List<WWidget> getWidgets() {
        return widgets;
    }
}

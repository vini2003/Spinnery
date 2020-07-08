package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.client.render.BaseRenderer;
import spinnery.client.render.TextRenderer;
import spinnery.widget.api.WDelegatedEventListener;
import spinnery.widget.api.WEventListener;
import spinnery.widget.api.WLayoutElement;
import spinnery.widget.api.WModifiableCollection;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class WPanel extends WAbstractWidget implements WModifiableCollection, WDelegatedEventListener {
    protected Set<WAbstractWidget> widgets = new LinkedHashSet<>();


    @Override
    public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
        if (isHidden()) {
            return;
        }

        float x = getX();
        float y = getY();
        float z = getZ();

        float sX = getWidth();
        float sY = getHeight();

        BaseRenderer.drawPanel(matrices, provider, x, y, z, sX, sY, getStyle().asColor("shadow"), getStyle().asColor("background"), getStyle().asColor("highlight"), getStyle().asColor("outline"));

        if (hasLabel()) {
            TextRenderer.pass().shadow(isLabelShadowed())
                    .text(getLabel()).at(x + 8, y + 6, z)
                    .color(getStyle().asColor("label.color")).shadowColor(getStyle().asColor("label.shadow_color")).render(matrices, provider);
        }

        for (WLayoutElement widget : widgets) {
            widget.draw(matrices, provider);
        }

        super.draw(matrices, provider);
    }

    @Override
    public void add(WAbstractWidget... widgets) {
        this.widgets.addAll(Arrays.asList(widgets));
        onLayoutChange();
    }

    @Override
    public Set<WAbstractWidget> getWidgets() {
        return widgets;
    }

    @Override
    public boolean contains(WAbstractWidget... widgets) {
        return this.widgets.containsAll(Arrays.asList(widgets));
    }

    @Override
    public void remove(WAbstractWidget... widgets) {
        this.widgets.removeAll(Arrays.asList(widgets));
        onLayoutChange();
    }

    @Override
    public Collection<? extends WEventListener> getEventDelegates() {
        return getWidgets();
    }
}

package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.client.render.BaseRenderer;
import spinnery.client.render.TextRenderer;
import spinnery.widget.api.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class WPanel extends WAbstractWidget implements WModifiableCollection, WDrawableCollection, WDelegatedEventListener {
	protected Set<WAbstractWidget> heldWidgets = new LinkedHashSet<>();
	protected List<WLayoutElement> orderedWidgets = new ArrayList<>();

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
		if(isHidden()) { return; }

		RenderSystem.translatef(0, 0, getZ() * 400f);
  		matrices.translate(0, 0, getZ() * 400f);

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

		for (WLayoutElement widget : getOrderedWidgets()) {
			widget.draw(matrices, provider);
		}

  		matrices.translate(0, 0, getZ() * -400f);
		RenderSystem.translatef(0, 0, getZ() * -400f);
	}

	@Override
	public void add(WAbstractWidget... widgets) {
		heldWidgets.addAll(Arrays.asList(widgets));
		onLayoutChange();
	}

	@Override
	public void onLayoutChange() {
		super.onLayoutChange();
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
	public Set<WAbstractWidget> getWidgets() {
		return heldWidgets;
	}

	@Override
	public boolean contains(WAbstractWidget... widgets) {
		return heldWidgets.containsAll(Arrays.asList(widgets));
	}

	@Override
	public void remove(WAbstractWidget... widgets) {
		heldWidgets.removeAll(Arrays.asList(widgets));
		onLayoutChange();
	}

	@Override
	public Collection<? extends WEventListener> getEventDelegates() {
		return getWidgets();
	}
}

package spinnery.widget.implementation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.Spinnery;
import spinnery.client.texture.PartitionedTexture;
import spinnery.client.utilities.Texts;
import spinnery.widget.declaration.collection.WModifiableCollection;

import java.util.*;

@Environment(EnvType.CLIENT)
public class WPanel extends WAbstractWidget implements WModifiableCollection {
	private final PartitionedTexture texture = new PartitionedTexture(Spinnery.identifier("textures/widget/panel.png"), 18F, 18F, 0.25F, 0.25F, 0.25F, 0.25F);

	private final ArrayList<WAbstractWidget> widgets = new ArrayList<>();

	@Override
	public ArrayList<WAbstractWidget> getWidgets() {
		return widgets;
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		float x = getX();
		float y = getY();

		float sX = getWidth();
		float sY = getHeight();

		texture.draw(matrices, provider, x, y, sX, sY);

		if (hasLabel()) {
			Texts.pass().shadow(isLabelShadowed()).text(getLabel()).at(x + 8, y + 6).color(getStyle().asColor("label.color")).render(matrices, provider);
		}

		for (WAbstractWidget widget : widgets) {
			widget.draw(matrices, provider);
		}
	}
}

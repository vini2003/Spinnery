package spinnery.widget;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import spinnery.Spinnery;
import spinnery.client.texture.PartitionedTexture;
import spinnery.client.utilities.Drawings;
import spinnery.client.utilities.Texts;

import java.util.List;

@Environment(EnvType.CLIENT)
public class WButton extends WAbstractWidget {
	private final PartitionedTexture textureOn = new PartitionedTexture(Spinnery.identifier("textures/widget/button_on.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.16666666666666666667F);
	private final PartitionedTexture textureOff = new PartitionedTexture(Spinnery.identifier("textures/widget/button_off.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F);
	private final PartitionedTexture textureOnFocus = new PartitionedTexture(Spinnery.identifier("textures/widget/button_on_focus.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F);

	private boolean isDisabled = false;

	public boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(boolean disabled) {
		isDisabled = disabled;
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isDisabled()) {
			textureOff.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
		} else {
			if (isFocused()) {
				textureOnFocus.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			} else {
				textureOn.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
			}
		}

		if (hasLabel()) {
			Drawings.getTextRenderer().drawWithShadow(matrices, getLabel(), getX() + (getWidth() / 2F - Texts.width(getLabel()) / 2F), getY() + (getHeight() / 2F - Texts.height() / 2F), getStyle().asColor("button.label").ARGB);
		}
	}
}

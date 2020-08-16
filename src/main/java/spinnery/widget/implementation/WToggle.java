package spinnery.widget.implementation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.Spinnery;
import spinnery.client.texture.PartitionedTexture;
import spinnery.client.utilities.Texts;

@Environment(EnvType.CLIENT)
public class WToggle extends WAbstractWidget {
	private final PartitionedTexture textureOn = new PartitionedTexture(Spinnery.identifier("textures/widget/toggle_background_on.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.16666666666666666667F);
	private final PartitionedTexture textureOff = new PartitionedTexture(Spinnery.identifier("textures/widget/toggle_background_off.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F);
	private final PartitionedTexture textureKnob = new PartitionedTexture(Spinnery.identifier("textures/widget/toggle_knob.png"), 18F, 18F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F, 0.11111111111111111111F);

	protected boolean isToggled = false;

	@Override
	public void onMouseClicked(float mouseX, float mouseY, int mouseButton) {
		setToggled(!isToggled());
		super.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	public boolean isToggled() {
		return isToggled;
	}

	public <W extends WToggle> W setToggled(boolean toggled) {
		this.isToggled = toggled;
		return (W) this;
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

		if (isToggled()) {
			textureOn.draw(matrices, provider, x + 1, y + 1, sX - 2, sY - 2);
			textureKnob.draw(matrices, provider, x + sX - 8, y - 1, 8, sY + 3);
		} else {
			textureOff.draw(matrices, provider, x + 1, y + 1, sX - 2, sY - 2);
			textureKnob.draw(matrices, provider, x + 1, y - 1, 8, sY + 3);
		}

		if (hasLabel()) {
			Texts.pass().shadow(isLabelShadowed()).text(getLabel()).at(x + sX + 2, y + sY / 2 - 4.5).color(getStyle().asColor("label.color")).render(matrices, provider);
		}
	}
}

package spinnery.widget.implementation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import spinnery.client.utilities.Texts;

@Environment(EnvType.CLIENT)
public class WText extends WAbstractWidget {
	protected Text text = new LiteralText("");

	protected float scale = 1.0F;

	protected int maximumWidth = Integer.MIN_VALUE;

	public int getMaximumWidth() {
		return maximumWidth;
	}

	public <W extends WText> W setMaximumWidth(int maximumWidth) {
		this.maximumWidth = maximumWidth;
		return (W) this;
	}

	public float getScale() {
		return scale;
	}

	public WText setScale(float scale) {
		this.scale = scale;
		return this;
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		float x = getX();
		float y = getY();

		Texts.pass().text(getText()).at(x, y).scale(scale).maxWidth(maximumWidth).shadow(getStyle().asBoolean("shadow")).color(getStyle().asColor("text")).render(matrices, provider);
	}

	@Override
	public float getHeight() {
		return Texts.height();
	}

	@Override
	public float getWidth() {
		return Texts.width(text);
	}

	public Text getText() {
		return text;
	}

	public <W extends WText> W setText(Text text) {
		this.text = text;
		return (W) this;
	}

	public <W extends WText> W setText(String text) {
		this.text = new LiteralText(text);
		return (W) this;
	}
}

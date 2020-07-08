package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import spinnery.client.render.TextRenderer;

@Environment(EnvType.CLIENT)
public class WStaticText extends WAbstractWidget {
	protected Text text = new LiteralText("");

	protected float scale = 1.0F;

	protected int maxWidth = Integer.MIN_VALUE;

	public int getMaxWidth() {
		return maxWidth;
	}

	public <W extends WStaticText> W setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
		return (W) this;
	}

	public float getScale() {
		return scale;
	}

	public WStaticText setScale(float scale) {
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
		float z = getZ();

		TextRenderer.pass().text(getText()).at(x, y, z).scale(scale).maxWidth(maxWidth)
				.shadow(getStyle().asBoolean("shadow")).shadowColor(getStyle().asColor("shadowColor"))
				.color(getStyle().asColor("text")).render(matrices, provider);

		super.draw(matrices, provider);
	}

	@Override
	public float getHeight() {
		return TextRenderer.height();
	}

	@Override
	public float getWidth() {
		return TextRenderer.width(text);
	}

	public Text getText() {
		return text;
	}

	public <W extends WStaticText> W setText(Text text) {
		this.text = text;
		return (W) this;
	}

	public <W extends WStaticText> W setText(String text) {
		this.text = new LiteralText(text);
		return (W) this;
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}
}

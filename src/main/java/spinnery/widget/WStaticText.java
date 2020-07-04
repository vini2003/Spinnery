package spinnery.widget;

import com.mojang.blaze3d.systems.RenderSystem;
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
	protected double scale = 1.0;
	protected TextRenderer.Font font = TextRenderer.Font.DEFAULT;
	protected Integer maxWidth = null;

	public Integer getMaxWidth() {
		return maxWidth;
	}

	public <W extends WStaticText> W setMaxWidth(Integer maxWidth) {
		this.maxWidth = maxWidth;
		return (W) this;
	}

	public TextRenderer.Font getFont() {
		return font;
	}

	public <W extends WStaticText> W setFont(TextRenderer.Font font) {
		this.font = font;
		return (W) this;
	}

	public double getScale() {
		return scale;
	}

	public WStaticText setScale(double scale) {
		this.scale = scale;
		return this;
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
		if (isHidden()) {
			return;
		}

		RenderSystem.translatef(0, 0, getZ() * 400f);
  		matrices.translate(0, 0, getZ() * 400f);

		float x = getX();
		float y = getY();
		float z = getZ();

		TextRenderer.pass().text(getText()).font(font).at(x, y, z).scale(scale).maxWidth(maxWidth)
				.shadow(getStyle().asBoolean("shadow")).shadowColor(getStyle().asColor("shadowColor"))
				.color(getStyle().asColor("text")).render(matrices, provider);

  		matrices.translate(0, 0, getZ() * -400f);
		RenderSystem.translatef(0, 0, getZ() * -400f);
	}

	@Override
	public float getHeight() {
		return TextRenderer.height(font);
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

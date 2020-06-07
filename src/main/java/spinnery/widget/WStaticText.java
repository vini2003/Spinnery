package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
	public void draw() {
		if (isHidden()) {
			return;
		}

		float x = getX();
		float y = getY();
		float z = getZ();

		TextRenderer.pass().text(getText()).font(font).at(x, y, z).scale(scale).maxWidth(maxWidth)
				.shadow(getStyle().asBoolean("shadow")).shadowColor(getStyle().asColor("shadowColor"))
				.color(getStyle().asColor("text")).render();
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

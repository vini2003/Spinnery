package spinnery.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import spinnery.client.TextRenderer;
import spinnery.widget.api.WFocusedMouseListener;

@Environment(EnvType.CLIENT)
@WFocusedMouseListener
public class WStaticText extends WWidget {
	protected Text text = new LiteralText("");
	protected TextRenderer.Font font = TextRenderer.Font.DEFAULT;
	protected Integer maxWidth = null;

	public Integer getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(Integer maxWidth) {
		this.maxWidth = maxWidth;
	}

	public TextRenderer.Font getFont() {
		return font;
	}

	public WStaticText setFont(TextRenderer.Font font) {
		this.font = font;
		return this;
	}

	@Override
	public int getWidth() {
		return TextRenderer.width(text);
	}

	@Override
	public int getHeight() {
		return TextRenderer.height(font);
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		int x = getX();
		int y = getY();
		int z = getZ();

		TextRenderer.pass().shadow(isLabelShadowed()).text(getText()).font(font).at(x, y, z).maxWidth(maxWidth)
				.shadow(getStyle().asBoolean("shadow")).shadowColor(getStyle().asColor("shadowColor"))
				.color(getStyle().asColor("text")).render();
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public void setText(String text) {
		this.text = new LiteralText(text);
	}
}

package spinnery.widget;

import net.minecraft.text.Text;
import spinnery.client.BaseRenderer;

import java.util.Map;

public class WStaticText extends WWidget implements WClient {

	public static final int SHADOW = 6;
	public static final int TEXT = 7;
	protected Text text;
	protected BaseRenderer.Font font;
	protected Integer maxWidth = null;

	public WStaticText(WPosition position, WInterface linkedInterface, Text text, BaseRenderer.Font font) {
		setInterface(linkedInterface);

		setPosition(position);

		setText(text);

		setTheme("light");

		this.font = font;
	}

	public WStaticText(WPosition position, WInterface linkedInterface, Text text) {
		this(position, linkedInterface, text, BaseRenderer.Font.DEFAULT);
	}

	public static WWidget.Theme of(Map<String, String> rawTheme) {
		WWidget.Theme theme = new WWidget.Theme();
		theme.add(SHADOW, WColor.of(rawTheme.get("shadow")));
		theme.add(TEXT, WColor.of(rawTheme.get("text")));
		return theme;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public Integer getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(Integer maxWidth) {
		this.maxWidth = maxWidth;
	}

	public BaseRenderer.Font getFont() {
		return font;
	}

	public void setFont(BaseRenderer.Font font) {
		this.font = font;
	}

	@Override
	public void setTheme(String theme) {
		if (getInterface().isClient()) {
			super.setTheme(theme);
		}
	}

	@Override
	public int getWidth() {
		return BaseRenderer.getTextRenderer(font).getStringWidth(text.asString());
	}

	@Override
	public int getHeight() {
		return BaseRenderer.getTextRenderer(font).fontHeight;
	}

	@Override
	public void draw() {
		if (isHidden()) {
			return;
		}

		int x = getX();
		int y = getY();

		if (isLabelShadowed()) {
			BaseRenderer.drawTextTrimmed(getText().asFormattedString(), x + 1, y + 1, maxWidth, getColor(SHADOW).RGB, font);
		}
		BaseRenderer.drawTextTrimmed(getText().asFormattedString(), x, y, maxWidth, getColor(TEXT).RGB, font);
	}
}

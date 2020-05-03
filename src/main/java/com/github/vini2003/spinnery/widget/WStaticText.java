package com.github.vini2003.spinnery.widget;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.github.vini2003.spinnery.client.TextRenderer;

@OnlyIn(Dist.CLIENT)
public class WStaticText extends WAbstractWidget {
	protected ITextComponent text = new StringTextComponent("");
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

		int x = getX();
		int y = getY();
		int z = getZ();

		TextRenderer.pass().text(getText()).font(font).at(x, y, z).scale(scale).maxWidth(maxWidth)
				.shadow(getStyle().asBoolean("shadow")).shadowColor(getStyle().asColor("shadowColor"))
				.color(getStyle().asColor("text")).render();
	}

	@Override
	public int getHeight() {
		return TextRenderer.height(font);
	}

	@Override
	public int getWidth() {
		return TextRenderer.width(text);
	}

	public ITextComponent getText() {
		return text;
	}

	public <W extends WStaticText> W setText(ITextComponent text) {
		this.text = text;
		return (W) this;
	}

	public <W extends WStaticText> W setText(String text) {
		this.text = new StringTextComponent(text);
		return (W) this;
	}

	@Override
	public boolean isFocusedMouseListener() {
		return true;
	}
}

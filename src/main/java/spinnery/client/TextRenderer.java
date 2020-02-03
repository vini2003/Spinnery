package spinnery.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import spinnery.widget.api.WColor;
import spinnery.widget.api.WPosition;

public class TextRenderer {
	public static RenderPass pass() {
		return new RenderPass();
	}

	public static int height() {
		return height(Font.DEFAULT);
	}

	public static int height(Font font) {
		return getTextRenderer(font).fontHeight;
	}

	public static net.minecraft.client.font.TextRenderer getTextRenderer(Font font) {
		switch (font) {
			case ENCHANTMENT:
				return MinecraftClient.getInstance().getFontManager()
						.getTextRenderer(MinecraftClient.ALT_TEXT_RENDERER_ID);
			case DEFAULT:
			default:
				return MinecraftClient.getInstance().textRenderer;
		}
	}

	public static int width(char character) {
		return width(character, Font.DEFAULT);
	}

	public static int width(char character, Font font) {
		return (int) getTextRenderer(font).getCharWidth(character);
	}

	public static int width(String string) {
		return width(string, Font.DEFAULT);
	}

	public static int width(String string, Font font) {
		return getTextRenderer(font).getStringWidth(string);
	}

	public static int width(Text text, Font font) {
		return width(text.asString(), font);
	}

	public static int width(Text text) {
		return width(text.asString(), Font.DEFAULT);
	}

	public enum Font {
		DEFAULT,
		ENCHANTMENT,
	}

	public static class RenderPass {
		private String text;
		private int x;
		private int y;
		private int z;
		private int color = 0xffffffff;
		private int shadowColor = 0xff3e3e3e;
		private boolean shadow;
		private Integer maxWidth;
		private Font font = Font.DEFAULT;

		public RenderPass text(String text) {
			this.text = text;
			return this;
		}

		public RenderPass text(char c) {
			this.text = String.valueOf(c);
			return this;
		}

		public RenderPass text(Text text) {
			this.text = text.asFormattedString();
			return this;
		}

		public RenderPass at(WPosition position) {
			return at(position.getX(), position.getY(), position.getZ());
		}

		public RenderPass at(Number x, Number y, Number z) {
			this.x = x.intValue();
			this.y = y.intValue();
			this.z = z.intValue();
			return this;
		}

		public RenderPass color(WColor color) {
			return color(color.ARGB);
		}

		public RenderPass color(int color) {
			this.color = color;
			return this;
		}

		public RenderPass shadow(boolean shadow) {
			this.shadow = shadow;
			return this;
		}

		public RenderPass shadowColor(WColor color) {
			return shadowColor(color.ARGB);
		}

		public RenderPass shadowColor(int color) {
			this.shadowColor = color;
			return this;
		}

		public RenderPass maxWidth(Integer maxWidth) {
			this.maxWidth = maxWidth;
			return this;
		}

		public RenderPass font(Font font) {
			this.font = font;
			return this;
		}

		public void render() {
			RenderSystem.translatef(0.0f, 0.0f, (float) z);
			if (maxWidth != null) {
				if (shadow)
					getTextRenderer(font).drawTrimmed(text, x + 1, y + 1, maxWidth, shadowColor);
				getTextRenderer(font).drawTrimmed(text, x, y, maxWidth, color);
			} else {
				if (shadow) getTextRenderer(font).draw(text, x + 1, y + 1, shadowColor);
				getTextRenderer(font).draw(text, x, y, color);
			}
			RenderSystem.translatef(0.0f, 0.0f, (float) -z);
		}
	}
}

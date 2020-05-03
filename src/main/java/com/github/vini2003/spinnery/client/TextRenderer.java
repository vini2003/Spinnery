package com.github.vini2003.spinnery.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import com.github.vini2003.spinnery.widget.api.Color;
import com.github.vini2003.spinnery.widget.api.Position;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;

public class TextRenderer {
	public static RenderPass pass() {
		return new RenderPass();
	}

	public static int height() {
		return height(Font.DEFAULT);
	}

	public static int height(Font font) {
		return getTextRenderer(font).FONT_HEIGHT;
	}

	public static FontRenderer getTextRenderer(Font font) {
		switch (font) {
			case ENCHANTMENT:
				return Minecraft.getInstance().getFontResourceManager()
						.getFontRenderer(Minecraft.DEFAULT_FONT_RENDERER_NAME);
			case DEFAULT:
			default:
				return Minecraft.getInstance().fontRenderer;
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

	public static int width(ITextComponent text, Font font) {
		return width(text.getFormattedText(), font);
	}

	public static int width(ITextComponent text) {
		return width(text.getFormattedText(), Font.DEFAULT);
	}

	public enum Font {
		DEFAULT,
		ENCHANTMENT,
	}

	public static class RenderPass {
		private String text;
		private String shadowText;
		private int x;
		private int y;
		private int z;
		private int color = 0xffffffff;
		private int shadowColor = 0xff3e3e3e;
		private double scale = 1.0;
		private boolean shadow;
		private Integer maxWidth;
		private Font font = Font.DEFAULT;

		public RenderPass text(String text) {
			this.text = text;
			this.shadowText = text.replaceAll("§[0-9a-f]", "");
			return this;
		}

		public RenderPass text(char c) {
			this.text = String.valueOf(c);
			this.shadowText = text;
			return this;
		}

		public RenderPass text(ITextComponent text) {
			this.text = text.getFormattedText();
			this.shadowText = this.text.replaceAll("§[0-9a-f]", "");
			return this;
		}

		public RenderPass at(Position position) {
			return at(position.getX(), position.getY(), position.getZ());
		}

		public RenderPass at(Number x, Number y, Number z) {
			this.x = x.intValue();
			this.y = y.intValue();
			this.z = z.intValue();
			return this;
		}

		public RenderPass size(int size) {
			return scale(size / 9D);
		}

		public RenderPass scale(double scale) {
			this.scale = scale;
			return this;
		}

		public RenderPass color(Color color) {
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

		public RenderPass shadowColor(Color color) {
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
			float oX = x * (1f - (float) scale);
			float oY = y * (1f - (float) scale);
			RenderSystem.pushMatrix();
			RenderSystem.translatef(oX, oY, z);
			RenderSystem.scaled(scale, scale, 1);
			if (maxWidth != null) {
				if (shadow)
					getTextRenderer(font).drawSplitString(shadowText, x + 1, y + 1, maxWidth, shadowColor);
				getTextRenderer(font).drawSplitString(text, x, y, maxWidth, color);
			} else {
				if (shadow) getTextRenderer(font).drawString(shadowText, x + 1, y + 1, shadowColor);
				getTextRenderer(font).drawString(text, x, y, color);
			}
			RenderSystem.popMatrix();
		}
	}
}

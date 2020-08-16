package spinnery.client.utilities;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Text;
import spinnery.widget.api.Color;
import spinnery.widget.api.Position;

public class Texts {
	public static RenderPass pass() {
		return new RenderPass();
	}

	public static int height() {
		return Drawings.getTextRenderer().fontHeight;
	}

	public static int width(char character) {
		return Drawings.getTextRenderer().getWidth(String.valueOf(character));
	}

	public static int width(String string) {
		return Drawings.getTextRenderer().getWidth(string);
	}

	public static int width(Text text) {
		return width(text.getString());
	}

	public static class RenderPass {
		private String text;

		private int x;
		private int y;

		private int color = 0xffffffff;

		private float scale = 1.0F;

		private boolean shadow;

		private int maxWidth = Integer.MIN_VALUE;

		public RenderPass text(String text) {
			this.text = text;
			return this;
		}

		public RenderPass text(char c) {
			this.text = String.valueOf(c);
			return this;
		}

		public RenderPass text(Text text) {
			this.text = text.getString();
			return this;
		}

		public RenderPass at(Position position) {
			return at(position.getX(), position.getY(), position.getZ());
		}

		public RenderPass at(Number x, Number y, Number z) {
			this.x = x.intValue();
			this.y = y.intValue();
			return this;
		}

		public RenderPass size(float size) {
			return scale(size / 9f);
		}

		public RenderPass scale(float scale) {
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

		public RenderPass maxWidth(int maxWidth) {
			this.maxWidth = maxWidth;
			return this;
		}

		public void render(MatrixStack matrices, VertexConsumerProvider provider) {
			matrices.push();

			matrices.scale(scale, scale, 0);

			if (maxWidth == Integer.MIN_VALUE) {
				if (shadow) {
					Drawings.getTextRenderer().drawWithShadow(matrices, StringRenderable.plain(text), x, y, maxWidth);
				} else {
					Drawings.getTextRenderer().drawTrimmed(StringRenderable.plain(text), x, y, maxWidth, color);
				}
			} else {
				Drawings.getTextRenderer().drawTrimmed(StringRenderable.plain(text), x, y, maxWidth, color);
			}

			matrices.pop();
		}
	}
}

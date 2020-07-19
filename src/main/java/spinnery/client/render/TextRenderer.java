package spinnery.client.render;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringRenderable;
import net.minecraft.text.Text;
import spinnery.widget.api.Color;
import spinnery.widget.api.Position;

public class TextRenderer {
	public static RenderPass pass() {
		return new RenderPass();
	}

	public static int height() {
		return BaseRenderer.getAdvancedTextRenderer().fontHeight;
	}

	public static int width(char character) {
		return BaseRenderer.getAdvancedTextRenderer().getWidth(String.valueOf(character));
	}

	public static int width(String string) {
		return BaseRenderer.getAdvancedTextRenderer().getWidth(string);
	}

	public static int width(Text text) {
		return width(text.getString());
	}

	public static class RenderPass {
		private StringRenderable text;
		private StringRenderable shadowText;

		private float x;
		private float y;
		private float z;

		private int color = 0xffffffff;
		private int shadowColor = 0xff3e3e3e;

		private float scale = 1.0F;

		private boolean shadow;

		private int maxWidth = Integer.MIN_VALUE;

		public RenderPass text(String text) {
			this.text = StringRenderable.plain(text);
			this.shadowText = this.text;
			return this;
		}

		public RenderPass text(char c) {
			this.text = StringRenderable.plain(String.valueOf(c));
			this.shadowText = text;
			return this;
		}

		public RenderPass text(Text text) {
			this.text = text;
			this.shadowText = this.text;
			return this;
		}
        
        public RenderPass text(StringRenderable text) {
            this.text = text;
            this.shadowText = this.text;
            return this;
        }

		public RenderPass at(Position position) {
			return at(position.getX(), position.getY(), position.getZ());
		}

		public RenderPass at(Number x, Number y, Number z) {
			this.x = x.floatValue();
			this.y = y.floatValue();
			this.z = z.floatValue();
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

		public RenderPass shadowColor(Color color) {
			return shadowColor(color.ARGB);
		}

		public RenderPass shadowColor(int color) {
			this.shadowColor = color;
			return this;
		}

		public RenderPass maxWidth(int maxWidth) {
			this.maxWidth = maxWidth;
			return this;
		}

		public void render(MatrixStack matrices, VertexConsumerProvider provider) {
			matrices.push();

			matrices.scale(scale, scale, 0);

			if (maxWidth != Integer.MIN_VALUE) {
				if (shadow)
					BaseRenderer.getAdvancedTextRenderer().drawTrimmed(matrices, provider, shadowText, x + 1, y + 1, z, maxWidth, shadowColor);
				BaseRenderer.getAdvancedTextRenderer().drawTrimmed(matrices, provider, text, x, y, z, maxWidth, color);
			} else {
				if (shadow)
					BaseRenderer.getAdvancedTextRenderer().draw(matrices, provider, shadowText, x + 1, y + 1, z, shadowColor);
				BaseRenderer.getAdvancedTextRenderer().draw(matrices, provider, text, x, y, z, color);
			}

			matrices.pop();
		}
	}
}

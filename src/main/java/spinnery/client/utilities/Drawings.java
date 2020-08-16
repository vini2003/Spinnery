package spinnery.client.utilities;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import spinnery.widget.api.Color;

public class Drawings {
	public static void drawQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, Color color) {
		drawQuad(matrices, provider, layer, x, y, sX, sY, 0x00f000f0, color);
	}

	public static void drawQuad(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float sX, float sY, Color color) {
		drawQuad(matrices, provider, Layers.getDefault(), x, y, sX, sY, 0x00f000f0, color);
	}

	public static void drawQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, int light, Color color) {
		VertexConsumer consumer = provider.getBuffer(layer);

		consumer.vertex(matrices.peek().getModel(), x, y, 0).color(color.R, color.G, color.B, color.A).light(light).next();
		consumer.vertex(matrices.peek().getModel(), x, y + sY, 0).color(color.R, color.G, color.B, color.A).light(light).next();
		consumer.vertex(matrices.peek().getModel(), x + sX, y + sY, 0).color(color.R, color.G, color.B, color.A).light(light).next();
		consumer.vertex(matrices.peek().getModel(), x + sX, y, 0).color(color.R, color.G, color.B, color.A).light(light).next();
	}

	public static void drawGradientQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float startX, float startY, float endX, float endY, Color colorStart, Color colorEnd) {
		drawGradientQuad(matrices, provider, layer, startX, startY, endX, endY, 0, 0, 1, 1, 0x00f000f0, colorStart, colorEnd, false);
	}

	public static void drawGradientQuad(MatrixStack matrices, VertexConsumerProvider provider, float startX, float startY, float endX, float endY, Color colorStart, Color colorEnd) {
		drawGradientQuad(matrices, provider, Layers.getDefault(), startX, startY, endX, endY, 0, 0, 1, 1, 0x00f000f0, colorStart, colorEnd, false);
	}

	public static void drawGradientQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float startX, float startY, float endX, float endY, int light, Color colorStart, Color colorEnd) {
		drawGradientQuad(matrices, provider, layer, startX, startY, endX, endY, 0, 0, 1, 1, light, colorStart, colorEnd, false);
	}

	public static void drawGradientQuad(MatrixStack matrices, VertexConsumerProvider provider, float startX, float startY, float endX, float endY, int light, Color colorStart, Color colorEnd) {
		drawGradientQuad(matrices, provider, Layers.getDefault(), startX, startY, endX, endY, 0, 0, 1, 1, light, colorStart, colorEnd, false);
	}

	public static void drawGradientQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float startX, float startY, float endX, float endY, float uS, float vS, float uE, float vE, int light, Color colorStart, Color colorEnd, boolean textured) {
		VertexConsumer consumer = provider.getBuffer(layer);

		consumer.vertex(matrices.peek().getModel(), endX, startY, 0).color(colorStart.R, colorStart.G, colorStart.B, colorStart.A).texture(uS, vS).light(light).normal(matrices.peek().getNormal(), 0, 1, 0).next();
		consumer.vertex(matrices.peek().getModel(), startX, startY, 0).color(colorStart.R, colorStart.G, colorStart.B, colorStart.A).texture(uS, vE).light(light).normal(matrices.peek().getNormal(), 0, 1, 0).next();
		consumer.vertex(matrices.peek().getModel(), startX, endY, 0).color(colorEnd.R, colorEnd.G, colorEnd.B, colorEnd.A).texture(uE, vS).light(light).normal(matrices.peek().getNormal(), 0, 1, 0).next();
		consumer.vertex(matrices.peek().getModel(), endX, endY, 0).color(colorEnd.R, colorEnd.G, colorEnd.B, colorEnd.A).texture(uE, vE).light(light).normal(matrices.peek().getNormal(), 0, 1, 0).next();
	}

	public static void drawBeveledPanel(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, Color topleft, Color panel, Color bottomright) {
		drawBeveledPanel(matrices, provider, layer, x, y, sX, sY, 0x00f000f0, topleft, panel, bottomright);
	}

	public static void drawBeveledPanel(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float sX, float sY, Color topleft, Color panel, Color bottomright) {
		drawBeveledPanel(matrices, provider, Layers.getDefault(), x, y, sX, sY, 0x00f000f0, topleft, panel, bottomright);
	}

	public static void drawBeveledPanel(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, int light, Color topleft, Color panel, Color bottomright) {
		drawQuad(matrices, provider, layer, x, y, sX, sY, light, panel);
		drawQuad(matrices, provider, layer, x, y, sX, 1, light, topleft);
		drawQuad(matrices, provider, layer, x, y + 1, 1, sY - 1, light, topleft);
		drawQuad(matrices, provider, layer, x + sX - 1, y + 1, 1, sY - 1, light, bottomright);
		drawQuad(matrices, provider, layer, x, y + sY - 1, sX - 1, 1, light, bottomright);
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, Identifier texture) {
		drawTexturedQuad(matrices, provider, layer, x, y, sX, sY, 0, 0, 1, 1, 0x00f000f0, Color.DEFAULT, texture);
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float sX, float sY, Identifier texture) {
		drawTexturedQuad(matrices, provider, Layers.get(texture), x, y, sX, sY, 0, 0, 1, 1, 0x00f000f0, Color.DEFAULT, texture);
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, Color color, Identifier texture) {
		drawTexturedQuad(matrices, provider, layer, x, y, sX, sY, 0, 0, 1, 1, 0x00f000f0, color, texture);
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float sX, float sY, Color color, Identifier texture) {
		drawTexturedQuad(matrices, provider, Layers.get(texture), x, y, sX, sY, 0, 0, 1, 1, 0x00f000f0, color, texture);
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, int light, Color color, Identifier texture) {
		drawTexturedQuad(matrices, provider, layer, x, y, sX, sY, 0, 0, 1, 1, light, color, texture);
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float sX, float sY, int light, Color color, Identifier texture) {
		drawTexturedQuad(matrices, provider, Layers.get(texture), x, y, sX, sY, 0, 0, 1, 1, light, color, texture);
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float sX, float sY, float u0, float v0, float u1, float v1, int light, Color color, Identifier texture) {
		drawTexturedQuad(matrices, provider, Layers.get(texture), x, y, sX, sY, u0, v0, u1, v1, light, color, texture);
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float sX, float sY, float u0, float v0, float u1, float v1, int light, Color color, Identifier texture) {
		getTextureManager().bindTexture(texture);

		VertexConsumer consumer = provider.getBuffer(layer);

		consumer.vertex(matrices.peek().getModel(), x, y + sY, 0).color(color.R, color.G, color.B, color.A).texture(u0, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrices.peek().getNormal(), 0, 0, 0).next();
		consumer.vertex(matrices.peek().getModel(), x + sX, y + sY, 0).color(color.R, color.G, color.B, color.A).texture(u1, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrices.peek().getNormal(), 0, 0, 0).next();
		consumer.vertex(matrices.peek().getModel(), x + sX, y, 0).color(color.R, color.G, color.B, color.A).texture(u1, v0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrices.peek().getNormal(), 0, 0, 0).next();
		consumer.vertex(matrices.peek().getModel(), x, y, 0).color(color.R, color.G, color.B, color.A).texture(u0, v0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrices.peek().getNormal(), 0, 0, 0).next();
	}

	public static ItemRenderer getItemRenderer() {
		return MinecraftClient.getInstance().getItemRenderer();
	}

	public static TextRenderer getTextRenderer() {
		return MinecraftClient.getInstance().textRenderer;
	}

	public static TextureManager getTextureManager() {
		return MinecraftClient.getInstance().getTextureManager();
	}
}

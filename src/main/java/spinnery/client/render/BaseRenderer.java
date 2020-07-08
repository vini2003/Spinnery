package spinnery.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import spinnery.access.ItemRendererAccessor;
import spinnery.access.TextRendererAccessor;
import spinnery.client.render.layer.SpinneryLayers;
import spinnery.widget.api.Color;

public class BaseRenderer {
	private static AdvancedItemRenderer advancedItemRenderer = new AdvancedItemRenderer();
	private static AdvancedTextRenderer advancedTextRenderer = new AdvancedTextRenderer();

	public static void drawQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float z, float sX, float sY, Color color) {
		drawQuad(matrices, provider, layer, x, y, z, sX, sY, 0x00f000f0, color);
	}

	public static void drawQuad(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float z, float sX, float sY, Color color) {
		drawQuad(matrices, provider, SpinneryLayers.getFlat(), x, y, z, sX, sY, 0x00f000f0, color);
	}

	public static void drawQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float z, float sX, float sY, int light, Color color) {
		VertexConsumer consumer = provider.getBuffer(layer);

		consumer.vertex(matrices.peek().getModel(), x, y, z).color(color.R, color.G, color.B, color.A).light(light).next();
		consumer.vertex(matrices.peek().getModel(), x, y + sY, z).color(color.R, color.G, color.B, color.A).light(light).next();
		consumer.vertex(matrices.peek().getModel(), x + sX, y + sY, z).color(color.R, color.G, color.B, color.A).light(light).next();
		consumer.vertex(matrices.peek().getModel(), x + sX, y, z).color(color.R, color.G, color.B, color.A).light(light).next();
	}

	public static void drawGradientQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float startX, float startY, float endX, float endY, float z, Color colorStart, Color colorEnd) {
		drawGradientQuad(matrices, provider, layer, startX, startY, endX, endY, z, 0, 0, 1, 1, 0x00f000f0, colorStart, colorEnd, false);
	}

	public static void drawGradientQuad(MatrixStack matrices, VertexConsumerProvider provider, float startX, float startY, float endX, float endY, float z, Color colorStart, Color colorEnd) {
		drawGradientQuad(matrices, provider, SpinneryLayers.getFlat(), startX, startY, endX, endY, z, 0, 0, 1, 1, 0x00f000f0, colorStart, colorEnd, false);
	}

	public static void drawGradientQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float startX, float startY, float endX, float endY, float z, int light, Color colorStart, Color colorEnd) {
		drawGradientQuad(matrices, provider, layer, startX, startY, endX, endY, z, 0, 0, 1, 1, light, colorStart, colorEnd, false);
	}

	public static void drawGradientQuad(MatrixStack matrices, VertexConsumerProvider provider, float startX, float startY, float endX, float endY, float z, int light, Color colorStart, Color colorEnd) {
		drawGradientQuad(matrices, provider, SpinneryLayers.getFlat(), startX, startY, endX, endY, z, 0, 0, 1, 1, light, colorStart, colorEnd, false);
	}

	public static void drawGradientQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float startX, float startY, float endX, float endY, float z, float uS, float vS, float uE, float vE, int light, Color colorStart, Color colorEnd, boolean textured) {
		if (!textured) RenderSystem.disableTexture();

		matrices.push();

		VertexConsumer consumer = provider.getBuffer(layer);

		consumer.vertex(matrices.peek().getModel(), endX, startY, z + 201).color(colorStart.R, colorStart.G, colorStart.B, colorStart.A).texture(uS, vS).light(light).normal(matrices.peek().getNormal(), 0, 1, 0).next();
		consumer.vertex(matrices.peek().getModel(), startX, startY, z + 201).color(colorStart.R, colorStart.G, colorStart.B, colorStart.A).texture(uS, vE).light(light).normal(matrices.peek().getNormal(), 0, 1, 0).next();
		consumer.vertex(matrices.peek().getModel(), startX, endY, z + 201).color(colorEnd.R, colorEnd.G, colorEnd.B, colorEnd.A).texture(uE, vS).light(light).normal(matrices.peek().getNormal(), 0, 1, 0).next();
		consumer.vertex(matrices.peek().getModel(), endX, endY, z + 201).color(colorEnd.R, colorEnd.G, colorEnd.B, colorEnd.A).texture(uE, vE).light(light).normal(matrices.peek().getNormal(), 0, 1, 0).next();

		if (!textured) RenderSystem.enableTexture();

		matrices.pop();
	}

	public static void drawTooltip(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float width, float height, Color shadowStart, Color shadowEnd, Color backgroundStart, Color backgroundEnd, Color outlineStart, Color outlineEnd) {
		drawTooltip(matrices, provider, SpinneryLayers.getTooltip(), x, y, 256F, width, height, shadowStart, shadowEnd, backgroundStart, backgroundEnd, outlineStart, outlineEnd);
	}

	public static void drawTooltip(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float z, float width, float height, Color shadowStart, Color shadowEnd, Color backgroundStart, Color backgroundEnd, Color outlineStart, Color outlineEnd) {
		drawGradientQuad(matrices, provider, layer, x - 3, y - 4, x + width + 3, y - 3, z, shadowStart, shadowStart); // Border - top
		drawGradientQuad(matrices, provider, layer, x - 3, y + height + 3, x + width + 3, y + height + 4, z, shadowEnd, shadowEnd); // Border - bottom
		drawGradientQuad(matrices, provider, layer, x - 3, y - 3, x + width + 3, y + height + 3, z, backgroundStart, backgroundEnd); // Body
		drawGradientQuad(matrices, provider, layer, x - 4, y - 3, x - 3, y + height + 3, z, shadowStart, shadowEnd); // Border - left
		drawGradientQuad(matrices, provider, layer, x + width + 3, y - 3, x + width + 4, y + height + 3, z, shadowStart, shadowEnd); // Border - right

		drawGradientQuad(matrices, provider, layer, x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, z, outlineStart, outlineEnd); // Outline - left
		drawGradientQuad(matrices, provider, layer, x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, z, outlineStart, outlineEnd); // Outline - right
		drawGradientQuad(matrices, provider, layer, x - 3, y - 3, x + width + 3, y - 3 + 1, z, outlineStart, outlineStart); // Outline - top
		drawGradientQuad(matrices, provider, layer, x - 3, y + height + 2, x + width + 3, y + height + 3, z, outlineEnd, outlineEnd); // Outline - bottom
	}

	public static void drawPanel(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float z, float sX, float sY, Color shadow, Color panel, Color hilight, Color outline) {
		drawPanel(matrices, provider, SpinneryLayers.getFlat(), x, y, z, sX, sY, shadow, panel, hilight, outline);
	}

	public static void drawPanel(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float z, float sX, float sY, Color shadow, Color panel, Color hilight, Color outline) {
		drawQuad(matrices, provider, layer, x + 3, y + 3, z, sX - 6, sY - 6, 0x00f000f0, panel);

		drawQuad(matrices, provider, layer, x + 2, y + 1, z, sX - 4, 2, 0x00f000f0, hilight);
		drawQuad(matrices, provider, layer, x + 2, y + sY - 3, z, sX - 4, 2, 0x00f000f0, shadow);
		drawQuad(matrices, provider, layer, x + 1, y + 2, z, 2, sY - 4, 0x00f000f0, hilight);
		drawQuad(matrices, provider, layer, x + sX - 3, y + 2, z, 2, sY - 4, 0x00f000f0, shadow);
		drawQuad(matrices, provider, layer, x + sX - 3, y + 2, z, 1, 1, 0x00f000f0, panel);
		drawQuad(matrices, provider, layer, x + 2, y + sY - 3, z, 1, 1, 0x00f000f0, panel);
		drawQuad(matrices, provider, layer, x + 3, y + 3, z, 1, 1, 0x00f000f0, hilight);
		drawQuad(matrices, provider, layer, x + sX - 4, y + sY - 4, z, 1, 1, 0x00f000f0, shadow);

		drawQuad(matrices, provider, layer, x + 2, y, z, sX - 4, 1, 0x00f000f0, outline);
		drawQuad(matrices, provider, layer, x, y + 2, z, 1, sY - 4, 0x00f000f0, outline);
		drawQuad(matrices, provider, layer, x + sX - 1, y + 2, z, 1, sY - 4, 0x00f000f0, outline);
		drawQuad(matrices, provider, layer, x + 2, y + sY - 1, z, sX - 4, 1, 0x00f000f0, outline);
		drawQuad(matrices, provider, layer, x + 1, y + 1, z, 1, 1, 0x00f000f0, outline);
		drawQuad(matrices, provider, layer, x + 1, y + sY - 2, z, 1, 1, 0x00f000f0, outline);
		drawQuad(matrices, provider, layer, x + sX - 2, y + 1, z, 1, 1, 0x00f000f0, outline);
		drawQuad(matrices, provider, layer, x + sX - 2, y + sY - 2, z, 1, 1, 0x00f000f0, outline);
	}

	public static void drawBeveledPanel(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float z, float sX, float sY, Color topleft, Color panel, Color bottomright) {
		drawBeveledPanel(matrices, provider, layer, x, y, z, sX, sY, 0x00f000f0, topleft, panel, bottomright);
	}

	public static void drawBeveledPanel(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float z, float sX, float sY, Color topleft, Color panel, Color bottomright) {
		drawBeveledPanel(matrices, provider, SpinneryLayers.getFlat(), x, y, z, sX, sY, 0x00f000f0, topleft, panel, bottomright);
	}

	public static void drawBeveledPanel(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float z, float sX, float sY, int light, Color topleft, Color panel, Color bottomright) {
		drawQuad(matrices, provider, layer, x, y, z, sX, sY, light, panel);
		drawQuad(matrices, provider, layer, x, y, z, sX, 1, light, topleft);
		drawQuad(matrices, provider, layer, x, y + 1, z, 1, sY - 1, light, topleft);
		drawQuad(matrices, provider, layer, x + sX - 1, y + 1, z, 1, sY - 1, light, bottomright);
		drawQuad(matrices, provider, layer, x, y + sY - 1, z, sX - 1, 1, light, bottomright);
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float z, float sX, float sY, Identifier texture) {
		drawTexturedQuad(matrices, provider, layer, x, y, z, sX, sY, 0, 0, 1, 1, 0x00f000f0, Color.DEFAULT, texture);
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float z, float sX, float sY, Identifier texture) {
		drawTexturedQuad(matrices, provider, SpinneryLayers.getText(texture), x, y, z, sX, sY, 0, 0, 1, 1, 0x00f000f0, Color.DEFAULT, texture);
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float z, float sX, float sY, Color color, Identifier texture) {
		drawTexturedQuad(matrices, provider, layer, x, y, z, sX, sY, 0, 0, 1, 1, 0x00f000f0, color, texture);
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float z, float sX, float sY, Color color, Identifier texture) {
		drawTexturedQuad(matrices, provider, SpinneryLayers.getText(texture), x, y, z, sX, sY, 0, 0, 1, 1, 0x00f000f0, color, texture);
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float z, float sX, float sY, int light, Color color, Identifier texture) {
		drawTexturedQuad(matrices, provider, layer, x, y, z, sX, sY, 0, 0, 1, 1, light, color, texture);
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float z, float sX, float sY, int light, Color color, Identifier texture) {
		drawTexturedQuad(matrices, provider, SpinneryLayers.getText(texture), x, y, z, sX, sY, 0, 0, 1, 1, light, color, texture);
	}

	public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float z, float sX, float sY, float u0, float v0, float u1, float v1, int light, Color color, Identifier texture) {
		drawTexturedQuad(matrices, provider, SpinneryLayers.get(texture), x, y, z, sX, sY, u0, v0, u1, v1, light, color, texture);
	}

		public static void drawTexturedQuad(MatrixStack matrices, VertexConsumerProvider provider, RenderLayer layer, float x, float y, float z, float sX, float sY, float u0, float v0, float u1, float v1, int light, Color color, Identifier texture) {
		getTextureManager().bindTexture(texture);

		VertexConsumer consumer = provider.getBuffer(layer);

		matrices.push();

		consumer.vertex(matrices.peek().getModel(), x, y + sY, z).color(color.R, color.G, color.B, color.A).texture(u0, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrices.peek().getNormal(), 0, 0, 0).next();
		consumer.vertex(matrices.peek().getModel(), x + sX, y + sY, z).color(color.R, color.G, color.B, color.A).texture(u1, v1).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrices.peek().getNormal(), 0, 0, 0).next();
		consumer.vertex(matrices.peek().getModel(), x + sX, y, z).color(color.R, color.G, color.B, color.A).texture(u1, v0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrices.peek().getNormal(), 0, 0, 0).next();
		consumer.vertex(matrices.peek().getModel(), x, y, z).color(color.R, color.G, color.B, color.A).texture(u0, v0).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrices.peek().getNormal(), 0, 0, 0).next();

		matrices.pop();
	}

	@Deprecated
	public static ItemRenderer getItemRenderer() {
		return getDefaultItemRenderer();
	}

	@Deprecated
	public static TextRenderer getTextRenderer() {
		return getDefaultTextRenderer();
	}

	public static TextureManager getTextureManager() {
		return MinecraftClient.getInstance().getTextureManager();
	}

	public static ItemRenderer getDefaultItemRenderer() {
		return MinecraftClient.getInstance().getItemRenderer();
	}

	public static TextRenderer getDefaultTextRenderer() {
		return MinecraftClient.getInstance().textRenderer;
	}

	public static AdvancedItemRenderer getAdvancedItemRenderer() {
		ItemRenderer defaultRenderer = getDefaultItemRenderer();

		advancedItemRenderer.setColorMap(((ItemRendererAccessor) defaultRenderer).spinner_getColorMap());
		advancedItemRenderer.setModels(((ItemRendererAccessor) defaultRenderer).spinnery_getModels());
		advancedItemRenderer.setTextureManager(((ItemRendererAccessor) defaultRenderer).spinnery_getTextureManager());

		return advancedItemRenderer;
	}

	public static AdvancedTextRenderer getAdvancedTextRenderer() {
		TextRenderer defaultRenderer = getDefaultTextRenderer();

		advancedTextRenderer.setFontStorageAccessor(((TextRendererAccessor) defaultRenderer).spinnery_getStorageAccessor());
		advancedTextRenderer.setHandler(((TextRendererAccessor) defaultRenderer).spinnery_getTextHandler());

		return advancedTextRenderer;
	}
}

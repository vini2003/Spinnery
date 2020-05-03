package com.github.vini2003.spinnery.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import com.github.vini2003.spinnery.widget.api.Color;
import com.github.vini2003.spinnery.widget.api.WLayoutElement;

public class BaseRenderer {
	public static void drawPanel(double x, double y, double z, double sX, double sY, Color shadow, Color panel, Color hilight, Color outline) {
		drawRectangle(x + 3, y + 3, z, sX - 6, sY - 6, panel);

		drawRectangle(x + 2, y + 1, z, sX - 4, 2, hilight);
		drawRectangle(x + 2, y + sY - 3, z, sX - 4, 2, shadow);
		drawRectangle(x + 1, y + 2, z, 2, sY - 4, hilight);
		drawRectangle(x + sX - 3, y + 2, z, 2, sY - 4, shadow);
		drawRectangle(x + sX - 3, y + 2, z, 1, 1, panel);
		drawRectangle(x + 2, y + sY - 3, z, 1, 1, panel);
		drawRectangle(x + 3, y + 3, z, 1, 1, hilight);
		drawRectangle(x + sX - 4, y + sY - 4, z, 1, 1, shadow);

		drawRectangle(x + 2, y, z, sX - 4, 1, outline);
		drawRectangle(x, y + 2, z, 1, sY - 4, outline);
		drawRectangle(x + sX - 1, y + 2, z, 1, sY - 4, outline);
		drawRectangle(x + 2, y + sY - 1, z, sX - 4, 1, outline);
		drawRectangle(x + 1, y + 1, z, 1, 1, outline);
		drawRectangle(x + 1, y + sY - 2, z, 1, 1, outline);
		drawRectangle(x + sX - 2, y + 1, z, 1, 1, outline);
		drawRectangle(x + sX - 2, y + sY - 2, z, 1, 1, outline);
	}

	public static void drawRectangle(double x, double y, double z, double sX, double sY, Color color) {
		RenderSystem.enableBlend();
		RenderSystem.disableTexture();
		RenderSystem.blendFuncSeparate(770, 771, 1, 0);
		RenderSystem.color4f(color.R, color.G, color.B, color.A);

		getBufferBuilder().begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);

		getBufferBuilder().pos(x, y, 0).endVertex();
		getBufferBuilder().pos(x, y + sY, 0).endVertex();
		getBufferBuilder().pos(x + sX, y, 0).endVertex();

		getBufferBuilder().pos(x, y + sY, 0).endVertex();
		getBufferBuilder().pos(x + sX, y + sY, 0).endVertex();
		getBufferBuilder().pos(x + sX, y, 0).endVertex();

		getTesselator().draw();

		RenderSystem.enableTexture();
		RenderSystem.disableBlend();
	}

	public static BufferBuilder getBufferBuilder() {
		return getTesselator().getBuffer();
	}

	public static Tessellator getTesselator() {
		return Tessellator.getInstance();
	}

	public static void drawGradient(double startX, double startY, double endX, double endY, double z, Color colorStart, Color colorEnd) {
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.disableAlphaTest();
		RenderSystem.defaultBlendFunc();
		RenderSystem.shadeModel(7425);
		getBufferBuilder().begin(7, DefaultVertexFormats.POSITION_COLOR);
		getBufferBuilder().pos(endX, startY, 0).color(colorStart.R, colorStart.G, colorStart.B, colorStart.A).endVertex();
		getBufferBuilder().pos(startX, startY, 0).color(colorStart.R, colorStart.G, colorStart.B, colorStart.A).endVertex();
		getBufferBuilder().pos(startX, endY, 0).color(colorEnd.R, colorEnd.G, colorEnd.B, colorEnd.A).endVertex();
		getBufferBuilder().pos(endX, endY, 0).color(colorEnd.R, colorEnd.G, colorEnd.B, colorEnd.A).endVertex();
		getTesselator().draw();
		RenderSystem.shadeModel(7424);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableTexture();
	}

	public static void drawBeveledPanel(double x, double y, double z, double sX, double sY, Color topleft, Color panel, Color bottomright) {
		drawRectangle(x, y, z, sX, sY, panel);
		drawRectangle(x, y, z, sX, 1, topleft);
		drawRectangle(x, y + 1, z, 1, sY - 1, topleft);
		drawRectangle(x + sX - 1, y + 1, z, 1, sY - 1, bottomright);
		drawRectangle(x, y + sY - 1, z, sX - 1, 1, bottomright);
	}

	public static void drawImage(double x, double y, double z, double sX, double sY, ResourceLocation texture) {
		getTextureManager().bindTexture(texture);

		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(770, 771, 1, 0);
		RenderSystem.color4f(255, 255, 255, 255);

		getBufferBuilder().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		getBufferBuilder().pos(x, y + sY, 0).tex(0, 1).endVertex();
		getBufferBuilder().pos(x + sX, y + sY, 0).tex(1, 1).endVertex();
		getBufferBuilder().pos(x + sX, y, 0).tex(1, 0).endVertex();
		getBufferBuilder().pos(x, y, 0).tex(0, 0).endVertex();

		getTesselator().draw();

		RenderSystem.disableBlend();
	}

	public static void drawPixel(double x, double y, double z, Color color) {
		drawRectangle(x, y, z, 1, 1, color);
	}

	public static void enableCropping() {
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
	}

	public static void disableCropping() {
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}

	public static void crop(int x1, int y1, int x2, int y2) {
		double h = Minecraft.getInstance().getMainWindow().getHeight();
		double s = Minecraft.getInstance().getMainWindow().getGuiScaleFactor();

		GL11.glScissor((int) (x1 * s), (int) (h - y2 * s), (int) ((x2 - x1) * s), (int) ((y1 - y2) * s * -1));
	}

	public static void crop(WLayoutElement element) {
		crop(element.getX(), element.getY(), element.getX() + element.getWidth(), element.getY() + element.getHeight());
	}

	public static TextureManager getTextureManager() {
		return Minecraft.getInstance().getTextureManager();
	}

	public static ItemRenderer getItemRenderer() {
		return Minecraft.getInstance().getItemRenderer();
	}

	public static FontRenderer getTextRenderer() {
		return Minecraft.getInstance().fontRenderer;
	}
}

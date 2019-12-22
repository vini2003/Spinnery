package spinnery.container.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class BaseRenderer {
	public static final int SHADOW_DEFAULT = 0xFF555555;
	public static final int PANEL_DEFAULT = 0xFFC6C6C6;
	public static final int HILIGHT_DEFUALT = 0xFFFFFFFF;
	public static final int OUTLINE_DEFAULT = 0xFF000000;

	public static void drawRectangle(double positionX, double positionY, double positionZ, double sizeX, double sizeY, int integerColor) {
		float A = (integerColor >> 24 & 255) / 255.0F;
		float R = (integerColor >> 16 & 255) / 255.0F;
		float G = (integerColor >> 8 & 255) / 255.0F;
		float B = (integerColor & 255) / 255.0F;

		GlStateManager.enableBlend();
		GlStateManager.disableTexture();
		GlStateManager.blendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color4f(R, G, B, A);

		getBufferBuilder().begin(GL11.GL_QUADS, VertexFormats.POSITION);

		getBufferBuilder().vertex(positionX,         positionY + sizeY, positionZ).next();
		getBufferBuilder().vertex(positionX + sizeX, positionY + sizeY, positionZ).next();
		getBufferBuilder().vertex(positionX + sizeX, positionY,         positionZ).next();
		getBufferBuilder().vertex(positionX,         positionY,         positionZ).next();

		getTesselator().draw();

		GlStateManager.enableTexture();
		GlStateManager.disableBlend();
	}

	public static void drawPanel(double positionX, double positionY, double positionZ, double sizeX, double sizeY, int shadow, int panel, int hilight, int outline) {
		drawRectangle(positionX + 3,         positionY + 3,          positionZ, sizeX - 6, sizeY - 6,  panel);

		drawRectangle(positionX + 2,         positionY + 1,          positionZ, sizeX - 4, 2,          hilight);
		drawRectangle(positionX + 2,         positionY + sizeY - 3,  positionZ, sizeX - 4, 2,          shadow);
		drawRectangle(positionX + 1,         positionY + 2,          positionZ, 2,         sizeY - 4,  hilight);
		drawRectangle(positionX + sizeX - 3, positionY + 2,          positionZ, 2,         sizeY - 4,  shadow);
		drawRectangle(positionX + sizeX - 3, positionY + 2,          positionZ, 1,         1,          panel);
		drawRectangle(positionX + 2,         positionY + sizeY - 3,  positionZ, 1,         1,          panel);
		drawRectangle(positionX + 3,         positionY + 3,          positionZ, 1,         1,          hilight);
		drawRectangle(positionX + sizeX - 4, positionY + sizeY - 4,  positionZ, 1,         1,          shadow);

		drawRectangle(positionX + 2,         positionY,              positionZ, sizeX - 4, 1,          outline);
		drawRectangle(positionX,             positionY + 2,          positionZ, 1,         sizeY - 4,  outline);
		drawRectangle(positionX + sizeX - 1, positionY + 2,          positionZ, 1,         sizeY - 4,  outline);
		drawRectangle(positionX + 2,         positionY + sizeY - 1,  positionZ, sizeX - 4, 1,          outline);
		drawRectangle(positionX + 1,         positionY + 1,          positionZ, 1,         1,          outline);
		drawRectangle(positionX + 1,         positionY + sizeY - 2,  positionZ, 1,         1,          outline);
		drawRectangle(positionX + sizeX - 2, positionY + 1,          positionZ, 1,         1,          outline);
		drawRectangle(positionX + sizeX - 2, positionY + sizeY - 2,  positionZ, 1,         1,          outline);
	}

	public static void drawBeveledPanel(double positionX, double positionY, double positionZ, double sizeX, double sizeY, int topleft, int panel, int bottomright) {
		drawRectangle(positionX,             positionY,             positionZ, sizeX,     sizeY,     panel);
		drawRectangle(positionX,             positionY,             positionZ, sizeX - 1, 1,         topleft);
		drawRectangle(positionX,             positionY + 1,         positionZ, 1,         sizeY - 2, topleft);
		drawRectangle(positionX + sizeX - 1, positionY + 1,         positionZ, 1,         sizeY - 1, bottomright);
		drawRectangle(positionX + 1,         positionY + sizeY - 1, positionZ, sizeX - 1, 1,         bottomright);
	}

	public static void drawSlot(double positionX, double positionY, double positionZ) {
		drawBeveledPanel(positionX, positionY, positionZ, 18, 18, 0xFF373737, 0xFF8b8b8b,0xFFFFFFFF);
	}

	public static void drawImage(double positionX, double positionY, double positionZ, double sizeX, double sizeY, Identifier texture) {
		getTextureManager().bindTexture(texture);

		GlStateManager.enableBlend();
		GlStateManager.blendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color4f(255, 255, 255, 255);

		getBufferBuilder().begin(GL11.GL_QUADS, VertexFormats.POSITION_UV);

		getBufferBuilder().vertex(positionX,         positionY + sizeY,  positionZ).texture(0, 1).next();
		getBufferBuilder().vertex(positionX + sizeX, positionY + sizeY,  positionZ).texture(1, 1).next();
		getBufferBuilder().vertex(positionX + sizeX, positionY,          positionZ).texture(1, 0).next();
		getBufferBuilder().vertex(positionX,         positionY,          positionZ).texture(0, 0).next();

		getTesselator().draw();

		GlStateManager.disableBlend();
	}

	public static TextRenderer getTextRenderer() {
		return MinecraftClient.getInstance().textRenderer;
	}

	public static Tessellator getTesselator() {
		return Tessellator.getInstance();
	}

	public static BufferBuilder getBufferBuilder() {
		return getTesselator().getBufferBuilder();
	}

	public static TextureManager getTextureManager() {
		return MinecraftClient.getInstance().getTextureManager();
	}
}

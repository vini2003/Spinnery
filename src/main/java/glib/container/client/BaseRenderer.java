package glib.container.client;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class BaseRenderer {
	public static ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
	public static TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

	public static final int SHADOW_DEFAULT = 0xFF555555;
	public static final int PANEL_DEFAULT = 0xFFC6C6C6;
	public static final int HILIGHT_DEFUALT = 0xFFFFFFFF;
	public static final int OUTLINE_DEFAULT = 0xFF000000;

	public static void drawRectangle(double positionX, double positionY, double positionZ, double sizeX, double sizeY, int integerColor) {
		float A = (integerColor >> 24 & 255) / 255.0F;
		float R = (integerColor >> 16 & 255) / 255.0F;
		float G = (integerColor >> 8 & 255) / 255.0F;
		float B = (integerColor & 255) / 255.0F;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBufferBuilder();

		GlStateManager.enableBlend();
		GlStateManager.disableTexture();
		GlStateManager.blendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color4f(R, G, B, A);

		bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION);

		bufferBuilder.vertex(positionX,         positionY + sizeY, positionZ).next();
		bufferBuilder.vertex(positionX + sizeX, positionY + sizeY, positionZ).next();
		bufferBuilder.vertex(positionX + sizeX, positionY,         positionZ).next();
		bufferBuilder.vertex(positionX,         positionY,         positionZ).next();

		tessellator.draw();

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
		drawBeveledPanel(positionX + 1, positionY - 2, positionZ, 18, 18, 0xFF373737, 0xFF8b8b8b,0xFFFFFFFF);
	}

	public static void drawImage(double positionX, double positionY, double positionZ, double sizeX, double sizeY, Identifier texture) {
		MinecraftClient.getInstance().getTextureManager().bindTexture(texture);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBufferBuilder();

		GlStateManager.enableBlend();
		GlStateManager.blendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color4f(255, 255, 255, 255);

		bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV);

		bufferBuilder.vertex(positionX,         positionY + sizeY,  positionZ).texture(0, 1).next();
		bufferBuilder.vertex(positionX + sizeX, positionY + sizeY,  positionZ).texture(1, 1).next();
		bufferBuilder.vertex(positionX + sizeX, positionY,          positionZ).texture(1, 0).next();
		bufferBuilder.vertex(positionX,         positionY,          positionZ).texture(0, 0).next();

		tessellator.draw();

		GlStateManager.disableBlend();
	}
}

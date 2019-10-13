package electron.container.client;

import com.mojang.blaze3d.platform.GlStateManager;
import electron.container.common.BaseContainer;
import electron.container.common.widget.ItemSlot;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class BaseRenderer {
	public static ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();

	public static void drawRectangle(double x1, double y1, double x2, double y2, int integerColor) {
		float A = (integerColor >> 24 & 255) / 255.0F;
		float R = (integerColor >> 16 & 255) / 255.0F;
		float G = (integerColor >> 8 & 255) / 255.0F;
		float B = (integerColor & 255) / 255.0F;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBufferBuilder();

		GlStateManager.enableBlend();
		GlStateManager.disableTexture();
		GlStateManager.blendFuncSeparate(GlStateManager.class_4534.SRC_ALPHA.value, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA.value, GlStateManager.class_4534.ONE.value, GlStateManager.class_4534.ZERO.value);
		GlStateManager.color4f(R, G, B, A);

		bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION);

		bufferBuilder.vertex(x1,      y1 + y2, 0).next();
		bufferBuilder.vertex(x1 + x2, y1 + y2, 0).next();
		bufferBuilder.vertex(x1 + x2, y1,      0).next();
		bufferBuilder.vertex(x1,      y1,      0).next();

		tessellator.draw();

		GlStateManager.enableTexture();
		GlStateManager.disableBlend();
	}

	public static void drawPanel(double x, double y, double width, double height, int shadow, int panel, int hilight, int outline) {
		shadow = 0xFF555555;
		panel = 0xFFC6C6C6;
		hilight = 0xFFFFFFFF;
		outline = 0xFF000000;

		drawRectangle(x + 3,         y + 3,          width - 6, height - 6, panel); //Main panel area

		drawRectangle(x + 2,         y + 1,          width - 4, 2,          hilight);
		drawRectangle(x + 2,         y + height - 3, width - 4, 2,          shadow);
		drawRectangle(x + 1,         y + 2,          2,         height - 4, hilight);
		drawRectangle(x + width - 3, y + 2,          2,         height - 4, shadow);
		drawRectangle(x + width - 3, y + 2,          1,         1,          panel);
		drawRectangle(x + 2,         y + height - 3, 1,         1,          panel);
		drawRectangle(x + 3,         y + 3,          1,         1,          hilight);
		drawRectangle(x + width - 4, y + height - 4, 1,         1,          shadow);

		drawRectangle(x + 2,     	    y,              width - 4, 1,          outline);
		drawRectangle(x,            	 y + 2,          1,         height - 4, outline);
		drawRectangle(x + width - 1, y + 2,          1,         height - 4, outline);
		drawRectangle(x + 2,         y + height - 1, width - 4, 1,          outline);
		drawRectangle(x + 1,         y + 1,          1,         1,          outline);
		drawRectangle(x + 1,         y + height - 2, 1,         1,          outline);
		drawRectangle(x + width - 2, y + 1,          1,         1,          outline);
		drawRectangle(x + width - 2, y + height - 2, 1,         1,          outline);
	}

	public static void drawBeveledPanel(double x, double y, double width, double height, int topleft, int panel, int bottomright) {
		drawRectangle(x,             y,              width,     height,     panel);
		drawRectangle(x,             y,              width - 1, 1,          topleft);
		drawRectangle(x,             y + 1,          1,         height - 2, topleft);
		drawRectangle(x + width - 1, y + 1,          1,         height - 1, bottomright);
		drawRectangle(x + 1,         y + height - 1, width - 1, 1,          bottomright);
	}

	public static void drawSlot(double x, double y) {
		drawBeveledPanel(x + 1, y - 2, 18, 18, 0xFF373737, 0xFF8b8b8b,0xFFFFFFFF);
	}

	public static void drawSprite(double x, double y, double width, double height, Identifier texture, float u1, float v1, float u2, float v2, int integerColor) {
		MinecraftClient.getInstance().getTextureManager().bindTexture(texture);

		float R = (integerColor >> 16 & 255) / 255.0F;
		float G = (integerColor >> 8 & 255) / 255.0F;
		float B = (integerColor & 255) / 255.0F;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBufferBuilder();

		GlStateManager.enableBlend();
		GlStateManager.blendFuncSeparate(GlStateManager.class_4534.SRC_ALPHA.value, GlStateManager.class_4534.ONE_MINUS_SRC_ALPHA.value, GlStateManager.class_4534.ONE.value, GlStateManager.class_4534.ZERO.value);
		GlStateManager.color4f(R, G, B, 1.0f);

		bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_UV);

		bufferBuilder.vertex(x,         y + height, 0).texture(u1, v2).next();
		bufferBuilder.vertex(x + width, y + height, 0).texture(u2, v2).next();
		bufferBuilder.vertex(x + width, y,          0).texture(u2, v1).next();
		bufferBuilder.vertex(x,         y,          0).texture(u1, v1).next();

		tessellator.draw();

		GlStateManager.disableBlend();
	}
}

package spinnery.client.texture;
		
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import spinnery.client.utilities.Drawings;
import spinnery.client.utilities.Layers;
import spinnery.widget.api.Color;

public class PartitionedTexture {
	private final Identifier texture;
	private final float originalWidth;
	private final float originalHeight;

	private final Part topLeft;
	private final Part topRight;
	private final Part bottomLeft;
	private final Part bottomRight;
	private final Part middleLeft;
	private final Part middleRight;
	private final Part middleTop;
	private final Part middleBottom;
	private final Part center;

	public PartitionedTexture(Identifier texture, float originalWidth, float originalHeight, float leftPadding, float rightPadding, float topPadding, float bottomPadding) {
		this.texture = texture;
		this.originalWidth = originalWidth;
		this.originalHeight = originalHeight;

		this.topLeft = new Part(0F, 0F, leftPadding, topPadding);
		this.topRight = new Part(1F - rightPadding, 0F, 1F, topPadding);
		this.bottomLeft = new Part(0F, 1F - bottomPadding, leftPadding, 1F);
		this.bottomRight = new Part(1F - rightPadding, 1F - bottomPadding, 1F, 1F);
		this.middleLeft = new Part(0F, topPadding, leftPadding, 1F - bottomPadding);
		this.middleRight = new Part(1F - rightPadding, topPadding, 1F, 1F - bottomPadding);
		this.middleTop = new Part(leftPadding, 0F, 1F - rightPadding, topPadding);
		this.middleBottom = new Part(leftPadding, 1F - bottomPadding, 1F - rightPadding, 1F);
		this.center = new Part(leftPadding, topPadding, 1F - rightPadding, 1F - bottomPadding);
	}
	
	public void draw(MatrixStack matrices, VertexConsumerProvider provider, float x, float y, float width, float height) {
		final float scaleWidth = width / originalWidth;
		final float scaleHeight = height / originalHeight;
;
		final float topLeftWidth = width * (topLeft.uE - topLeft.uS) / scaleWidth;
		final float topLeftHeight = height * (topLeft.vE - topLeft.vS) / scaleHeight;
;
		final float topRightWidth = width * (topRight.uE - topRight.uS) / scaleWidth;
		final float topRightHeight = height * (topRight.vE - topRight.vS) / scaleHeight;
;
;
		final float bottomLeftWidth = width * (bottomLeft.uE - bottomLeft.uS) / scaleWidth;
		final float bottomLeftHeight = height * (bottomLeft.vE - bottomLeft.vS) / scaleHeight;
;
		final float bottomRightWidth = width * (bottomRight.uE - bottomRight.uS) / scaleWidth;
		final float bottomRightHeight = height * (bottomRight.vE - bottomRight.vS) / scaleHeight;
;
;
		final float middleTopWidth = width * (middleTop.uE + middleTop.uS) - topLeftWidth - topRightWidth;
		final float middleTopHeight = height * (middleTop.vE - middleTop.vS) / scaleHeight;
;
		final float middleBottomWidth = width * (middleBottom.uE + middleBottom.uS) - bottomLeftWidth - bottomRightWidth;
		final float middleBottomHeight = height * (middleBottom.vE - middleBottom.vS) / scaleHeight;
;
;
		final float middleLeftWidth = width * (middleLeft.uE - middleLeft.uS) / scaleWidth;
		float middleLeftHeight = height * (middleLeft.vE + middleLeft.vS) - topLeftHeight - topRightHeight;
;
		final float middleRightWidth = width * (middleRight.uE - middleRight.uS) / scaleWidth;
		float middleRightHeight = height * (middleRight.vE + middleRight.vS) - topLeftHeight - topRightHeight;
;
;
		final float centerWidth = width * (center.uE + center.uS) - topLeftWidth - topRightWidth;
		float centerHeight = height * (center.vE + center.vS) - topLeftHeight - topRightHeight;
;
;
		final float heightMultiplier = height / (topLeftHeight + middleLeftHeight + bottomLeftHeight);

		middleLeftHeight *= heightMultiplier;
		middleRightHeight *= heightMultiplier;
		centerHeight *= heightMultiplier;

		final RenderLayer layer = Layers.get(texture);

		final Color color = Color.DEFAULT;

		Drawings.drawTexturedQuad(matrices, provider, layer, x, y, topLeftWidth, topLeftHeight, topLeft.uS(), topLeft.vS(), topLeft.uE(), topLeft.vE(), 0x00F000F0, color, texture);

		Drawings.drawTexturedQuad(matrices, provider, layer, x + topLeftWidth, y, middleTopWidth, middleTopHeight, middleTop.uS(), middleTop.vS(), middleTop.uE(), middleTop.vE(), 0x00F000F0, color, texture);

		Drawings.drawTexturedQuad(matrices, provider, layer, x + topLeftWidth + middleTopWidth, y, topRightWidth, topRightHeight, topRight.uS(), topRight.vS(), topRight.uE(), topRight.vE(), 0x00F000F0, color, texture);

		Drawings.drawTexturedQuad(matrices, provider, layer, x, y + topRightHeight, middleLeftWidth, middleLeftHeight, middleLeft.uS(), middleLeft.vS(), middleLeft.uE(), middleLeft.vE(), 0x00F000F0, color, texture);

		Drawings.drawTexturedQuad(matrices, provider, layer, x + middleLeftWidth + middleTopWidth, y + topRightHeight, middleRightWidth, middleRightHeight, middleRight .uS(), middleRight.vS(), middleRight.uE(), middleRight.vE(), 0x00F000F0, color, texture);

		Drawings.drawTexturedQuad(matrices, provider, layer, x + topLeftWidth, y + topLeftHeight, centerWidth, centerHeight, center.uS(), center.vS(), center.uE(), center.vE(), 0x00F000F0, color, texture);

		Drawings.drawTexturedQuad(matrices, provider, layer, x, y + centerHeight + topLeftHeight, bottomLeftWidth, bottomLeftHeight, bottomLeft.uS(), bottomLeft.vS(), bottomLeft.uE(), bottomLeft.vE(), 0x00F000F0, color, texture);

		Drawings.drawTexturedQuad(matrices, provider, layer, x + topLeftWidth, y + centerHeight + topLeftHeight, middleBottomWidth, middleBottomHeight, middleBottom.uS(), middleBottom.vS(), middleBottom.uE(), middleBottom.vE(), 0x00F000F0, color, texture);

		Drawings.drawTexturedQuad(matrices, provider, layer, x + topLeftWidth + middleBottomWidth, y + centerHeight + topLeftHeight, bottomRightWidth, bottomRightHeight, bottomRight.uS(), bottomRight.vS(), bottomRight.uE(), bottomRight.vE(), 0x00F000F0, color, texture);
	}

	public static class Part {
		private final float uS;
		private final float vS;
		private final float uE;
		private final float vE;

		public Part(float uS, float vS, float uE, float vE) {
			this.uS = uS;
			this.vS = vS;
			this.uE = uE;
			this.vE = vE;
		}

		public float uS() {
			return uS;
		}

		public float vS() {
			return vS;
		}

		public float uE() {
			return uE;
		}

		public float vE() {
			return vE;
		}
	}
}
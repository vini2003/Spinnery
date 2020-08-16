package spinnery.widget.implementation;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.client.texture.PartitionedTexture;

public class WPartitionedTexture extends WAbstractWidget {
	private PartitionedTexture texture;

	public PartitionedTexture getTexture() {
		return texture;
	}

	public void setTexture(PartitionedTexture texture) {
		this.texture = texture;
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		texture.draw(matrices, provider, getX(), getY(), getWidth(), getHeight());
	}
}

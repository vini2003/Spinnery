package spinnery.widget.implementation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import spinnery.Spinnery;
import spinnery.client.texture.PartitionedTexture;
import spinnery.client.utilities.Scissors;

@Environment(EnvType.CLIENT)
public class WHorizontalBar extends WAbstractBar {
	private final PartitionedTexture foreground = new PartitionedTexture(Spinnery.identifier("textures/widget/bar_foreground.png"), 18F, 18F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F);
	private final PartitionedTexture background = new PartitionedTexture(Spinnery.identifier("textures/widget/bar_background.png"), 18F, 18F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F, 0.05555555555555555556F);

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider provider) {
		if (isHidden()) {
			return;
		}

		float x = getX();
		float y = getY();

		float sX = getWidth();
		float sY = getHeight();

		float rawHeight = MinecraftClient.getInstance().getWindow().getHeight();
		float scale = (float) MinecraftClient.getInstance().getWindow().getScaleFactor();

		float sBGX = (int) (((sX / getLimit()) * getProgress()));

		Scissors area = new Scissors(provider, (int) (x * scale), (int) (rawHeight - ((y + sY) * scale)), (int) (sX * scale), (int) (sY * scale));

		background.draw(matrices, provider, x, y, sX, sY);

		area.destroy(provider);

		area = new Scissors(provider, (int) (x * scale), (int) (rawHeight - ((y + sY) * scale)), (int) (sBGX * scale), (int) (sY * scale));

		foreground.draw(matrices, provider, x, y, sX, sY);

		area.destroy(provider);
	}
}

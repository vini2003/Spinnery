package spinnery.widget.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

/**
 * Utility class representing a virtual layout element that has a parent, position and size, but no drawing
 * logic.
 */
@Environment(EnvType.CLIENT)
public class WVirtualArea implements WLayoutElement {
	public static final WVirtualArea SCREEN = new WVirtualArea(Position.origin(),
			Size.of(MinecraftClient.getInstance().getWindow().getScaledWidth(),
					MinecraftClient.getInstance().getWindow().getScaledHeight()));

	protected final WLayoutElement parent;
	protected final Position position;
	protected final Size size;

	public WVirtualArea(WLayoutElement parent, Position position, Size size) {
		this.parent = parent;
		this.position = position;
		this.size = size;
	}

	public WVirtualArea(Position position, Size size) {
		this.parent = null;
		this.position = position;
		this.size = size;
	}

	@Override
	public float getX() {
		return position.getX();
	}

	@Override
	public float getY() {
		return position.getY();
	}

	@Override
	public float getZ() {
		return position.getZ();
	}

	@Environment(EnvType.CLIENT)
	public void center() {
		centerX();
		centerY();
	}

	@Environment(EnvType.CLIENT)
	public void centerX() {
		float x;
		if (parent == null) {
			x = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f - getWidth() / 2f;
		} else {
			x = parent.getWidth() / 2f - getWidth() / 2f;
		}
		position.setX(x);
	}

	@Environment(EnvType.CLIENT)
	public void centerY() {
		float y;
		if (parent == null) {
			y = MinecraftClient.getInstance().getWindow().getScaledHeight() / 2f - getHeight() / 2f;
		} else {
			y = parent.getHeight() / 2f - getHeight() / 2f;
		}
		position.setY(y);
	}

	@Override
	public float getWidth() {
		return size.getWidth();
	}

	@Override
	public float getHeight() {
		return size.getHeight();
	}

	@Override
	public void draw(MatrixStack matrices, VertexConsumerProvider.Immediate provider) {
	}

	public boolean isWithinBounds(float x, float y) {
		return x >= position.getX() && x <= position.getX() + size.getWidth()
				&& y >= position.getY() && y <= position.getY() + size.getHeight();
	}
}

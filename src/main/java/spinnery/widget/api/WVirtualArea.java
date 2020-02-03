package spinnery.widget.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class WVirtualArea implements WPositioned, WSized {
	protected final WPosition position;
	protected final WSize size;

	public WVirtualArea(WPosition position, WSize size) {
		this.position = position;
		this.size = size;
	}

	@Override
	public int getX() {
		return position.getX();
	}

	@Override
	public int getY() {
		return position.getY();
	}

	@Override
	public int getZ() {
		return position.getZ();
	}

	public void center() {
		int x, y;
		if (position.getAnchor() == null) {
			x = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - getWidth() / 2;
			y = MinecraftClient.getInstance().getWindow().getScaledHeight() / 2 - getHeight() / 2;
		} else {
			x = position.getAnchor().getWidth() / 2 - getWidth() / 2;
			y = position.getAnchor().getHeight() / 2 - getHeight() / 2;
		}
		position.setX(x);
		position.setY(y);
	}

	@Override
	public int getWidth() {
		return size.getX();
	}

	@Override
	public int getHeight() {
		return size.getY();
	}
}

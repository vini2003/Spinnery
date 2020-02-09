package spinnery.widget.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class WVirtualArea implements WLayoutElement {
	public static final WVirtualArea SCREEN = new WVirtualArea(WPosition.origin(),
			WSize.of(MinecraftClient.getInstance().getWindow().getScaledWidth(),
					MinecraftClient.getInstance().getWindow().getScaledHeight()));

	protected final WLayoutElement parent;
	protected final WPosition position;
	protected final WSize size;

	public WVirtualArea(WLayoutElement parent, WPosition position, WSize size) {
		this.parent = parent;
		this.position = position;
		this.size = size;
	}

	public WVirtualArea(WPosition position, WSize size) {
		this.parent = null;
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

	@Environment(EnvType.CLIENT)
	public void center() {
		centerX();
		centerY();
	}

	@Environment(EnvType.CLIENT)
	public void centerX() {
		int x;
		if (parent == null) {
			x = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - getWidth() / 2;
		} else {
			x = parent.getWidth() / 2 - getWidth() / 2;
		}
		position.setX(x);
	}

	@Environment(EnvType.CLIENT)
	public void centerY() {
		int y;
		if (parent == null) {
			y = MinecraftClient.getInstance().getWindow().getScaledHeight() / 2 - getHeight() / 2;
		} else {
			y = parent.getHeight() / 2 - getHeight() / 2;
		}
		position.setY(y);
	}

	@Override
	public int getWidth() {
		return size.getWidth();
	}

	@Override
	public int getHeight() {
		return size.getHeight();
	}

	@Override
	public void draw() {
	}

	public boolean isWithinBounds(int x, int y) {
		return x >= position.getX() && x <= position.getX() + size.getWidth()
				&& y >= position.getY() && y <= position.getY() + size.getHeight();
	}
}

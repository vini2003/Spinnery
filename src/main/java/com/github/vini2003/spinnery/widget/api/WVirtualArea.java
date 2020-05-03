package com.github.vini2003.spinnery.widget.api;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;

/**
 * Utility class representing a virtual layout element that has a parent, position and size, but no drawing
 * logic.
 */
@OnlyIn(Dist.CLIENT)
public class WVirtualArea implements WLayoutElement {
	public static final WVirtualArea SCREEN = new WVirtualArea(Position.origin(),
			Size.of(Minecraft.getInstance().getMainWindow().getScaledWidth(),
					Minecraft.getInstance().getMainWindow().getScaledHeight()));

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

	@OnlyIn(Dist.CLIENT)
	public void center() {
		centerX();
		centerY();
	}

	@OnlyIn(Dist.CLIENT)
	public void centerX() {
		int x;
		if (parent == null) {
			x = Minecraft.getInstance().getMainWindow().getScaledWidth() / 2 - getWidth() / 2;
		} else {
			x = parent.getWidth() / 2 - getWidth() / 2;
		}
		position.setX(x);
	}

	@OnlyIn(Dist.CLIENT)
	public void centerY() {
		int y;
		if (parent == null) {
			y = Minecraft.getInstance().getMainWindow().getScaledHeight() / 2 - getHeight() / 2;
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

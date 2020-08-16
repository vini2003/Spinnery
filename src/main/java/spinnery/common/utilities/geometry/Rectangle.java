package spinnery.common.utilities.geometry;

import spinnery.common.utilities.miscellaneous.Position;
import spinnery.common.utilities.miscellaneous.Size;

class Rectangle {
	public static Rectangle empty() {
		return new Rectangle(Position.of(0F, 0F), Size.of(0F, 0F));
	}

	private final Position position;
	private final Size size;

	public Rectangle(Position position, Size size) {
		this.position = position;
		this.size = size;
	}

	public boolean isWithin(float x, float y) {
		return x > position.getX() && x < position.getX() + size.getWidth() && y > position.getY() && y < position.getY() + size.getHeight();
	}
}
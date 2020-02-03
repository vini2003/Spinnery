package spinnery.widget.api;

public interface WPositioned {
	int getX();
	int getY();
	int getZ();

	default WPosition offset(int x, int y, int z) {
		return WPosition.of(this, x, y, z);
	}
}

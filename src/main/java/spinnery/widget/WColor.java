package spinnery.widget;

public class WColor {
	public float A = 1.0f, R = 1.0f, G = 1.0f, B = 1.0f;

	public int RGB = 0x000000;

	public WColor(String ARGB) {
		if (ARGB.length() == 8) {
			R = Integer.decode("0x" + ARGB.substring(2, 4)) / 255f;
			G = Integer.decode("0x" + ARGB.substring(4, 6)) / 255f;
			B = Integer.decode("0x" + ARGB.substring(6, 8)) / 255f;
			RGB = Integer.decode(ARGB);
		} else if (ARGB.length() == 10) {
			A = Integer.decode("0x" + ARGB.substring(2, 4)) / 255f;
			R = Integer.decode("0x" + ARGB.substring(4, 6)) / 255f;
			G = Integer.decode("0x" + ARGB.substring(6, 8)) / 255f;
			B = Integer.decode("0x" + ARGB.substring(8, 10)) / 255f;
		}
	}

	public static WColor of(String ARGB) {
		return new WColor(ARGB);
	}
}

package spinnery.widget;

public class WColor {
	public float A = 1.0f, R = 1.0f, G = 1.0f, B = 1.0f;

	public long ARGB = 0xffffffff;
	public int RGB = 0xffffff;

	public WColor(String ARGB) {
		if (ARGB.length() == 8) {
			R = Integer.decode("0x" + ARGB.substring(2, 4)) / 255f;
			G = Integer.decode("0x" + ARGB.substring(4, 6)) / 255f;
			B = Integer.decode("0x" + ARGB.substring(6, 8)) / 255f;
			this.ARGB = Long.decode(ARGB);
			this.RGB = Integer.decode(ARGB);
		} else if (ARGB.length() == 10) {
			A = Integer.decode("0x" + ARGB.substring(2, 4)) / 255f;
			R = Integer.decode("0x" + ARGB.substring(4, 6)) / 255f;
			G = Integer.decode("0x" + ARGB.substring(6, 8)) / 255f;
			B = Integer.decode("0x" + ARGB.substring(8, 10)) / 255f;
			this.ARGB = Long.decode(ARGB);
			this.RGB = Integer.decode("0x" + ARGB.substring(4));
		}
	}

	public static WColor of(String ARGB) {
		return new WColor(ARGB);
	}

	public static WColor of(Number color) {
		return WColor.of("0x" + Integer.toHexString(color.intValue()));
	}
}

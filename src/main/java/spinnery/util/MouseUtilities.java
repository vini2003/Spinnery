package spinnery.util;

public class MouseUtilities {
	public static int mouseX = 0, mouseY = 0;
	public static long lastNanos = 0;
	public static final long nanoDelay = 25000000;

	public static long lastNanos() {
		return lastNanos;
	}

	public static void lastNanos(long lastNanos) {
		MouseUtilities.lastNanos = lastNanos;
	}

	public static long nanoDelay() {
		return 100000000;
	}
}

package spinnery.util;

public class MouseUtilities {
	public static final long nanoDelay = 150000000;
	public static int mouseX = 0, mouseY = 0;
	public static long lastNanos = 0;

	public static void lastNanos(long lastNanos) {
		MouseUtilities.lastNanos = lastNanos;
	}

	public static long nanoInterval() {
		return System.nanoTime() - lastNanos();
	}

	public static long lastNanos() {
		return lastNanos;
	}

	public static long nanoDelay() {
		return nanoDelay;
	}

	public static void nanoUpdate() {
		lastNanos = System.nanoTime();
	}
}

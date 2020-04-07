package spinnery.util;

public class MouseUtilities {
	public static final long nanoDelay = 150000000;
	public static int mouseX = 0, mouseY = 0;
	public static long lastNanos = 0;

	/**
	 * Retrieve the interval in nanoseconds since the last update.
	 * @return Interval in nanoseconds since the last update.
	 */
	public static long nanoInterval() {
		return System.nanoTime() - lastNanos();
	}

	/**
	 * Retrieve the last time, in nanoseconds, when update was called.
	 * @return The last time, in nanoseconds, when update was called.
	 */
	public static long lastNanos() {
		return lastNanos;
	}

	/**
	 * Retrieve the delay, in nanoseconds, used for calculations.
	 * @return The delay, in nanoseconds.
	 */
	public static long nanoDelay() {
		return nanoDelay;
	}

	/**
	 * Update the last time, in nanoseconds, when update was called.
	 */
	public static void nanoUpdate() {
		lastNanos = System.nanoTime();
	}
}

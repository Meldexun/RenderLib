package meldexun.renderlib.util;

public class SmoothSync {

	private static boolean initialised;
	private static long prevFrame;

	public static void sync(int fps) {
		if (!initialised) {
			initialised = true;
			prevFrame = System.nanoTime();
		}

		long nextFrame = prevFrame + 1_000_000_000 / fps;

		long sleep = (nextFrame - System.nanoTime() - 500_000L) / 1_000_000;
		if (sleep > 0) {
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				// ignore
			}
		}

		while (nextFrame - System.nanoTime() > 0) {
			// wait
		}

		prevFrame = System.nanoTime();
	}

}

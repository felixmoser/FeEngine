package de.fieben.feengine;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

class FeSurfaceThread extends Thread {
	private static final String LOG_TAG = FeSurfaceThread.class.getSimpleName();

	private boolean mRun = true;

	// TAI thread pausieren wenn surface weg?
	@Override
	public void run() {
		final SurfaceHolder holder = FeSurface.SURFACE.getHolder();
		long lastUpdate = System.currentTimeMillis();
		while (mRun) {
			final Canvas canvas = holder.lockCanvas();
			if (canvas != null) {
				final long currentSystemTime = System.currentTimeMillis();
				FeSurface.SURFACE.onUpdate(currentSystemTime - lastUpdate);
				FeSurface.SURFACE.onDraw(canvas);
				lastUpdate = currentSystemTime;
				holder.unlockCanvasAndPost(canvas);
			}
		}
		super.run();
	}

	public void end() {
		boolean joinSuccessful = false;
		mRun = false;
		while (!joinSuccessful) {
			try {
				join();
				joinSuccessful = true;
			} catch (final InterruptedException e) {
				Log.e(LOG_TAG, e.getMessage(), e);
			}
		}
	}
}
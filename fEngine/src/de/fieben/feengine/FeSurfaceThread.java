package de.fieben.feengine;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

final class FeSurfaceThread extends Thread {
	private static final String LOG_TAG = FeSurfaceThread.class.getSimpleName();

	private FeSurface mSurface;
	private boolean mRun = true;

	public FeSurfaceThread(final FeSurface surface) {
		mSurface = surface;
	}

	@Override
	public void run() {
		final SurfaceHolder holder = mSurface.getHolder();
		long lastUpdate = System.currentTimeMillis();
		while (mRun) {
			final Canvas canvas = holder.lockCanvas();
			if (canvas != null) {
				final long currentSystemTime = System.currentTimeMillis();
				mSurface.onUpdate(currentSystemTime - lastUpdate);
				mSurface.onDraw(canvas);
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
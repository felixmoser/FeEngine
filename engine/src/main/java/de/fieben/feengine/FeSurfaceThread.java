package de.fieben.feengine;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * This is the {@link FeSurface}s update and drawing loop. Once started it triggers {@link FeSurface#onDraw(Canvas)} and {@link FeSurface#onUpdate(long)} as often as possible until stopped.
 * 
 * @author Felix Moser - felix.ernesto.moser@googlemail.com
 */
class FeSurfaceThread extends Thread {
	private static final String LOG_TAG = FeSurfaceThread.class.getSimpleName();

	private final FeSurface mSurface;
	private boolean mRun = true;

	public FeSurfaceThread(FeSurface feSurface) {
		mSurface = feSurface;
	}

	// WIP thread pausieren wenn surface weg?
	@SuppressLint("WrongCall")
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
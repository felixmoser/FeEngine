package de.fieben.fengine.surface;

import android.graphics.Canvas;

public final class FeSurfaceThread extends Thread {
	private FeSurface mSurface;
	public boolean mRun = true;

	public FeSurfaceThread(final FeSurface surface) {
		mSurface = surface;
	}

	// TODO handle "screen off"
	@Override
	public void run() {
		Canvas canvas = null;
		long lastUpdate = System.currentTimeMillis();
		while (mRun) {
			try {
				canvas = mSurface.getHolder().lockCanvas();
				synchronized (mSurface.getHolder()) {
					if (canvas != null) {
						final long currentSystemTime = System
								.currentTimeMillis();
						mSurface.onUpdate(currentSystemTime - lastUpdate);
						mSurface.onDraw(canvas);
						lastUpdate = currentSystemTime;
					}
				}
			} finally {
				if (canvas != null) {
					mSurface.getHolder().unlockCanvasAndPost(canvas);
				}
			}
		}
		super.run();
	}
}
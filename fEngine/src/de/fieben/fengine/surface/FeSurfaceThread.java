package de.fieben.fengine.surface;

import android.graphics.Canvas;

public class FeSurfaceThread extends Thread {
	private FeSurface mSurface;
	public boolean mRun = true;
	private long mLastUpdate;
	private Canvas mCanvas;

	public FeSurfaceThread(final FeSurface surface) {
		mSurface = surface;
	}

	// TODO add frequenz
	@Override
	public void run() {
		while (mRun) {
			try {
				mCanvas = mSurface.getHolder().lockCanvas();
				synchronized (mSurface.getHolder()) {
					if (mCanvas != null) {
						mSurface.onDraw(mCanvas);
						mSurface.onUpdate(System.currentTimeMillis()
								- mLastUpdate);
						mLastUpdate = System.currentTimeMillis();
					}
				}
			} finally {
				if (mCanvas != null) {
					mSurface.getHolder().unlockCanvasAndPost(mCanvas);
				}
			}
		}
		super.run();
	}
}
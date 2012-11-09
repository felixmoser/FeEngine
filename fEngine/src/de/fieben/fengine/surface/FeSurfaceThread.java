package de.fieben.fengine.surface;

import android.graphics.Canvas;

public class FeSurfaceThread extends Thread {
	private FeSurface mSurface;

	public boolean mRun = true;
	private long mLastUpdate;
	private long mCurrentSystemTime;
	private Canvas mCanvas;

	public FeSurfaceThread(final FeSurface surface) {
		mSurface = surface;
	}

	// TODO add frequenz
	@Override
	public void run() {
		mLastUpdate = System.currentTimeMillis();
		while (mRun) {
			try {
				mCanvas = mSurface.getHolder().lockCanvas();
				synchronized (mSurface.getHolder()) {
					if (mCanvas != null) {
						mCurrentSystemTime = System.currentTimeMillis();
						mSurface.onDraw(mCanvas);
						mSurface.onUpdate(mCurrentSystemTime - mLastUpdate);
						mLastUpdate = mCurrentSystemTime;
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
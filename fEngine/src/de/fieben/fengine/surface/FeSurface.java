package de.fieben.fengine.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import de.fieben.fengine.surface.impl.FeRootElementImpl;

public class FeSurface extends SurfaceView implements SurfaceHolder.Callback {
	private final String LOG_TAG = FeSurface.class.getSimpleName();

	private FeSurfaceThread mSurfaceThread;
	private Paint mPaint;
	private FeRootElementImpl mRootElement;

	public FeSurface(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		mSurfaceThread = new FeSurfaceThread(this);
		setFocusable(true);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// TODO set backgroundcolor as attribute
		mRootElement = new FeRootElementImpl(Color.CYAN, 100);
	}

	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
		if (!mSurfaceThread.isAlive()) {
			mSurfaceThread = new FeSurfaceThread(this);
		}
		mSurfaceThread.start();
	}

	@Override
	public void surfaceChanged(final SurfaceHolder holder, final int format,
			final int width, final int height) {
		// TODO enable grid from outsite
		mRootElement.calculateGrid(width, height);
	}

	@Override
	public void surfaceDestroyed(final SurfaceHolder holder) {
		boolean joinSuccessful = false;
		mSurfaceThread.mRun = false;
		while (!joinSuccessful) {
			try {
				mSurfaceThread.join();
				joinSuccessful = true;
			} catch (final InterruptedException e) {
				Log.e(LOG_TAG, e.getMessage(), e);
			}
		}
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		synchronized (mRootElement) {
			mRootElement.draw(canvas, mPaint);
		}
	}

	protected void onUpdate(final long elapsedMillis) {
		synchronized (mRootElement) {
			mRootElement.update(elapsedMillis);
		}
	}

	public void addElement(final FeSurfaceElement element) {
		synchronized (mRootElement) {
			mRootElement.addChild(element);
		}
	}

	public void setScale(final float scaleX, final float scaleY) {
		mRootElement.setScale(scaleX, scaleY);
	}

	public void addTranslate(final float translateX, final float translateY) {
		mRootElement.addTranslate(translateX, translateY);
	}
}
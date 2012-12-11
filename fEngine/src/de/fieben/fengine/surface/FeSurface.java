package de.fieben.fengine.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import de.fieben.fengine.R;
import de.fieben.fengine.surface.impl.FeRootElementImpl;

public abstract class FeSurface extends SurfaceView implements
		SurfaceHolder.Callback {
	private final String LOG_TAG = FeSurface.class.getSimpleName();

	private FeSurfaceThread mSurfaceThread;
	private final Paint mPaint;
	private final FeRootElementImpl mRootElement;

	public FeSurface(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		mSurfaceThread = new FeSurfaceThread(this);
		setFocusable(true);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mRootElement = new FeRootElementImpl(getIntValue(attrs,
				R.string.xmlattr_backgroundRowCount, 0), getIntValue(attrs,
				R.string.xmlattr_backgroundColumnCount, 0));
		mRootElement.setVoidColor(getIntValue(attrs,
				R.string.xmlattr_voidColor, Color.BLACK));
		mRootElement.enableGrid(getIntValue(attrs,
				R.string.xmlattr_gridSpacing, 0));
		mRootElement.setGridColor(getIntValue(attrs,
				R.string.xmlattr_gridColor, Color.WHITE));
		setScrollable(getBooleanValue(attrs, R.string.xmlattr_scrollable, false));
	}

	// TODO getter/setter for all (xml)attributes

	private boolean getBooleanValue(final AttributeSet attrs, final int attrId,
			final boolean defaultValue) {
		return attrs.getAttributeBooleanValue(
				getResources().getString(R.string.xmlns_fengine),
				getResources().getString(attrId), defaultValue);
	}

	private int getIntValue(final AttributeSet attrs, final int attrId,
			final int defaultValue) {
		return attrs.getAttributeIntValue(
				getResources().getString(R.string.xmlns_fengine),
				getResources().getString(attrId), defaultValue);
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
		// TODO enable grid from attribute
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

	public void setScrollable(final boolean dragEnabled) {
		mDragEnabled = true;
	}

	private boolean mDragEnabled;
	// TODO rename
	private float mLastX;
	private float mLastY;

	// WIP impl tiled mode

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		// TODO zoom gesture
		if (mDragEnabled) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastX = event.getX();
				mLastY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				addTranslate(event.getX() - mLastX, event.getY() - mLastY);
				mLastX = event.getX();
				mLastY = event.getY();
				break;
			case MotionEvent.ACTION_UP:
				// TODO stop scrolling if tile border is reached
				addTranslate(event.getX() - mLastX, event.getY() - mLastY);
				break;
			}
			return true;
		}
		return false;
	}
}
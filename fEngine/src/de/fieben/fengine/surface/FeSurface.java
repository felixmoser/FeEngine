package de.fieben.fengine.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
	private boolean mScrollEnabled;
	private float mLastTouchX;
	private float mLastTouchY;

	public FeSurface(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		mSurfaceThread = new FeSurfaceThread(this);
		setFocusable(true);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		// WIP combine row and column attribute? constructor or method to set
		// tile mode?
		mRootElement = new FeRootElementImpl(getIntValue(attrs,
				R.string.xmlattr_backgroundRowCount, 0), getIntValue(attrs,
				R.string.xmlattr_backgroundColumnCount, 0));
		mRootElement.setVoidColor(getIntValue(attrs,
				R.string.xmlattr_voidColor, Color.BLACK));
		mScrollEnabled = getBooleanValue(attrs, R.string.xmlattr_scrollable,
				false);
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
		mRootElement.updateSurfaceSize(width, height);
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

		mPaint.setColor(Color.BLUE);
		mPaint.setTextSize(34);
		drawMultilineText(mFPS + "\n" + mRootElement.getDebugOutput(), 35, 50,
				canvas);
	}

	private Rect mBounds = new Rect();

	void drawMultilineText(final String str, final int x, final int y,
			final Canvas canvas) {
		int lineHeight = 0;
		int yoffset = 0;
		final String[] lines = str.split("\n");

		mPaint.getTextBounds("Ig", 0, 2, mBounds);
		lineHeight = (int) (mBounds.height() * 1.2);
		for (int i = 0; i < lines.length; ++i) {
			canvas.drawText(lines[i], x, y + yoffset, mPaint);
			yoffset = yoffset + lineHeight;
		}
	}

	protected void onUpdate(final long elapsedMillis) {
		synchronized (mRootElement) {
			mRootElement.update(elapsedMillis);
		}
		calculateFPS(elapsedMillis);
	}

	// WIP enable in debug mode
	String mFPS = "";
	private long[] mLastElapsed = new long[20];
	private int mElapsedIndex = 0;

	private void calculateFPS(final long elapsedMillis) {
		if (mElapsedIndex >= 20) {
			mElapsedIndex = 0;
		}
		mLastElapsed[mElapsedIndex++] = elapsedMillis;
		long averageFPS = 0;
		for (int i = 0; i < 20; i++) {
			averageFPS += mLastElapsed[i];
		}
		mFPS = "FPS: " + String.valueOf(20000 / averageFPS);
	}

	// WIP !!! impl. "layers"
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

	public void setScrollable(final boolean scrollEnabled) {
		mScrollEnabled = scrollEnabled;
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		// TODO zoom gesture
		if (mScrollEnabled) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastTouchX = event.getX();
				mLastTouchY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				mRootElement.addTranslate(event.getX() - mLastTouchX,
						event.getY() - mLastTouchY);
				mLastTouchX = event.getX();
				mLastTouchY = event.getY();
				break;
			case MotionEvent.ACTION_UP:
				// TODO stop scrolling if tile border is reached
				mRootElement.addTranslate(event.getX() - mLastTouchX,
						event.getY() - mLastTouchY);
				break;
			}
			return true;
		}
		return false;
	}
}
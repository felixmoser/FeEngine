package de.fieben.fengine.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import de.fieben.fengine.R;
import de.fieben.fengine.surface.FeSurfaceElement.FeSurfaceTouchable;
import de.fieben.fengine.surface.impl.FeRootElement;
import de.fieben.fengine.surface.impl.FeSurfaceMap;
import de.fieben.fengine.surface.impl.FeSurfaceTile;

public abstract class FeSurface extends SurfaceView implements
		SurfaceHolder.Callback {
	private final String LOG_TAG = FeSurface.class.getSimpleName();

	// TODO ist static hier ok?
	public static int WIDTH;
	public static int HEIGHT;
	public static float OFFSET_X;
	public static float OFFSET_Y;

	private FeSurfaceThread mSurfaceThread;
	private final Paint mPaint;
	private final FeRootElement mRootElement;

	public FeSurface(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		mSurfaceThread = new FeSurfaceThread(this);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		final int voidColor = getIntValue(attrs, R.string.xmlattr_voidColor,
				Color.BLACK);
		final boolean scrollEnabled = getBooleanValue(attrs,
				R.string.xmlattr_scrollable, false);
		mRootElement = new FeRootElement(voidColor, scrollEnabled);
		OFFSET_X = 0f;
		OFFSET_Y = 0f;
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
		WIDTH = width;
		HEIGHT = height;
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

	// TODO synchronized blocks überall prüfen - vl erklärung einholen
	@Override
	protected void onDraw(final Canvas canvas) {
		synchronized (mRootElement) {
			mRootElement.draw(canvas, mPaint);
		}

		mPaint.setColor(Color.MAGENTA);
		mPaint.setTextSize(34);
		String debugString = null;
		if (mMap != null) {
			debugString = mFPS + "\n" + mMap.getDebugOutput();
		} else {
			debugString = mFPS + " | " + mRootElement.getDebugOutput();
		}
		drawMultilineText(debugString, 35, 50, canvas);
	}

	protected void onUpdate(final long elapsedMillis) {
		synchronized (mRootElement) {
			mRootElement.update(elapsedMillis);
		}
		calculateFPS(elapsedMillis);
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		return mRootElement.onTouch(event);
	}

	public void registerTouchable(final FeSurfaceTouchable touchableElement) {
		mRootElement.registerTouchable(touchableElement);
	}

	public void addElement(final FeSurfaceElement element) {
		mRootElement.addChild(element);
	}

	public void addMap(final FeSurfaceMap.MapMode mode,
			final SparseArray<SparseArray<? extends FeSurfaceTile>> tiles) {
		mMap = new FeSurfaceMap(mode, tiles);
		mRootElement.addChild(FeSurfaceElement.BACKGROUND_LAYER, mMap);
	}

	public void addTranslate(final float translateX, final float translateY) {
		mRootElement.addTranslate(translateX, translateY);
	}

	public void setScale(final float scaleX, final float scaleY) {
		mRootElement.setScale(scaleX, scaleY);
	}

	public void setRotate(final float degrees) {
		mRootElement.setRotate(degrees);
	}

	// TODO getter/setter for all (xml)attributes?
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

	// DEBUG
	private String mFPS = "";
	private long[] mLastElapsed = new long[20];
	private int mElapsedIndex = 0;

	private FeSurfaceMap mMap = null;

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

	private void drawMultilineText(final String str, final int x, final int y,
			final Canvas canvas) {
		final Rect bounds = new Rect();
		int lineHeight = 0;
		int yoffset = 0;
		final String[] lines = str.split("\n");

		mPaint.getTextBounds("Ig", 0, 2, bounds);
		lineHeight = (int) (bounds.height() * 1.2);
		for (int i = 0; i < lines.length; ++i) {
			canvas.drawText(lines[i], x, y + yoffset, mPaint);
			yoffset = yoffset + lineHeight;
		}
	}
}
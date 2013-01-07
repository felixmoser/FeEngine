package de.fieben.feengine;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import de.fieben.feengine.FeSurfaceElement.FeSurfaceTouchable;

public abstract class FeSurface extends SurfaceView implements
		SurfaceHolder.Callback {

	public static enum MapMode {
		NORMAL, ISOMETRIC
	};

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

		final TypedArray styleAttrs = context.getTheme()
				.obtainStyledAttributes(attrs, R.styleable.FeSurface, 0, 0);
		try {
			final int voidColor = styleAttrs.getInteger(
					R.styleable.FeSurface_voidColor, Color.BLACK);
			final int touchMode = styleAttrs.getInt(
					R.styleable.FeSurface_touchMode, 0);
			mRootElement = new FeRootElement(voidColor, touchMode);
		} finally {
			styleAttrs.recycle();
		}
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
		mSurfaceThread.end();
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		mRootElement.draw(canvas, mPaint);

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
		mRootElement.update(elapsedMillis);
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
		mRootElement.addChild(FeRootElement.LAYER_ELEMENTS, element);
	}

	public void addElement(final int layerLevel, final FeSurfaceElement element) {
		mRootElement.addChild(layerLevel + FeRootElement.LAYER_ELEMENTS,
				element);
	}

	public void addMap(final MapMode mode,
			final SparseArray<SparseArray<? extends FeSurfaceTile>> tiles) {
		mMap = new FeSurfaceMap(mode, tiles);
		mRootElement.addChild(FeRootElement.LAYER_MAP, mMap);
	}

	// TODO support all add and set methods
	public void addTranslation(final float translationX,
			final float translationY) {
		mRootElement.addTranslation(translationX, translationY);
	}

	public void setScale(final float scaleX, final float scaleY) {
		mRootElement.setScale(scaleX, scaleY);
	}

	@Override
	public void setRotation(final float degrees) {
		mRootElement.setRotation(degrees);
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
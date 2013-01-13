package de.fieben.feengine;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class FeSurface extends SurfaceView implements
		SurfaceHolder.Callback {
	// TAI impl preparation thread and "loading" support
	// TAI to enable hw-acceleration use textureview instead

	// TAI impl isometric mode
	// public static enum MapMode {
	// NORMAL, ISOMETRIC
	// };

	private final static int LAYER_BACKGROUND = 0;
	private final static int LAYER_MAP = 1;
	private final static int LAYER_ELEMENTS = 2;

	static FeSurface SURFACE;
	private FeRootElement mRootElement;
	private FeSurfaceThread mSurfaceThread;

	private final Paint mPaint;
	private int mWidth;
	private int mHeight;

	public FeSurface(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);

		final TypedArray styleAttrs = context.getTheme()
				.obtainStyledAttributes(attrs, R.styleable.FeSurface, 0, 0);
		final int voidColor = styleAttrs.getInteger(
				R.styleable.FeSurface_voidColor, Color.BLACK);
		final int touchMode = styleAttrs.getInt(
				R.styleable.FeSurface_touchMode, 0);
		styleAttrs.recycle();

		SURFACE = this;
		mRootElement = new FeRootElement(voidColor, touchMode);

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
		startSurfaceThread();
	}

	private void startSurfaceThread() {
		if (mSurfaceThread == null || !mSurfaceThread.isAlive()) {
			mSurfaceThread = new FeSurfaceThread();
		}
		mSurfaceThread.start();
	}

	@Override
	public void surfaceChanged(final SurfaceHolder holder, final int format,
			final int width, final int height) {
		mWidth = width;
		mHeight = height;
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
		canvas.drawText(mFPS, 35, 50, mPaint);
	}

	protected void onUpdate(final long elapsedMillis) {
		mRootElement.update(elapsedMillis);

		calculateFPS(elapsedMillis);
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		return mRootElement.onTouch(event);
	}

	// TAI schöner machen? evtl mit flag @ addElement?
	public void registerTouchable(final FeSurfaceTouchable touchableElement) {
		mRootElement.registerTouchable(touchableElement);
	}

	public void addElement(final FeSurfaceElement element) {
		mRootElement.addChild(LAYER_ELEMENTS, element);
	}

	public void addElement(final int layerLevel, final FeSurfaceElement element) {
		mRootElement.addChild(layerLevel + LAYER_ELEMENTS, element);
	}

	/**
	 * Adds an {@link FeSurfaceElement} in the background. Behind all elements
	 * and even behind the map.
	 * 
	 * @param element
	 *            The background element to add.
	 */
	public void addBackgroundElement(final FeSurfaceElement element) {
		mRootElement.addChild(LAYER_BACKGROUND, element);
	}

	public void addMap(final FeSurfaceTile[][] tiles) {
		mRootElement.addChild(LAYER_MAP, new FeSurfaceMap(tiles));
	}

	public void setSurfaceTranslation(final float translateX,
			final float translateY) {
		mRootElement.setTranslate(translateX, translateY);
	}

	public void addSurfaceTranslation(final float translateX,
			final float translateY) {
		mRootElement.addTranslate(translateX, translateY);
	}

	public void setSurfaceScale(final float scaleX, final float scaleY,
			final float pointX, final float pointY) {
		mRootElement.setScale(scaleX, scaleY, pointX, pointY);
	}

	public void addSurfaceScale(final float scaleX, final float scaleY,
			final float pointX, final float pointY) {
		mRootElement.addScale(scaleX, scaleY, pointX, pointY);
	}

	public void setSurfaceRotation(final float degrees) {
		mRootElement.setRotate(degrees);
	}

	public void addSurfaceRotation(final float degrees) {
		mRootElement.addRotate(degrees);
	}

	public float getSurfaceTranslationX() {
		return mRootElement.getTranslateX();
	}

	public float getSurfaceTranslationY() {
		return mRootElement.getTranslateY();
	}

	public float getSurfaceScaleX() {
		return mRootElement.getScaleX();
	}

	public float getSurfaceScaleY() {
		return mRootElement.getScaleY();
	}

	public float getSurfaceRotationX() {
		return mRootElement.getTranslateX();
	}

	public float getSurfaceRotateionY() {
		return mRootElement.getTranslateY();
	}

	public int getSurfaceWidth() {
		return mWidth;
	}

	public int getSurfaceHeight() {
		return mHeight;
	}

	// DEBUG
	private String mFPS = "";
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
}
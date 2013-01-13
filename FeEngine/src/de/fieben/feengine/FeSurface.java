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

/**
 * This view provides methods to draw {@link FeSurfaceElement}s on a dedicated
 * drawing surface. It holds a scene graph with all elements to draw.
 * 
 * @author Felix Moser - felix.ernesto.moser@googlemail.com
 * 
 */
public abstract class FeSurface extends SurfaceView implements
		SurfaceHolder.Callback {

	private final static int LAYER_BACKGROUND = 0;
	private final static int LAYER_MAP = 1;
	private final static int LAYER_ELEMENTS = 2;

	static FeSurface SURFACE;
	private FeRootElement mRootElement;
	private FeSurfaceThread mSurfaceThread;

	protected final Paint mPaint;
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

	/**
	 * Draws the scene graph on the canvas. Implement to do custom drawing.
	 * 
	 * @param canvas
	 *            The canvas the scene graph gets drawn onto.
	 */
	@Override
	protected void onDraw(final Canvas canvas) {
		mRootElement.draw(canvas, mPaint);
	}

	/**
	 * Updates the scene graph.
	 * 
	 * @param elapsedMillis
	 *            The elapsed milliseconds since last call of this method.
	 */
	protected void onUpdate(final long elapsedMillis) {
		mRootElement.update(elapsedMillis);
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		return mRootElement.onTouch(event);
	}

	/**
	 * Adds an {@link FeSurfaceTouchable} to the list of elements that get
	 * notified about touch inputs.
	 * 
	 * @param touchableElement
	 *            The {@link FeSurfaceTouchable} to add.
	 */
	public void registerTouchable(final FeSurfaceTouchable touchableElement) {
		mRootElement.registerTouchable(touchableElement);
	}

	/**
	 * Adds an {@link FeSurfaceElement} as a child to the scene graphs root
	 * element on the element layer. Equivalent to
	 * {@link #addElement(layerLevel, element)} with layer 0.
	 * 
	 * @param element
	 *            The {@link FeSurfaceElement} to add.
	 */
	public void addElement(final FeSurfaceElement element) {
		mRootElement.addChild(LAYER_ELEMENTS, element);
	}

	/**
	 * Adds an {@link FeSurfaceElement} as a child to the scene graphs root
	 * element on the element layer plus layerLevel.
	 * 
	 * @param layerLevel
	 *            The layer level offset to the element layer. Needs to be >=0.
	 * @param element
	 *            The {@link FeSurfaceElement} to add.
	 */
	public void addElement(final int layerLevel, final FeSurfaceElement element) {
		mRootElement.addChild(layerLevel + LAYER_ELEMENTS, element);
	}

	/**
	 * Adds an {@link FeSurfaceElement} as a child to the scene graphs root
	 * element on the background layer. This element gets drawn even behind the
	 * map.
	 * 
	 * @param element
	 *            The {@link FeSurfaceElement} to add.
	 */
	public void addBackgroundElement(final FeSurfaceElement element) {
		mRootElement.addChild(LAYER_BACKGROUND, element);
	}

	/**
	 * Creates and adds a {@link FeSurfaceMap} to the scene graphs root element
	 * on the map layer.
	 * 
	 * @param tiles
	 *            The {@link FeSurfaceTile}s that gets drawn on the map. First
	 *            dimension indicates the row, second the column.
	 */
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
}
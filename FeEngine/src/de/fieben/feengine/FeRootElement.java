package de.fieben.feengine;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import de.fieben.feengine.FeSurfaceElement.FeSurfaceTouchable;

final class FeRootElement extends FeSurfaceElement implements
		FeSurfaceTouchable {

	public final static int LAYER_MAP = 0;
	public final static int LAYER_ELEMENTS = 1;

	// TAI add touch mode rotate
	public final static int TOUCH_NONE = 0;
	public final static int TOUCH_DRAG = 1;
	public final static int TOUCH_ZOOM = 2;

	private int mVoidColor;
	private int mTouchMode;
	private int mCurrentTouch;

	private float mLastTouchX;
	private float mLastTouchY;
	private float mTouchPointerLastDistance = 0f;

	private ArrayList<FeSurfaceTouchable> mTouchableElements = new ArrayList<FeSurfaceTouchable>();

	// WIP try to improve package struckture
	public FeRootElement(final int voidColor, final int touchMode) {
		// TODO -1 indicates infinity size and no reaction on touch, check if
		// nessesary or right.
		super(-1, -1);
		mVoidColor = voidColor;
		mTouchMode = touchMode;

		FeSurface.OFFSET_X = 0f;
		FeSurface.OFFSET_Y = 0f;
	}

	@Override
	public void onDraw(final Canvas canvas, final Paint paint) {
		canvas.drawColor(mVoidColor);
	}

	@Override
	public void onUpdate(final long elapsedMillis) {
	}

	@Override
	public void doUpdate() {
	}

	@Override
	public void setTranslation(final float translationX,
			final float translationY) {
		super.addTranslation(translationX, translationY);
		FeSurface.OFFSET_X = translationX;
		FeSurface.OFFSET_Y = translationY;
	}

	@Override
	public void addTranslation(final float translationX,
			final float translationY) {
		super.addTranslation(translationX, translationY);
		FeSurface.OFFSET_X += translationX;
		FeSurface.OFFSET_Y += translationY;
	}

	public void registerTouchable(final FeSurfaceTouchable touchableElement) {
		mTouchableElements.add(touchableElement);
	}

	// TAI stop scrolling if tile border is reached?
	@Override
	public boolean onTouch(final MotionEvent event) {
		// TODO prevent jumping after mode changes from zoom to drag (einfach
		// mit mTouchMode = TouchMode.NONE in MotionEvent.ACTION_POINTER_UP? das
		// verhindert allerdings wechsel zwischen zoom und drag)

		if (mCurrentTouch == TOUCH_NONE) {
			final MotionEvent offsetEvent = MotionEvent.obtain(event);
			offsetEvent.offsetLocation(-getTranslationX(), -getTranslationY());
			for (final FeSurfaceTouchable e : mTouchableElements) {
				// TODO change to detect element on position x,y?
				if (e.onTouch(offsetEvent)) {
					return true;
				}
			}
		}

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mDebugOutput = "ACTION_DOWN";

			if (mTouchMode >= TOUCH_DRAG) {
				mCurrentTouch = TOUCH_DRAG;
				mLastTouchX = event.getX(0);
				mLastTouchY = event.getY(0);
				return true;
			}
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			mDebugOutput = "ACTION_POINTER_DOWN";

			if (mTouchMode >= TOUCH_ZOOM) {
				mCurrentTouch = TOUCH_ZOOM;
				return true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			switch (mCurrentTouch) {
			case TOUCH_DRAG:
				addTranslation(event.getX() - mLastTouchX, event.getY()
						- mLastTouchY);
				mLastTouchX = event.getX(0);
				mLastTouchY = event.getY(0);
				return true;
			case TOUCH_ZOOM:
				final float newDistance = (float) Math.hypot(event.getX(0)
						- event.getX(1), event.getY(0) - event.getY(1));
				if (mTouchPointerLastDistance > 0f) {
					final float pointX = (event.getX(0) + event.getX(1)) / 2;
					final float pointY = (event.getY(0) + event.getY(1)) / 2;
					final float scale = newDistance / mTouchPointerLastDistance;
					addScale(scale, scale, pointX, pointY);
				}
				mTouchPointerLastDistance = newDistance;
				return true;
			}
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mDebugOutput = "ACTION_POINTER_UP";

			if (mTouchMode >= TOUCH_DRAG) {
				mCurrentTouch = TOUCH_DRAG;
				mLastTouchX = event.getX(0);
				mLastTouchY = event.getY(0);
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			mDebugOutput = "ACTION_UP";

			mCurrentTouch = TOUCH_NONE;
			mTouchPointerLastDistance = 0f;
			return true;
		}

		return false;
	}

	private String mDebugOutput = "";

	public String getDebugOutput() {
		return mDebugOutput;
	}
}
package de.fieben.feengine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * This is the root element of the {@link FeSurface}s scene graph.
 * 
 * @author Felix Moser - felix.ernesto.moser@googlemail.com
 * 
 */
class FeRootElement extends FeSurfaceElement implements FeSurfaceTouchable {

	// WIP add touch mode rotate
	private final static int TOUCH_NONE = 0;
	private final static int TOUCH_DRAG = 1;
	private final static int TOUCH_ZOOM = 2;

	private int mVoidColor;
	private int mTouchMode;
	private int mCurrentTouch;

	private float mLastTouchX;
	private float mLastTouchY;
	private float mLastTouchDistance = 0f;

	private ArrayList<FeSurfaceTouchable> mTouchableElements = new ArrayList<FeSurfaceTouchable>();

	public FeRootElement(final int voidColor, final int touchMode) {
		super(-1);
		mVoidColor = voidColor;
		mTouchMode = touchMode;
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

	public void registerTouchable(final FeSurfaceTouchable touchableElement) {
		mTouchableElements.add(touchableElement);
	}

	// WIP stop scrolling if tile border is reached?
	@Override
	public boolean onTouch(final MotionEvent event) {
		if (mCurrentTouch == TOUCH_NONE) {
			final MotionEvent offsetEvent = MotionEvent.obtain(event);
			offsetEvent.offsetLocation(-getTranslateX(), -getTranslateY());
			for (final FeSurfaceTouchable e : mTouchableElements) {
				// WIP change to detect element on position x,y?
				if (e.onTouch(offsetEvent)) {
					return true;
				}
			}
		}

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			if (mTouchMode >= TOUCH_DRAG) {
				mCurrentTouch = TOUCH_DRAG;
				mLastTouchX = event.getX(0);
				mLastTouchY = event.getY(0);
				return true;
			}
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			if (mTouchMode >= TOUCH_ZOOM) {
				mCurrentTouch = TOUCH_ZOOM;
				mLastTouchDistance = (float) Math.hypot(
						event.getX(0) - event.getX(1),
						event.getY(0) - event.getY(1));
				return true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			switch (mCurrentTouch) {
			case TOUCH_DRAG:
				FeSurface.SURFACE.addSurfaceTranslation(event.getX()
						- mLastTouchX, event.getY() - mLastTouchY);
				mLastTouchX = event.getX(0);
				mLastTouchY = event.getY(0);
				return true;
			case TOUCH_ZOOM:
				final float newDistance = (float) Math.hypot(event.getX(0)
						- event.getX(1), event.getY(0) - event.getY(1));
				final float centerPointX = (event.getX(0) + event.getX(1)) / 2;
				final float centerPointY = (event.getY(0) + event.getY(1)) / 2;
				final float scale = newDistance / mLastTouchDistance;
				FeSurface.SURFACE.addSurfaceScale(scale, scale, centerPointX,
						centerPointY);
				mLastTouchDistance = newDistance;
				return true;
			}
			break;
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_UP:
			mCurrentTouch = TOUCH_NONE;
			return true;
			// WIP impl mode changing and prevent jumping afterwards
			// if (mTouchMode >= TOUCH_DRAG) {
			// (the above is a workaround)
			// mCurrentTouch = TOUCH_DRAG;
			// mLastTouchX = event.getX(0);
			// mLastTouchY = event.getY(0);
			// return true;
			// }
		}
		return false;
	}
}
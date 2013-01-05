package de.fieben.fengine.surface.impl;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import de.fieben.fengine.surface.FeSurface;
import de.fieben.fengine.surface.FeSurfaceElement;
import de.fieben.fengine.surface.FeSurfaceElement.FeSurfaceTouchable;

public final class FeRootElement extends FeSurfaceElement implements
		FeSurfaceTouchable {

	// TAI add touch mode rotate
	private enum TouchMode {
		NONE, DRAG, ZOOM
	};

	private int mVoidColor;
	private boolean mScrollEnabled;
	private boolean mZoomEnabled;
	private TouchMode mTouchMode = TouchMode.NONE;
	private float mLastTouchX;
	private float mLastTouchY;
	private ArrayList<FeSurfaceTouchable> mTouchableElements = new ArrayList<FeSurfaceTouchable>();

	// TODO better construktor param usage/handling
	public FeRootElement(final int voidColor, final boolean scrollEnabled) {
		// TODO -1 indicates infinity size and no reaction on touch, check if
		// nessesary or right.
		super(-1, -1);
		mVoidColor = voidColor;
		mScrollEnabled = scrollEnabled;
		// WIP impl
		mZoomEnabled = true;
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
	public void addTranslate(final float translateX, final float translateY) {
		super.addTranslate(translateX, translateY);
		FeSurface.OFFSET_X += translateX;
		FeSurface.OFFSET_Y += translateY;
	}

	public void registerTouchable(final FeSurfaceTouchable touchableElement) {
		mTouchableElements.add(touchableElement);
	}

	private float mTouchPointerLastDistance = 0f;

	// TAI stop scrolling if tile border is reached?
	@Override
	public boolean onTouch(final MotionEvent event) {
		final int action = event.getAction();
		// TODO prevent jumping after mode changes from zoom to drag
		final int pointerCount = event.getPointerCount();

		if (mTouchMode == TouchMode.NONE) {
			final MotionEvent offsetEvent = MotionEvent.obtain(event);
			offsetEvent.offsetLocation(-getTranslateX(), -getTranslateY());
			for (final FeSurfaceTouchable e : mTouchableElements) {
				// TODO change to detect element on position x,y?
				if (e.onTouch(offsetEvent)) {
					return true;
				}
			}
		}

		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mDebugOutput = "ACTION_DOWN";

			mTouchMode = TouchMode.DRAG;
			mLastTouchX = event.getX(0);
			mLastTouchY = event.getY(0);
			return true;
		case MotionEvent.ACTION_POINTER_DOWN:
			mDebugOutput = "ACTION_POINTER_DOWN";

			mTouchMode = TouchMode.ZOOM;
			return true;
		case MotionEvent.ACTION_MOVE:
			if (mScrollEnabled && mTouchMode == TouchMode.DRAG) {
				addTranslate(event.getX() - mLastTouchX, event.getY()
						- mLastTouchY);
				mLastTouchX = event.getX(0);
				mLastTouchY = event.getY(0);
				return true;
			}
			if (mZoomEnabled && mTouchMode == TouchMode.ZOOM) {
				final float newDistance = (float) Math.hypot(event.getX(0)
						- event.getX(1), event.getY(0) - event.getY(1));

				if (mTouchPointerLastDistance > 0f) {
					final float pointX = (event.getX(0) + event.getX(1)) / 2;
					final float pointY = (event.getY(0) + event.getY(1)) / 2;
					final float scale = newDistance / mTouchPointerLastDistance;

					postScale(scale, scale, pointX, pointY);
				}

				mTouchPointerLastDistance = newDistance;
				return true;
			}
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mDebugOutput = "ACTION_POINTER_UP";

			mTouchMode = TouchMode.DRAG;
			mLastTouchX = event.getX(0);
			mLastTouchY = event.getY(0);
			return true;
		case MotionEvent.ACTION_UP:
			mDebugOutput = "ACTION_UP";

			mTouchMode = TouchMode.NONE;
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
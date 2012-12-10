package de.fieben.fengine.showroom.scenes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import de.fieben.fengine.surface.FeSurface;

public abstract class AbstractScene extends FeSurface {
	public AbstractScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public void setDrag(final boolean dragEnabled) {
		mDragEnabled = true;
	}

	private boolean mDragEnabled;
	// TODO rename
	private float mLastX;
	private float mLastY;

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
				addTranslate(event.getX() - mLastX, event.getY() - mLastY);
				break;
			}
			return true;
		}
		return false;
	}
}
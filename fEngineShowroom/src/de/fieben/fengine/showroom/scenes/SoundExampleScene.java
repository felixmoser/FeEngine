package de.fieben.fengine.showroom.scenes;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import de.fieben.fengine.showroom.R;
import de.fieben.fengine.showroom.elements.CircleElement;
import de.fieben.fengine.surface.FeSurface;
import de.fieben.fengine.surface.FeSurfaceElement.FeSurfaceTouchable;
import de.fieben.fengine.surface.impl.FeSoundElement;

public class SoundExampleScene extends FeSurface {

	private SoundTestElement mElement;

	public SoundExampleScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		mElement = new SoundTestElement(context, 100, R.raw.sound_view_clicked,
				true);
		mElement.setTranslation(200, 200);
		addElement(mElement);
		// TAI schöner machen? evtl mit flag @ addElement?
		registerTouchable(mElement);
	}

	@Override
	public void surfaceDestroyed(final SurfaceHolder holder) {
		// TODO resume audio playback after home is pressed...
		mElement.stopSound();
		super.surfaceDestroyed(holder);
	}

	private class SoundTestElement extends CircleElement implements
			FeSurfaceTouchable, FeSoundElement.LoadCompleteCallback {

		private boolean mDragMode = false;
		private float mLastTouchX;
		private float mLastTouchY;

		public SoundTestElement(final Context context, final int radius,
				final int resourceId, final boolean enableSurround) {
			super(radius);
			setSound(context, resourceId, this);
			setSurroundSound(enableSurround);
		}

		@Override
		public boolean onTouch(final MotionEvent event) {
			final Point cords = getAbsoluteSurfacePosition();
			if (!mDragMode
					&& Math.hypot(event.getX() - cords.x, event.getY()
							- cords.y) > mRadius) {
				return false;
			}

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastTouchX = event.getX();
				mLastTouchY = event.getY();

				mDragMode = true;
				break;
			case MotionEvent.ACTION_MOVE:
				addTranslation(event.getX() - mLastTouchX, event.getY()
						- mLastTouchY);
				mLastTouchX = event.getX();
				mLastTouchY = event.getY();
				break;
			case MotionEvent.ACTION_UP:
				mDragMode = false;
				break;
			}
			limitTranslation();
			return true;
		}

		private void limitTranslation() {
			final float translationX = getTranslationX();
			final float translationY = getTranslationY();
			if (translationX < 0 || translationX > WIDTH || translationY < 0
					|| translationY > HEIGHT) {
				setTranslation(Math.max(0, Math.min(translationX, WIDTH)),
						Math.max(0, Math.min(translationY, HEIGHT)));
			}
		}

		@Override
		public void soundLoaded() {
			playSound(true);
		}
	}
}
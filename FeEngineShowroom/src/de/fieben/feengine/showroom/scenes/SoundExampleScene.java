package de.fieben.feengine.showroom.scenes;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import de.fieben.feengine.FeSurface;
import de.fieben.feengine.FeSurfaceElement.FeSurfaceTouchable;
import de.fieben.feengine.FeSurfaceElement.LoadCompleteCallback;
import de.fieben.feengine.showroom.R;
import de.fieben.feengine.showroom.elements.CircleElement;

public class SoundExampleScene extends FeSurface implements
		LoadCompleteCallback {

	private SoundTestElement mSoundElement;
	private boolean mSoundLoaded = false;

	public SoundExampleScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		mSoundElement = new SoundTestElement(context, 100,
				R.raw.sound_view_clicked, true);
		mSoundElement.setTranslate(200, 200);
		addElement(mSoundElement);
		// TAI schöner machen? evtl mit flag @ addElement?
		registerTouchable(mSoundElement);
	}

	@Override
	public void soundLoaded() {
		mSoundElement.playSound(true);
		mSoundLoaded = true;
	}

	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
		if (mSoundLoaded) {
			mSoundElement.playSound(true);
		}
		super.surfaceCreated(holder);
	}

	@Override
	public void surfaceDestroyed(final SurfaceHolder holder) {
		mSoundElement.stopSound();
		super.surfaceDestroyed(holder);
	}

	private class SoundTestElement extends CircleElement implements
			FeSurfaceTouchable {
		private boolean mDragMode = false;
		private float mLastTouchX;
		private float mLastTouchY;

		public SoundTestElement(final Context context, final int radius,
				final int resourceId, final boolean enableSurround) {
			super(radius);
			setSound(context, resourceId, SoundExampleScene.this);
			setSurroundSound(enableSurround);
		}

		@Override
		public boolean onTouch(final MotionEvent event) {
			final Point cords = getAbsoluteSurfacePosition();
			// WIP scaling breaks touchDetection
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
				addTranslate(event.getX() - mLastTouchX, event.getY()
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
				setTranslate(Math.max(0, Math.min(translationX, WIDTH)),
						Math.max(0, Math.min(translationY, HEIGHT)));
			}
		}
	}
}
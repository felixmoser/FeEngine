package de.fieben.feengine.showroom.scenes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import de.fieben.feengine.FeSurface;
import de.fieben.feengine.FeSurfaceElement;
import de.fieben.feengine.FeSurfaceElement.FeSurfaceTouchable;
import de.fieben.feengine.FeSurfaceElement.LoadCompleteCallback;
import de.fieben.feengine.showroom.R;
import de.fieben.feengine.showroom.elements.CircleElement;

public class SoundExampleScene extends FeSurface implements
		LoadCompleteCallback {
	// WIP add "sound mode"-switch FSE

	private SoundTestElement mSoundElement;
	private boolean mSoundLoaded = false;

	public SoundExampleScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		mSoundElement = new SoundTestElement(context, 100,
				R.raw.sound_view_clicked);
		mSoundElement.setTranslate(300, 300);
		addElement(mSoundElement);
		// TAI schöner machen? evtl mit flag @ addElement?
		registerTouchable(mSoundElement);

		setSurfaceScale(0.5f, 0.5f, 0f, 0f);
	}

	@Override
	public void soundLoaded() {
		mSoundElement.playSound(FeSurfaceElement.SoundMode.SURROUND, true);
		mSoundLoaded = true;
	}

	@Override
	public void surfaceCreated(final SurfaceHolder holder) {
		if (mSoundLoaded) {
			mSoundElement.playSound(FeSurfaceElement.SoundMode.SURROUND, true);
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
				final int resourceId) {
			super(radius);
			setSound(context, resourceId, SoundExampleScene.this);
		}

		@Override
		public boolean onTouch(final MotionEvent event) {
			Point cords = getAbsoluteSurfacePosition();
			final float surfaceScaleFactor = (getSurfaceScaleX() + getSurfaceScaleY()) / 2;

			if (!mDragMode
					&& Math.hypot(event.getX() - cords.x * surfaceScaleFactor,
							event.getY() - cords.y * surfaceScaleFactor) > mRadius
							* surfaceScaleFactor) {
				return false;
			}

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastTouchX = event.getX();
				mLastTouchY = event.getY();

				mDragMode = true;
				setColor(Color.BLUE);
				break;
			case MotionEvent.ACTION_MOVE:
				addTranslate((event.getX() - mLastTouchX) / surfaceScaleFactor,
						(event.getY() - mLastTouchY) / surfaceScaleFactor);
				mLastTouchX = event.getX();
				mLastTouchY = event.getY();
				break;
			case MotionEvent.ACTION_UP:
				mDragMode = false;
				setColor(Color.WHITE);
				break;
			}

			// WIP finisch
			cords = getAbsoluteSurfacePosition();
			if (cords.x * surfaceScaleFactor < getSurfaceTranslationX()
					|| cords.x * surfaceScaleFactor > getSurfaceWidth()
							- getSurfaceTranslationX()
					|| cords.y < getSurfaceTranslationY()
					|| cords.y > getSurfaceHeight() - getSurfaceTranslationY()) {
				// DEBUG scale
				setColor(Color.YELLOW);
			}

			// limitTranslation();
			return true;
		}

		// private void limitTranslation() {
		// final float translateX = getTranslateX();
		// final float translateY = getTranslateY();
		// if (translateX < 0 || translateX > WIDTH || translateY < 0
		// || translateY > HEIGHT) {
		// setColor(Color.YELLOW);
		// setTranslate(Math.max(0, Math.min(translateX, WIDTH)),
		// Math.max(0, Math.min(translateY, HEIGHT)));
		// }
		// }
	}
}
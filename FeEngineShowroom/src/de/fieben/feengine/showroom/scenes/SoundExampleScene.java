package de.fieben.feengine.showroom.scenes;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import de.fieben.feengine.FeSurface;
import de.fieben.feengine.FeSurfaceElement;
import de.fieben.feengine.FeSurfaceTouchable;
import de.fieben.feengine.SoundLoadCallback;
import de.fieben.feengine.showroom.R;
import de.fieben.feengine.showroom.elements.CircleElement;

public class SoundExampleScene extends FeSurface implements SoundLoadCallback {
	// WIP add "sound mode"-switch FSE

	private SoundTestElement mSoundElement;
	private boolean mSoundLoaded = false;

	public SoundExampleScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		mSoundElement = new SoundTestElement(context, 150,
				R.raw.sound_view_clicked);
		mSoundElement.setTranslate(300, 300);
		addElement(mSoundElement);
		// TAI schöner machen? evtl mit flag @ addElement?
		registerTouchable(mSoundElement);

		setSurfaceScale(0.5f, 0.5f, 0f, 0f);
	}

	@Override
	public void soundLoaded(final boolean success) {
		if (success) {
			mSoundElement.playSound(FeSurfaceElement.SoundMode.SURROUND, true);
			mSoundLoaded = true;
		}
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
				setColor(Color.BLUE);
				break;
			case MotionEvent.ACTION_UP:
				mDragMode = false;
				setColor(Color.WHITE);
				break;
			}

			cords = getAbsoluteSurfacePosition();
			final float minX = -getSurfaceTranslationX() / surfaceScaleFactor;
			final float maxX = (getSurfaceWidth() - getSurfaceTranslationX())
					/ surfaceScaleFactor;
			final float minY = -getSurfaceTranslationY() / surfaceScaleFactor;
			final float maxY = (getSurfaceHeight() - getSurfaceTranslationY())
					/ surfaceScaleFactor;
			if (cords.x < minX || cords.x > maxX || cords.y < minY
					|| cords.y > maxY) {
				setColor(Color.YELLOW);
				setTranslate(Math.max(minX, Math.min(getTranslateX(), maxX)),
						Math.max(minY, Math.min(getTranslateY(), maxY)));
			}
			return true;
		}
	}
}
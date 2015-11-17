package de.fieben.feengine.showroom.scenes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
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

	private SoundTestElement mSoundElement;
	private boolean mSoundLoaded = false;

	public SoundExampleScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		mSoundElement = new SoundTestElement(context, 150, R.raw.sound_view_clicked);
		mSoundElement.setTranslate(300, 300);
		addElement(mSoundElement);
		registerTouchable(mSoundElement);

		setSurfaceScale(0.5f, 0.5f, 0f, 0f);
	}

	@Override
	public void soundLoaded(final boolean success) {
		mSoundLoaded = success;
	}

	@Override
	public void surfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height) {
		final GridElement grid = new GridElement(100);
		grid.calculateGrid((int) (width / getSurfaceScaleX()), (int) (height / getSurfaceScaleY()));
		addBackgroundElement(grid);

		final ButtonElement monoButton = new ButtonElement(FeSurfaceElement.SoundMode.MONO, 300, 200);
		monoButton.setTranslate((width - 200) / getSurfaceScaleX(), 50 / getSurfaceScaleX());
		addElement(monoButton);
		registerTouchable(monoButton);

		final ButtonElement stereoButton = new ButtonElement(FeSurfaceElement.SoundMode.STEREO, 300, 200);
		stereoButton.setTranslate((width - 200) / getSurfaceScaleX(), 200 / getSurfaceScaleX());
		addElement(stereoButton);
		registerTouchable(stereoButton);

		final ButtonElement surroundButton = new ButtonElement(FeSurfaceElement.SoundMode.SURROUND, 300, 200);
		surroundButton.setTranslate((width - 200) / getSurfaceScaleX(), 350 / getSurfaceScaleX());
		addElement(surroundButton);
		registerTouchable(surroundButton);

		final ButtonElement stopButton = new ButtonElement(null, 300, 200);
		stopButton.setTranslate((width - 200) / getSurfaceScaleX(), 500 / getSurfaceScaleX());
		addElement(stopButton);
		registerTouchable(stopButton);

		super.surfaceChanged(holder, format, width, height);
	}

	@Override
	public void surfaceDestroyed(final SurfaceHolder holder) {
		mSoundElement.stopSound();
		super.surfaceDestroyed(holder);
	}

	private class SoundTestElement extends CircleElement implements FeSurfaceTouchable {
		private boolean mDragMode = false;
		private float mLastTouchX;
		private float mLastTouchY;

		public SoundTestElement(final Context context, final int radius, final int resourceId) {
			super(radius);
			setSound(context, resourceId, SoundExampleScene.this);
		}

		@Override
		public boolean onTouch(final MotionEvent event) {
			Point cords = getAbsoluteSurfacePosition();
			final float surfaceScaleFactor = (getSurfaceScaleX() + getSurfaceScaleY()) / 2;

			if (!mDragMode &&
					Math.hypot(event.getX() - cords.x * surfaceScaleFactor, event.getY() - cords.y * surfaceScaleFactor) > mRadius * surfaceScaleFactor) {
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
				addTranslate((event.getX() - mLastTouchX) / surfaceScaleFactor, (event.getY() - mLastTouchY) / surfaceScaleFactor);
				mLastTouchX = event.getX();
				mLastTouchY = event.getY();
				break;
			case MotionEvent.ACTION_UP:
				mDragMode = false;
				setColor(Color.WHITE);
				break;
			}

			cords = getAbsoluteSurfacePosition();
			final float minX = -getSurfaceTranslationX() / surfaceScaleFactor;
			final float maxX = (getSurfaceWidth() - getSurfaceTranslationX()) / surfaceScaleFactor;
			final float minY = -getSurfaceTranslationY() / surfaceScaleFactor;
			final float maxY = (getSurfaceHeight() - getSurfaceTranslationY()) / surfaceScaleFactor;
			if (cords.x < minX || cords.x > maxX || cords.y < minY || cords.y > maxY) {
				setTranslate(Math.max(minX, Math.min(getTranslateX(), maxX)), Math.max(minY, Math.min(getTranslateY(), maxY)));
			}
			return true;
		}
	}

	private class ButtonElement extends FeSurfaceElement implements FeSurfaceTouchable {

		private FeSurfaceElement.SoundMode mMode;
		private final Rect mDimensions;

		public ButtonElement(final FeSurfaceElement.SoundMode mode, final int width, final int height) {
			super(-1);
			mMode = mode;
			mDimensions = new Rect(0, 0, width, height);
		}

		@Override
		public boolean onTouch(final MotionEvent event) {
			final Point cords = getAbsoluteSurfacePosition();
			final float surfaceScaleFactor = (getSurfaceScaleX() + getSurfaceScaleY()) / 2;

			if (event.getX() < cords.x * surfaceScaleFactor ||
					event.getX() > (cords.x + mDimensions.right) * surfaceScaleFactor ||
					event.getY() < cords.y * surfaceScaleFactor ||
					event.getY() > (cords.y + mDimensions.bottom) * surfaceScaleFactor) {
				return false;
			}

			if (mSoundLoaded && event.getAction() == MotionEvent.ACTION_DOWN) {
				mSoundElement.stopSound();
				if (mMode != null) {
					mSoundElement.playSound(mMode, true);
				}
				return true;
			}
			return false;

		}

		@Override
		public void onDraw(final Canvas canvas, final Paint paint) {
			paint.setColor(Color.WHITE);
			canvas.drawRect(mDimensions, paint);

			paint.setColor(Color.BLACK);
			paint.setTextSize(50f);
			if (mMode != null) {
				canvas.drawText(mMode.name(), 10, 50, paint);
			} else {
				canvas.drawText("stop", 10, 50, paint);
			}
		}

		@Override
		public void onUpdate(final long elapsedMillis) {
		}

		@Override
		public void doUpdate() {
		}
	}

	private class GridElement extends FeSurfaceElement {
		private final int mSpacing;
		private float[] mGrid;

		public GridElement(final int spacing) {
			super(-1);
			mSpacing = spacing;
		}

		@Override
		public void onDraw(final Canvas canvas, final Paint paint) {
			paint.setColor(Color.YELLOW);
			canvas.drawLines(mGrid, paint);
		}

		public void calculateGrid(final int width, final int height) {
			mGrid = new float[(width / mSpacing + height / mSpacing) * 4];
			int nextGridIndex = 0;
			for (int nextXValue = mSpacing; nextXValue < width; nextXValue += mSpacing) {
				mGrid[nextGridIndex++] = nextXValue;
				mGrid[nextGridIndex++] = 0;
				mGrid[nextGridIndex++] = nextXValue;
				mGrid[nextGridIndex++] = height;
			}
			for (int nextYValue = mSpacing; nextYValue < height; nextYValue += mSpacing) {
				mGrid[nextGridIndex++] = 0;
				mGrid[nextGridIndex++] = nextYValue;
				mGrid[nextGridIndex++] = width;
				mGrid[nextGridIndex++] = nextYValue;
			}
		}

		@Override
		public void onUpdate(final long elapsedMillis) {
		}

		@Override
		public void doUpdate() {
		}
	}
}
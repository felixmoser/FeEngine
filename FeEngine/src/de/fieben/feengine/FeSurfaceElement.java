package de.fieben.feengine;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.SparseArray;

public abstract class FeSurfaceElement {
	// TAI what about display density?
	// TAI impl ancor point/coorinats

	public static enum SoundMode {
		MONO, STEREO, SURROUND
	}

	private final int mBitmapKey;

	private final Matrix mMatrix;
	private final float[] mMatrixValues;

	private float mRotationDegrees = 0f;
	private float mRotationAroundCenterDegrees = 0f;

	private FeSurfaceElement mParent;

	// TAI add translate modifier to layers. e.g. 0f equals no translation, 1f
	// full
	private final SparseArray<ArrayList<FeSurfaceElement>> mChildLayers;
	private int mMaxLayerLevel = 0;

	private int mDoUpdateInterval;
	private long mDoUpdateCounter;

	private int mAnimationInterval;
	private int mAnimationCounter;
	private int mAnimationSteps;
	private int mAnimationCurrentIndex;
	private int mAnimationBitmapGridDimension;
	private int mAnimationWidth;
	private int mAnimationHeight;
	private Rect mAnimationSrcRect;
	private Rect mAnimationDstRect;

	private FeSoundElement mSoundElement = null;
	private SoundMode mSoundMode = SoundMode.MONO;

	/**
	 * 
	 * @param bitmapKey
	 *            The key of the bitmap in the {@link SufaceView}s bitmappool.
	 *            -1 if no bitmap should be used.
	 */
	public FeSurfaceElement(final int bitmapKey) {
		mMatrix = new Matrix();
		mMatrixValues = new float[9];
		mChildLayers = new SparseArray<ArrayList<FeSurfaceElement>>();
		mBitmapKey = bitmapKey;
	}

	public FeSurfaceElement(final int bitmapKey, final int stepCount,
			final int animationInterval) {
		mMatrix = new Matrix();
		mMatrixValues = new float[9];
		mChildLayers = new SparseArray<ArrayList<FeSurfaceElement>>();
		mBitmapKey = bitmapKey;

		mAnimationCounter = mAnimationInterval = animationInterval;

		mAnimationSteps = stepCount;
		mAnimationCurrentIndex = 0;

		mAnimationBitmapGridDimension = (int) Math.ceil(Math.sqrt(stepCount));
		mAnimationWidth = getBitmap().getWidth()
				/ mAnimationBitmapGridDimension;
		mAnimationHeight = getBitmap().getHeight()
				/ mAnimationBitmapGridDimension;

		mAnimationDstRect = new Rect(-mAnimationWidth / 2,
				-mAnimationHeight / 2, mAnimationWidth / 2,
				mAnimationHeight / 2);
		mAnimationSrcRect = new Rect(0, 0, mAnimationWidth, mAnimationHeight);
	}

	private Bitmap getBitmap() {
		return FeBitmapPool.getBitmap(mBitmapKey);
	}

	public void setTranslate(final float translateX, final float translateY) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrix.postTranslate(translateX - mMatrixValues[Matrix.MTRANS_X],
					translateY - mMatrixValues[Matrix.MTRANS_Y]);
		}
	}

	public void addTranslate(final float translateX, final float translateY) {
		synchronized (mMatrix) {
			mMatrix.postTranslate(translateX, translateY);
		}
	}

	public float getTranslateX() {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			return mMatrixValues[Matrix.MTRANS_X];
		}
	}

	public float getTranslateY() {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			return mMatrixValues[Matrix.MTRANS_Y];
		}
	}

	public final Point getAbsoluteSurfacePosition() {
		if (mParent == null) {
			return new Point();
		} else {
			final Point cords = mParent.getAbsoluteSurfacePosition();
			synchronized (mMatrix) {
				mMatrix.getValues(mMatrixValues);
				cords.x += mMatrixValues[Matrix.MTRANS_X];
				cords.y += mMatrixValues[Matrix.MTRANS_Y];
			}
			return cords;
		}
	}

	public void setScale(final float scaleX, final float scaleY) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrix.postScale(scaleX / mMatrixValues[Matrix.MSCALE_X], scaleY
					/ mMatrixValues[Matrix.MSCALE_Y]);
		}
	}

	public void addScale(final float scaleX, final float scaleY) {
		synchronized (mMatrix) {
			mMatrix.postScale(scaleX, scaleY);
		}
	}

	public void setScale(final float scaleX, final float scaleY,
			final float pointX, final float pointY) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrix.postScale(scaleX / mMatrixValues[Matrix.MSCALE_X], scaleY
					/ mMatrixValues[Matrix.MSCALE_Y], pointX, pointY);
		}
	}

	public void addScale(final float scaleX, final float scaleY,
			final float pointX, final float pointY) {
		synchronized (mMatrix) {
			mMatrix.postScale(scaleX, scaleY, pointX, pointY);
		}
	}

	public float getScaleX() {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			return mMatrixValues[Matrix.MSCALE_X];
		}
	}

	public float getScaleY() {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			return mMatrixValues[Matrix.MSCALE_Y];
		}
	}

	public void setScaleFromCenter(final float scaleX, final float scaleY) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrix.postScale(scaleX / mMatrixValues[Matrix.MSCALE_X], scaleY
					/ mMatrixValues[Matrix.MSCALE_Y],
					mMatrixValues[Matrix.MTRANS_X],
					mMatrixValues[Matrix.MTRANS_Y]);
		}
	}

	public void addScaleFromCenter(final float scaleX, final float scaleY) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrix.postScale(scaleX, scaleY, mMatrixValues[Matrix.MTRANS_X],
					mMatrixValues[Matrix.MTRANS_Y]);
		}
	}

	public void setRotate(final float degrees) {
		synchronized (mMatrix) {
			mMatrix.postRotate(degrees - mRotationDegrees);
			mRotationDegrees = degrees;
		}
	}

	public void addRotate(final float degrees) {
		synchronized (mMatrix) {
			mMatrix.postRotate(degrees);
			mRotationDegrees += degrees;
		}
	}

	public float getRotate() {
		synchronized (mMatrix) {
			return mRotationDegrees;
		}
	}

	public void setRotateAroundCenter(final float degrees) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrix.postRotate(degrees - mRotationAroundCenterDegrees,
					mMatrixValues[Matrix.MTRANS_X],
					mMatrixValues[Matrix.MTRANS_Y]);
			mRotationAroundCenterDegrees = degrees;
		}
	}

	public void addRotateAroundCenter(final float degrees) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrix.postRotate(degrees, mMatrixValues[Matrix.MTRANS_X],
					mMatrixValues[Matrix.MTRANS_Y]);
			mRotationAroundCenterDegrees += degrees;
		}
	}

	public float getRotateAroundCenter() {
		synchronized (mMatrix) {
			return mRotationAroundCenterDegrees;
		}
	}

	// TAI provide removeChild method
	public void addChild(final FeSurfaceElement child) {
		addChild(0, child);
	}

	public void addChild(final int layerLevel, final FeSurfaceElement child) {
		synchronized (mMatrix) {
			child.mParent = this;

			final int layerIndex = mChildLayers.indexOfKey(layerLevel);
			if (layerIndex < 0) {
				final ArrayList<FeSurfaceElement> layer = new ArrayList<FeSurfaceElement>();
				layer.add(child);
				mChildLayers.put(layerLevel, layer);
			} else {
				mChildLayers.valueAt(layerIndex).add(child);
			}

			if (mMaxLayerLevel < layerLevel) {
				mMaxLayerLevel = layerLevel;
			}
		}
	}

	protected final void draw(final Canvas canvas, final Paint paint) {
		synchronized (mMatrix) {
			canvas.save();
			canvas.concat(mMatrix);

			if (mBitmapKey != -1) {
				if (mAnimationSteps > 0) {
					canvas.drawBitmap(getBitmap(), mAnimationSrcRect,
							mAnimationDstRect, paint);
				} else {
					canvas.drawBitmap(getBitmap(), 0, 0, paint);
				}
			}
			onDraw(canvas, paint);

			for (int i = 0; i <= mMaxLayerLevel; i++) {
				final int index = mChildLayers.indexOfKey(i);
				if (index >= 0) {
					for (final FeSurfaceElement e : mChildLayers.valueAt(index)) {
						e.draw(canvas, paint);
					}
				}
			}

			canvas.restore();
		}
	}

	protected final void update(final long elapsedMillis) {
		synchronized (mMatrix) {
			onUpdate(elapsedMillis);

			// TAI handle "screen off"
			if (mDoUpdateInterval > 0) {
				mDoUpdateCounter -= elapsedMillis;
				if (mDoUpdateCounter <= 0) {
					mDoUpdateCounter += mDoUpdateInterval;
					doUpdate();
				}
			}

			if (mSoundMode != SoundMode.MONO) {
				updateVolumes();
			}

			// TAI start and stop animation
			if (mAnimationInterval > 0) {
				mAnimationCounter -= elapsedMillis;
				if (mAnimationCounter <= 0) {
					mAnimationCounter += mAnimationInterval;
					if (++mAnimationCurrentIndex >= mAnimationSteps) {
						mAnimationCurrentIndex = 0;
					}
					final int left = (mAnimationCurrentIndex % mAnimationBitmapGridDimension)
							* mAnimationWidth;
					final int top = (mAnimationCurrentIndex / mAnimationBitmapGridDimension)
							* mAnimationHeight;
					final int right = left + mAnimationWidth;
					final int bottom = top + mAnimationHeight;
					mAnimationSrcRect = new Rect(left, top, right, bottom);
				}
			}

			for (int i = 0; i <= mMaxLayerLevel; i++) {
				final int index = mChildLayers.indexOfKey(i);
				if (index >= 0) {
					for (final FeSurfaceElement e : mChildLayers.valueAt(index)) {
						e.update(elapsedMillis);
					}
				}
			}
		}
	}

	public abstract void onDraw(final Canvas canvas, final Paint paint);

	public abstract void onUpdate(final long elapsedMillis);

	public abstract void doUpdate();

	public void setUpdateInterval(final int updateInterval) {
		mDoUpdateCounter = mDoUpdateInterval = updateInterval;
	}

	public void setSound(final Context context, final int resourceId,
			final SoundLoadCallback callback) {
		mSoundElement = new FeSoundElement(context, resourceId, callback);
	}

	public void playSound(final SoundMode mode, final boolean loop) {
		mSoundElement.playSound(1f, 1f, loop);
		mSoundMode = mode;
		if (mode != SoundMode.MONO) {
			updateVolumes();
		}
	}

	public void stopSound() {
		mSoundElement.stopSound();
	}

	public void setVolume(final float volume) {
		mSoundElement.setMasterVolume(volume);
	}

	// TAI handle offscreen elements?
	private void updateVolumes() {
		final Point cords = getAbsoluteSurfacePosition();

		final float surfaceScaleX = FeSurface.SURFACE.getSurfaceScaleX();
		final float surfaceTranslateX = FeSurface.SURFACE
				.getSurfaceTranslationX();

		final float leftVolume = (FeSurface.SURFACE.getSurfaceWidth()
				- surfaceTranslateX - cords.x * surfaceScaleX)
				/ FeSurface.SURFACE.getSurfaceWidth();
		final float rightVolume = (surfaceTranslateX + cords.x * surfaceScaleX)
				/ FeSurface.SURFACE.getSurfaceWidth();
		final float yMod = mSoundMode == SoundMode.SURROUND ? (FeSurface.SURFACE
				.getSurfaceTranslationY() + cords.y
				* FeSurface.SURFACE.getSurfaceScaleY())
				/ FeSurface.SURFACE.getSurfaceHeight() : 1f;

		mSoundElement.setChannelVolumes(leftVolume * yMod, rightVolume * yMod);
	}
}
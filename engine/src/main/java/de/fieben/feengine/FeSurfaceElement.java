package de.fieben.feengine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.SoundPool;
import android.util.SparseArray;

import java.util.ArrayList;

/**
 * This class builds the basis for all elements in the {@link FeSurface}s scene graph that gets drawn on its surface.
 * Provides method for scaling, translation and rotation.
 * Native support of drawing bitmaps, update mechanisms, animations, child layers and playback of sound.
 *
 * @author Felix Moser - felix.ernesto.moser@googlemail.com
 */
public class FeSurfaceElement {

	// WIP what about display density?
	// WIP impl ancor point/coorinats

	/**
	 * The mode used for sound playback.
	 */
	public enum SoundMode {
		MONO, STEREO, SURROUND
	}

	private FeSurface mSurface;
	private final int mBitmapKey;

	private final Matrix mMatrix;
	private final float[] mMatrixValues;

	private float mRotationDegrees = 0f;
	private float mRotationAroundCenterDegrees = 0f;

	private FeSurfaceElement mParent;

	// WIP add translate modifier to layers. e.g. 0f equals no translation, 1f full
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
	 * @param bitmapKey The key retrieved from {@link FeBitmapPool} after adding a bitmap. -1 if no bitmap should be used.
	 */
	public FeSurfaceElement(final FeSurface feSurface, final int bitmapKey) {
		mSurface = feSurface;
		mMatrix = new Matrix();
		mMatrixValues = new float[9];
		mChildLayers = new SparseArray<>();
		mBitmapKey = bitmapKey;
	}

	/**
	 * @param bitmapKey The key retrieved from {@link FeBitmapPool} after adding an animation bitmap. -1 if no bitmap should be used.
	 * @param stepCount The count of steps used for the animation.
	 * @param animationInterval The time in milliseconds each animationstep should be displayed.
	 */
	public FeSurfaceElement(final FeSurface feSurface, final int bitmapKey, final int stepCount, final int animationInterval) {
		this(feSurface, bitmapKey);

		mAnimationCounter = mAnimationInterval = animationInterval;

		mAnimationSteps = stepCount;
		mAnimationCurrentIndex = 0;

		mAnimationBitmapGridDimension = (int) Math.ceil(Math.sqrt(stepCount));
		mAnimationWidth = getBitmap().getWidth() / mAnimationBitmapGridDimension;
		mAnimationHeight = getBitmap().getHeight() / mAnimationBitmapGridDimension;

		mAnimationDstRect = new Rect(-mAnimationWidth / 2, -mAnimationHeight / 2, mAnimationWidth / 2, mAnimationHeight / 2);
		mAnimationSrcRect = new Rect(0, 0, mAnimationWidth, mAnimationHeight);
	}

	FeSurface getSurface() {
		return mSurface;
	}

	private Bitmap getBitmap() {
		return FeBitmapPool.getBitmap(mBitmapKey);
	}

	public void setTranslate(final float translateX, final float translateY) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrix.postTranslate(translateX - mMatrixValues[Matrix.MTRANS_X], translateY - mMatrixValues[Matrix.MTRANS_Y]);
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

	/**
	 * Calculates all translation values of all parents together and returns the absolute surface position of this element, including its own translation.
	 *
	 * @return A {@link Point} contains the absolute position of this element on the surface.
	 */
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
			mMatrix.postScale(scaleX / mMatrixValues[Matrix.MSCALE_X], scaleY / mMatrixValues[Matrix.MSCALE_Y]);
		}
	}

	public void addScale(final float scaleX, final float scaleY) {
		synchronized (mMatrix) {
			mMatrix.postScale(scaleX, scaleY);
		}
	}

	public void setScale(final float scaleX, final float scaleY, final float pointX, final float pointY) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrix.postScale(scaleX / mMatrixValues[Matrix.MSCALE_X], scaleY / mMatrixValues[Matrix.MSCALE_Y], pointX, pointY);
		}
	}

	public void addScale(final float scaleX, final float scaleY, final float pointX, final float pointY) {
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
			mMatrix.postScale(scaleX / mMatrixValues[Matrix.MSCALE_X], scaleY / mMatrixValues[Matrix.MSCALE_Y], mMatrixValues[Matrix.MTRANS_X], mMatrixValues[Matrix.MTRANS_Y]);
		}
	}

	public void addScaleFromCenter(final float scaleX, final float scaleY) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrix.postScale(scaleX, scaleY, mMatrixValues[Matrix.MTRANS_X], mMatrixValues[Matrix.MTRANS_Y]);
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
			mMatrix.postRotate(degrees - mRotationAroundCenterDegrees, mMatrixValues[Matrix.MTRANS_X], mMatrixValues[Matrix.MTRANS_Y]);
			mRotationAroundCenterDegrees = degrees;
		}
	}

	public void addRotateAroundCenter(final float degrees) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrix.postRotate(degrees, mMatrixValues[Matrix.MTRANS_X], mMatrixValues[Matrix.MTRANS_Y]);
			mRotationAroundCenterDegrees += degrees;
		}
	}

	public float getRotateAroundCenter() {
		synchronized (mMatrix) {
			return mRotationAroundCenterDegrees;
		}
	}

	// WIP provide removeChild method
	/**
	 * Equivalent to {@link #addChild(int, FeSurfaceElement)} with layer value 0.
	 *
	 * @param child The {@link FeSurfaceElement} to add.
	 */
	public void addChild(final FeSurfaceElement child) {
		addChild(0, child);
	}

	/**
	 * Adds a {@link FeSurfaceElement} as a child of this element on a given layer.
	 *
	 * @param layerLevel The level of which the child gets added. Needs to be >=0.
	 * @param child The {@link FeSurfaceElement} to add.
	 */
	public void addChild(final int layerLevel, final FeSurfaceElement child) {
		synchronized (mMatrix) {
			child.mParent = this;

			final int layerIndex = mChildLayers.indexOfKey(layerLevel);
			if (layerIndex < 0) {
				final ArrayList<FeSurfaceElement> layer = new ArrayList<>();
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
					canvas.drawBitmap(getBitmap(), mAnimationSrcRect, mAnimationDstRect, paint);
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

			// WIP handle "screen off"
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

			// WIP start and stop animation
			if (mAnimationInterval > 0) {
				mAnimationCounter -= elapsedMillis;
				if (mAnimationCounter <= 0) {
					mAnimationCounter += mAnimationInterval;
					if (++mAnimationCurrentIndex >= mAnimationSteps) {
						mAnimationCurrentIndex = 0;
					}
					final int left = (mAnimationCurrentIndex % mAnimationBitmapGridDimension) * mAnimationWidth;
					final int top = (mAnimationCurrentIndex / mAnimationBitmapGridDimension) * mAnimationHeight;
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

	/**
	 * This method is used to do custom drawing of this element.
	 *
	 * @param canvas The {@link Canvas} to draw on.
	 * @param paint The {@link Paint} used for drawing.
	 */
	public void onDraw(final Canvas canvas, final Paint paint) {
		// Do nothing in default implementation.
	}

	/**
	 * This method is used to do continuous update of this element.
	 *
	 * @param elapsedMillis The elapsed milliseconds since last call of this method.
	 */
	public void onUpdate(final long elapsedMillis){
		// Do nothing in default implementation.
	}

	/**
	 * This method is used to do interval based updates of this element. The interval needs to be set with {@link #setUpdateInterval(int)}.
	 */
	public void doUpdate(){
		// Do nothing in default implementation.
	}

	/**
	 * Sets the update in which {@link #doUpdate()} gets called. First {@link #doUpdate()} call after the interval elapsed.
	 *
	 * @param updateInterval Interval in milliseconds.
	 */
	public void setUpdateInterval(final int updateInterval) {
		mDoUpdateCounter = mDoUpdateInterval = updateInterval;
	}

	/**
	 * Creates a {@link FeSoundElement} with a given sound resource.
	 *
	 * @param context The context used to get the resource.
	 * @param resourceId The android id of the sound resource to load. All types support by {@link SoundPool} are supported as well.
	 * @param callback The {@link SoundLoadCallback#soundLoaded(boolean)} of this callback gets triggered after the sound is loaded, can be null.
	 */
	public void setSound(final Context context, final int resourceId, final SoundLoadCallback callback) {
		mSoundElement = new FeSoundElement(context, resourceId, callback);
	}

	/**
	 * Starts the playback of a previous set sound resource if sound has finished loading.
	 *
	 * @param mode The {@link SoundMode} used for playback.
	 * @param loop true if sound playback should be looping.
	 */
	public void playSound(final SoundMode mode, final boolean loop) {
		mSoundElement.playSound(1f, 1f, loop);
		mSoundMode = mode;
		if (mode != SoundMode.MONO) {
			updateVolumes();
		}
	}

	/**
	 * Stops the playback of a sound previous started.
	 */
	public void stopSound() {
		mSoundElement.stopSound();
	}

	/**
	 * Sets the maximum volume of a previous loaded sound.
	 *
	 * @param volume The volume range from 0f to 1f.
	 */
	public void setVolume(final float volume) {
		mSoundElement.setMasterVolume(volume);
	}

	// WIP handle offscreen elements?
	private void updateVolumes() {
		final Point cords = getAbsoluteSurfacePosition();

		final float surfaceScaleX = mSurface.getSurfaceScaleX();
		final float surfaceTranslateX = mSurface.getSurfaceTranslationX();

		final float leftVolume = (mSurface.getSurfaceWidth() - surfaceTranslateX - cords.x * surfaceScaleX) / mSurface.getSurfaceWidth();
		final float rightVolume = (surfaceTranslateX + cords.x * surfaceScaleX) / mSurface.getSurfaceWidth();
		final float yMod = mSoundMode == SoundMode.SURROUND ? (mSurface.getSurfaceTranslationY() + cords.y * mSurface.getSurfaceScaleY()) / mSurface.getSurfaceHeight() : 1f;

		mSoundElement.setChannelVolumes(leftVolume * yMod, rightVolume * yMod);
	}
}
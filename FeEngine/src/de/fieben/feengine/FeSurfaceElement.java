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
import android.view.MotionEvent;

public abstract class FeSurfaceElement {
	// TAI what about display density?

	private final Bitmap mBitmap;

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
	private boolean m3DSound = false;

	public FeSurfaceElement(final int width, final int height) {
		mMatrix = new Matrix();
		mMatrixValues = new float[9];
		mChildLayers = new SparseArray<ArrayList<FeSurfaceElement>>();
		mBitmap = null;
	}

	public FeSurfaceElement(final Bitmap bitmap) {
		mMatrix = new Matrix();
		mMatrixValues = new float[9];
		mChildLayers = new SparseArray<ArrayList<FeSurfaceElement>>();
		mBitmap = bitmap;
	}

	public FeSurfaceElement(final Bitmap bitmap, final int stepCount,
			final int animationInterval) {
		mMatrix = new Matrix();
		mMatrixValues = new float[9];
		mChildLayers = new SparseArray<ArrayList<FeSurfaceElement>>();
		mBitmap = bitmap;

		mAnimationCounter = mAnimationInterval = animationInterval;

		mAnimationSteps = stepCount;
		mAnimationCurrentIndex = 0;

		mAnimationBitmapGridDimension = (int) Math.ceil(Math.sqrt(stepCount));
		mAnimationWidth = mBitmap.getWidth() / mAnimationBitmapGridDimension;
		mAnimationHeight = mBitmap.getHeight() / mAnimationBitmapGridDimension;

		mAnimationDstRect = new Rect(-mAnimationWidth / 2,
				-mAnimationHeight / 2, mAnimationWidth / 2,
				mAnimationHeight / 2);
		mAnimationSrcRect = new Rect(0, 0, mAnimationWidth, mAnimationHeight);
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

	// WIP impl all getter
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

	// TODO only draw (or iterate over?) visible elements
	protected final void draw(final Canvas canvas, final Paint paint) {
		synchronized (mMatrix) {
			canvas.save();
			canvas.concat(mMatrix);

			if (mBitmap != null) {
				if (mAnimationSteps > 0) {
					canvas.drawBitmap(mBitmap, mAnimationSrcRect,
							mAnimationDstRect, paint);
				} else {
					canvas.drawBitmap(mBitmap, 0, 0, paint);
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

			// TODO handle "screen off"
			if (mDoUpdateInterval > 0) {
				mDoUpdateCounter -= elapsedMillis;
				if (mDoUpdateCounter <= 0) {
					mDoUpdateCounter += mDoUpdateInterval;
					doUpdate();
				}
			}

			if (m3DSound) {
				update3DVolume();
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
			final LoadCompleteCallback callback) {
		mSoundElement = new FeSoundElement(context, resourceId, callback);
	}

	public void playSound(final boolean loop) {
		mSoundElement.playSound(1f, 1f, loop);
		if (m3DSound) {
			update3DVolume();
		}
	}

	public void stopSound() {
		mSoundElement.stopSound();
	}

	public void setVolume(final float volume) {
		mSoundElement.setMasterVolume(volume);
	}

	// WIP change to mono, stereo and surround
	public void setSurroundSound(final boolean enabled) {
		m3DSound = enabled;
	}

	// TAI handle offscreen elements?
	private void update3DVolume() {
		final Point cords = getAbsoluteSurfacePosition();
		final float leftVolume = (FeSurface.WIDTH - FeSurface.OFFSET_X - cords.x)
				/ FeSurface.WIDTH;
		final float rightVolume = (FeSurface.OFFSET_X + cords.x)
				/ FeSurface.WIDTH;
		// TODO if stereo yMod = 1f
		final float yMod = (FeSurface.OFFSET_Y + cords.y) / FeSurface.HEIGHT;
		mSoundElement.setChannelVolumes(leftVolume * yMod, rightVolume * yMod);
	}

	public interface FeSurfaceTouchable {
		public boolean onTouch(final MotionEvent event);
	}

	public interface LoadCompleteCallback {
		public void soundLoaded();
	}
}
package de.fieben.fengine.surface;

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
import de.fieben.fengine.surface.impl.FeSoundElement;

// TODO impl FeSurfaceObject as parent and FeSurfaceTile as child of FSO.
public abstract class FeSurfaceElement {

	protected Bitmap mBitmap = null;
	public final int mWidth;
	public final int mHeight;

	// TODO final + sync block or volatile?
	private final Matrix mMatrix;
	private final float[] mMatrixValues = new float[9];

	// TODO right way with seperate rotation matrix? -> better do it with pre-
	// and postRotate() and store only the current rotation degrees
	private final Matrix mRotationMatrix = new Matrix();
	private float mRotationDegrees = 0f;
	private float mRotationAroundCenterDegrees = 0f;

	protected FeSurfaceElement mParent;

	// TODO add translate modifier to layers. e.g. 0f equals no translation, 1f
	// full
	private SparseArray<ArrayList<FeSurfaceElement>> mChildLayers;
	// TODO default layer
	public static int BACKGROUND_LAYER = 10;
	public static int FOREGROUND_LAYER = 25;
	private int mMaxLayerLevel = 0;

	private int mDoUpdateInterval;
	private long mDoUpdateCounter;

	private int mAnimationInterval;
	private int mAnimationCounter;
	private int mAnimationSteps;
	private int mAnimationCurrentIndex;
	// TODO rename
	private int mAnimationStepRootCeiled;
	private int mAnimationWidth;
	private int mAnimationHeight;
	private Rect mAnimationSrcRect;
	private Rect mAnimationDstRect;

	private FeSoundElement mSoundElement = null;
	private boolean m3DSound = false;

	public FeSurfaceElement(final int width, final int height) {
		mWidth = width;
		mHeight = height;
		mMatrix = new Matrix();
		mChildLayers = new SparseArray<ArrayList<FeSurfaceElement>>();
	}

	public FeSurfaceElement(final Bitmap bitmap) {
		this(bitmap.getWidth(), bitmap.getHeight());
		mBitmap = bitmap;
	}

	public void setTranslate(final float translateX, final float translateY) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrixValues[Matrix.MTRANS_X] = translateX;
			mMatrixValues[Matrix.MTRANS_Y] = translateY;
			mMatrix.setValues(mMatrixValues);
		}
	}

	public void addTranslate(final float translateX, final float translateY) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrixValues[Matrix.MTRANS_X] += translateX;
			mMatrixValues[Matrix.MTRANS_Y] += translateY;
			mMatrix.setValues(mMatrixValues);
		}
	}

	// TODO simplifiy. dont get values unnessersery often
	public float getTranslateX() {
		mMatrix.getValues(mMatrixValues);
		return mMatrixValues[Matrix.MTRANS_X];
	}

	public float getTranslateY() {
		mMatrix.getValues(mMatrixValues);
		return mMatrixValues[Matrix.MTRANS_Y];
	}

	// TODO make all methods final, if possible
	public final Point getAbsoluteSurfacePosition() {
		if (mParent == null) {
			return new Point();
		} else {
			final Point cords = mParent.getAbsoluteSurfacePosition();
			mMatrix.getValues(mMatrixValues);
			cords.x += mMatrixValues[Matrix.MTRANS_X];
			cords.y += mMatrixValues[Matrix.MTRANS_Y];
			return cords;
		}
	}

	// TAI make depending on display density?
	public void setScale(final float scaleX, final float scaleY) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrixValues[Matrix.MSCALE_X] = scaleX;
			mMatrixValues[Matrix.MSCALE_Y] = scaleY;
			mMatrix.setValues(mMatrixValues);
		}
	}

	public void postScale(final float scaleX, final float scaleY) {
		synchronized (mMatrix) {
			mMatrix.postScale(scaleX, scaleY);
		}
	}

	// WIP !!!! scaling needs to be checked everywhere. -> onTouch, map (?), ...
	public void postScale(final float scaleX, final float scaleY,
			final float pointX, final float pointY) {
		synchronized (mMatrix) {
			mMatrix.postScale(scaleX, scaleY, pointX, pointY);
		}
	}

	public void addScale(final float scaleX, final float scaleY) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrixValues[Matrix.MSCALE_X] += scaleX;
			mMatrixValues[Matrix.MSCALE_Y] += scaleY;
			mMatrix.setValues(mMatrixValues);
		}
	}

	public void setRotate(final float degrees) {
		synchronized (mMatrix) {
			mRotationDegrees = degrees;
			mRotationMatrix.setRotate(mRotationDegrees);
			mMatrix.postConcat(mRotationMatrix);
		}
	}

	public void addRotate(final float degrees) {
		synchronized (mMatrix) {
			mRotationDegrees += degrees;
			mRotationMatrix.setRotate(mRotationDegrees);
			mMatrix.postConcat(mRotationMatrix);
		}
	}

	public void setRotateAroundCenter(final float degrees) {
		synchronized (mMatrix) {
			mRotationAroundCenterDegrees = degrees;
			mMatrix.getValues(mMatrixValues);
			mRotationMatrix.setRotate(mRotationAroundCenterDegrees,
					mMatrixValues[Matrix.MTRANS_X],
					mMatrixValues[Matrix.MTRANS_Y]);
			mMatrix.postConcat(mRotationMatrix);
		}
	}

	public void addRotateAroundCenter(final float degrees) {
		synchronized (mMatrix) {
			mRotationAroundCenterDegrees += degrees;
			mMatrix.getValues(mMatrixValues);
			mRotationMatrix.setRotate(mRotationAroundCenterDegrees,
					mMatrixValues[Matrix.MTRANS_X],
					mMatrixValues[Matrix.MTRANS_Y]);
			mMatrix.postConcat(mRotationMatrix);
		}
	}

	public void addChild(final FeSurfaceElement child) {
		addChild(FOREGROUND_LAYER, child);
	}

	public void addChild(final int layerLevel, final FeSurfaceElement child) {
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

	// TODO only draw visible elements
	public final void draw(final Canvas canvas, final Paint paint) {
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

	public final void update(final long elapsedMillis) {
		synchronized (mMatrix) {
			onUpdate(elapsedMillis);

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

			if (mAnimationInterval > 0) {
				mAnimationCounter -= elapsedMillis;
				if (mAnimationCounter <= 0) {
					mAnimationCounter += mAnimationInterval;
					if (++mAnimationCurrentIndex >= mAnimationSteps) {
						mAnimationCurrentIndex = 0;
					}
					final int left = (mAnimationCurrentIndex % mAnimationStepRootCeiled)
							* mAnimationWidth;
					final int top = (mAnimationCurrentIndex / mAnimationStepRootCeiled)
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

	public final void setUpdateInterval(final int updateInterval) {
		mDoUpdateCounter = mDoUpdateInterval = updateInterval;
	}

	// TAI start and stop animation
	public void setAnimation(final int stepCount, final int animationInterval) {
		mAnimationCounter = mAnimationInterval = animationInterval;

		mAnimationSteps = stepCount;
		mAnimationCurrentIndex = 0;

		mAnimationStepRootCeiled = (int) Math.ceil(Math.sqrt(stepCount));
		mAnimationWidth = mWidth / mAnimationStepRootCeiled;
		mAnimationHeight = mHeight / mAnimationStepRootCeiled;

		mAnimationDstRect = new Rect(-mAnimationWidth / 2,
				-mAnimationHeight / 2, mAnimationWidth / 2,
				mAnimationHeight / 2);
		mAnimationSrcRect = new Rect(0, 0, mAnimationWidth, mAnimationHeight);
	}

	public void setSound(final Context context, final int resourceId,
			final FeSoundElement.LoadCompleteCallback callback) {
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

	// TAI change to mono, stereo and surround
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
		final float yMod = (FeSurface.OFFSET_Y + cords.y) / FeSurface.HEIGHT;
		mSoundElement.setChannelVolumes(leftVolume * yMod, rightVolume * yMod);
	}

	// TODO extract?
	public interface FeSurfaceTouchable {
		public boolean onTouch(final MotionEvent event);
	}
}
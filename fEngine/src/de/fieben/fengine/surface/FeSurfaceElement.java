package de.fieben.fengine.surface;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.SparseArray;

// TODO impl FeSurfaceObject as parent and FeSurfaceTile as child of FSO.
public abstract class FeSurfaceElement {

	protected Bitmap mBitmap = null;

	// TODO final + sync block or volatile?
	private final Matrix mMatrix;
	private final float[] mMatrixValues = new float[9];

	// TODO right way with seperate rotation matrix? -> better do it with pre-
	// and postRotate() and store only the current rotation degrees
	private final Matrix mRotationMatrix = new Matrix();
	private float mRotationDegrees = 0f;
	private float mRotationAroundCenterDegrees = 0f;
	// TODO and what about translate?
	private float mTranslateOffsetX;
	private float mTranslateOffsetY;

	// TODO add translate modifier to layers. e.g. 0f equals no translation, 1f
	// full
	private SparseArray<ArrayList<FeSurfaceElement>> mLayers;

	private int mDoUpdateInterval;
	private long mDoUpdateCounter;

	public FeSurfaceElement() {
		mMatrix = new Matrix();
		mLayers = new SparseArray<ArrayList<FeSurfaceElement>>();
	}

	public FeSurfaceElement(final Bitmap bitmap) {
		this();
		mBitmap = bitmap;
	}

	public void setTranslate(final float translateX, final float translateY) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrixValues[Matrix.MTRANS_X] = mTranslateOffsetX = translateX;
			mMatrixValues[Matrix.MTRANS_Y] = mTranslateOffsetY = translateY;
			mMatrix.setValues(mMatrixValues);
		}
	}

	public void addTranslate(final float translateX, final float translateY) {
		synchronized (mMatrix) {
			mMatrix.getValues(mMatrixValues);
			mMatrixValues[Matrix.MTRANS_X] = mTranslateOffsetX += translateX;
			mMatrixValues[Matrix.MTRANS_Y] = mTranslateOffsetY += translateY;
			mMatrix.setValues(mMatrixValues);
		}
	}

	// TODO make depending on display density?
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

	public static int BACKGROUND_LAYER = 10;
	public static int FOREGROUND_LAYER = 25;

	private int mMaxLayerLevel = 0;

	public void addChild(final int layerLevel, final FeSurfaceElement child) {
		final int layerIndex = mLayers.indexOfKey(layerLevel);
		if (layerIndex < 0) {
			final ArrayList<FeSurfaceElement> layer = new ArrayList<FeSurfaceElement>();
			layer.add(child);
			mLayers.put(layerLevel, layer);
		} else {
			mLayers.valueAt(layerIndex).add(child);
		}

		if (mMaxLayerLevel < layerLevel) {
			mMaxLayerLevel = layerLevel;
		}
	}

	public final void draw(final Canvas canvas, final Paint paint) {
		synchronized (mMatrix) {
			canvas.save();
			canvas.concat(mMatrix);

			// TODO only draw visible elements
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
				final int index = mLayers.indexOfKey(i);
				if (index >= 0) {
					for (final FeSurfaceElement e : mLayers.valueAt(index)) {
						e.draw(canvas, paint);
					}
				}
			}

			canvas.restore();
		}
	}

	public final void setUpdateInterval(final int updateInterval) {
		mDoUpdateCounter = mDoUpdateInterval = updateInterval;
	}

	private int mAnimationInterval;
	private int mAnimationCounter;
	private int mAnimationSteps;
	private int mAnimationCurrentIndex;
	private int mAnimationStepRootCeiled;
	private int mAnimationWidth;
	private int mAnimationHeight;
	private Rect mAnimationSrcRect;
	private Rect mAnimationDstRect;

	public void setAnimation(final int stepCount, final int animationInterval) {
		mAnimationCounter = mAnimationInterval = animationInterval;

		mAnimationSteps = stepCount;
		mAnimationCurrentIndex = 0;

		mAnimationStepRootCeiled = (int) Math.ceil(Math.sqrt(stepCount));
		mAnimationWidth = mBitmap.getWidth() / mAnimationStepRootCeiled;
		mAnimationHeight = mBitmap.getHeight() / mAnimationStepRootCeiled;

		mAnimationSrcRect = mAnimationDstRect = new Rect(0, 0, mAnimationWidth,
				mAnimationHeight);
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
				final int index = mLayers.indexOfKey(i);
				if (index >= 0) {
					for (final FeSurfaceElement e : mLayers.valueAt(index)) {
						e.update(elapsedMillis);
					}
				}
			}
		}
	}

	public abstract void onDraw(final Canvas canvas, final Paint paint);

	public abstract void onUpdate(final long elapsedMillis);

	public abstract void doUpdate();

	public float getTranslateX() {
		return mTranslateOffsetX;
	}

	public float getTranslateY() {
		return mTranslateOffsetY;
	}

	// TODO !!! impl onTouchEvent
}
package de.fieben.fengine.surface;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public abstract class FeSurfaceElement {

	private final Matrix mMatrix;
	private final float[] mMatrixValues = new float[9];

	// TODO right way with seperate rotation matrix?
	private final Matrix mRotationMatrix = new Matrix();
	private float mRotationDegrees = 0f;
	private float mRotationAroundCenterDegrees = 0f;

	// TODO what is the most performant data holder?
	private List<FeSurfaceElement> mChildren;

	private int mDoUpdateInterval;
	private long mDoUpdateCounter;

	public FeSurfaceElement() {
		mMatrix = new Matrix();
		mChildren = new ArrayList<FeSurfaceElement>();
	}

	public void setTranslate(final float translateX, final float translateY) {
		// TODO sync block needed?
		synchronized (mMatrixValues) {
			mMatrix.getValues(mMatrixValues);
			mMatrixValues[Matrix.MTRANS_X] = translateX;
			mMatrixValues[Matrix.MTRANS_Y] = translateY;
			mMatrix.setValues(mMatrixValues);
		}
	}

	public void addTranslate(final float translateX, final float translateY) {
		synchronized (mMatrixValues) {
			mMatrix.getValues(mMatrixValues);
			mMatrixValues[Matrix.MTRANS_X] += translateX;
			mMatrixValues[Matrix.MTRANS_Y] += translateY;
			mMatrix.setValues(mMatrixValues);
		}
	}

	public void setScale(final float scaleX, final float scaleY) {
		synchronized (mMatrixValues) {
			mMatrix.getValues(mMatrixValues);
			mMatrixValues[Matrix.MSCALE_X] = scaleX;
			mMatrixValues[Matrix.MSCALE_Y] = scaleY;
			mMatrix.setValues(mMatrixValues);
		}
	}

	public void addScale(final float scaleX, final float scaleY) {
		synchronized (mMatrixValues) {
			mMatrix.getValues(mMatrixValues);
			mMatrixValues[Matrix.MSCALE_X] += scaleX;
			mMatrixValues[Matrix.MSCALE_Y] += scaleY;
			mMatrix.setValues(mMatrixValues);
		}
	}

	public void setRotate(final float degrees) {
		mRotationDegrees = degrees;
		mRotationMatrix.setRotate(mRotationDegrees);
		mMatrix.postConcat(mRotationMatrix);
	}

	public void addRotate(final float degrees) {
		mRotationDegrees += degrees;
		mRotationMatrix.setRotate(mRotationDegrees);
		mMatrix.postConcat(mRotationMatrix);
	}

	public void setRotateAroundCenter(final float degrees) {
		synchronized (mMatrixValues) {
			mRotationAroundCenterDegrees = degrees;
			mMatrix.getValues(mMatrixValues);
			mRotationMatrix.setRotate(mRotationAroundCenterDegrees,
					mMatrixValues[Matrix.MTRANS_X],
					mMatrixValues[Matrix.MTRANS_Y]);
			mMatrix.postConcat(mRotationMatrix);
		}
	}

	public void addRotateAroundCenter(final float degrees) {
		synchronized (mMatrixValues) {
			mRotationAroundCenterDegrees += degrees;
			mMatrix.getValues(mMatrixValues);
			mRotationMatrix.setRotate(mRotationAroundCenterDegrees,
					mMatrixValues[Matrix.MTRANS_X],
					mMatrixValues[Matrix.MTRANS_Y]);
			mMatrix.postConcat(mRotationMatrix);
		}
	}

	public void addChild(final FeSurfaceElement child) {
		mChildren.add(child);
	}

	public final void draw(final Canvas canvas, final Paint paint) {
		canvas.save();
		canvas.concat(mMatrix);
		onDraw(canvas, paint);
		for (final FeSurfaceElement e : mChildren) {
			e.draw(canvas, paint);
		}
		canvas.restore();
	}

	public final void setUpdateInterval(final int updateInterval) {
		mDoUpdateInterval = updateInterval;
		mDoUpdateCounter = updateInterval;
	}

	public final void update(final long elapsedMillis) {
		onUpdate(elapsedMillis);
		for (final FeSurfaceElement e : mChildren) {
			e.update(elapsedMillis);
		}

		if (mDoUpdateInterval > 0) {
			mDoUpdateCounter -= elapsedMillis;
			if (mDoUpdateCounter <= 0) {
				mDoUpdateCounter += mDoUpdateInterval;
				doUpdate();
			}
		}
	}

	public abstract void onDraw(final Canvas canvas, final Paint paint);

	public abstract void onUpdate(final long elapsedMillis);

	public abstract void doUpdate();
}
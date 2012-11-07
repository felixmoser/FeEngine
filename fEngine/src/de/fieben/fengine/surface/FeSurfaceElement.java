package de.fieben.fengine.surface;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public abstract class FeSurfaceElement {

	private final Matrix mMatrix;
	private final float[] mMatrixValues = new float[9];
	// TODO what is the most performant data holder?
	private List<FeSurfaceElement> mChildren;
	private long mUpdateInterval;
	private long mLastDoUpdate;

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
		mMatrix.setRotate(degrees);
		// synchronized (mMatrixValues) {
		// mMatrix.getValues(mMatrixValues);
		// mMatrixValues[Matrix.] += scaleX;
		// mMatrixValues[Matrix.MSCALE_Y] += scaleY;
		// mMatrix.setValues(mMatrixValues);
		// }
	}

	public void addRotate(final float degrees) {
		mMatrix.setRotate(degrees);
		// mMatrix.
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

	public final void setUpdateInterval(final long updateInterval) {
		mUpdateInterval = updateInterval;
	}

	public final void update(final long elapsedMillis) {
		onUpdate(elapsedMillis);
		if (mUpdateInterval > 0
				&& mLastDoUpdate + mUpdateInterval <= System
						.currentTimeMillis()) {
			doUpdate();
			mLastDoUpdate = System.currentTimeMillis();
		}
		for (final FeSurfaceElement e : mChildren) {
			e.update(elapsedMillis);
		}
	}

	public abstract void onDraw(final Canvas canvas, final Paint paint);

	public abstract void onUpdate(final long elapsedMillis);

	public abstract void doUpdate();
}
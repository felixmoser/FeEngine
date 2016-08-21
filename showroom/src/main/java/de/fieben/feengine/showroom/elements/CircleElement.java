package de.fieben.feengine.showroom.elements;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import de.fieben.feengine.FeSurface;
import de.fieben.feengine.FeSurfaceElement;

public class CircleElement extends FeSurfaceElement {

	protected final int mRadius;
	private int mColor;
	private boolean mCenterRotation;
	private boolean mNormalRotation;
	private float mRotateAroundParentSpeed;
	private float mRotateAroundCenterSpeed;

	public CircleElement(final FeSurface feSurface, final int radius) {
		super(feSurface, -1);
		mRadius = radius;
		mColor = Color.WHITE;
	}

	public void setColor(final int color) {
		mColor = color;
	}

	@Override
	public void onDraw(final Canvas canvas, final Paint paint) {
		paint.setColor(mColor);
		canvas.drawCircle(0, 0, mRadius, paint);
	}

	public void enableRotationAroundCenter() {
		mColor = Color.RED;
		mRotateAroundParentSpeed = 360f / 4000;
		mRotateAroundCenterSpeed = -360f / 2000;
		mCenterRotation = true;
		setUpdateInterval(1000);
	}

	public void setContinuousRotationAroundParent(final int secondsPerRound) {
		mRotateAroundParentSpeed = 360f / (secondsPerRound * 1000);
		mNormalRotation = true;
	}

	@Override
	public void onUpdate(final long elapsedMillis) {
		if (mCenterRotation) {
			addRotate(mRotateAroundParentSpeed * elapsedMillis);
			addRotateAroundCenter(mRotateAroundCenterSpeed * elapsedMillis);
		} else if (mNormalRotation) {
			addRotate(-mRotateAroundParentSpeed * elapsedMillis);
		}
	}

	@Override
	public void doUpdate() {
		if (mCenterRotation) {
			mColor = mColor == Color.RED ? Color.GREEN : Color.RED;
		}
	}
}

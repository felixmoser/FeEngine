package de.fieben.feengine.showroom.elements;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import de.fieben.feengine.FeSurfaceElement;

public class CircleElement extends FeSurfaceElement {

	protected final int mRadius;
	private int mColor;
	private boolean mUpdateTestRotation, mNormalRotation = false;
	private float mRotateAroundParentSpeed, mRotateArountCenterSpeed;

	public CircleElement(final int radius) {
		super(null);
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

	public void setUpdateTestRotation(final boolean rotation) {
		mColor = Color.RED;
		mRotateAroundParentSpeed = 360f / 4000;
		mRotateArountCenterSpeed = -180f / 1000;
		mUpdateTestRotation = rotation;
		setUpdateInterval(1000);
	}

	public void setNormalTestRotation(final boolean rotation) {
		mRotateAroundParentSpeed = 360f / 8000;
		mNormalRotation = rotation;
	}

	public void setFastTestRotation(final boolean rotation) {
		mRotateAroundParentSpeed = 360f / 4000;
		mNormalRotation = rotation;
	}

	@Override
	public void onUpdate(final long elapsedMillis) {
		if (mUpdateTestRotation) {
			addRotate(mRotateAroundParentSpeed * elapsedMillis);
			addRotateAroundCenter(mRotateArountCenterSpeed * elapsedMillis);
		} else if (mNormalRotation) {
			addRotate(mRotateAroundParentSpeed * elapsedMillis);
		}
	}

	@Override
	public void doUpdate() {
		if (mUpdateTestRotation) {
			mColor = mColor == Color.RED ? Color.GREEN : Color.RED;
		}
	}
}

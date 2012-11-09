package de.fieben.fengine.showroom.elements;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import de.fieben.fengine.surface.FeSurfaceElement;

public class CircleElement extends FeSurfaceElement {

	private final int mRadius;
	private int mColor;
	private boolean mWeirdRotation;
	private float mRotateAroundParentSpeed;
	private float mRotateArountCenterSpeed;

	public CircleElement(final int radius) {
		mRadius = radius;
		mColor = Color.WHITE;
	}

	public void setColor(final int color) {
		mColor = color;
	}

	public void setWeirdRotation(final boolean rotation) {
		mColor = Color.RED;
		mRotateAroundParentSpeed = 360f / 4000;
		mRotateArountCenterSpeed = -180f / 1000;
		mWeirdRotation = rotation;
		setUpdateInterval(1000);
	}

	@Override
	public void onDraw(final Canvas canvas, final Paint paint) {
		paint.setColor(mColor);
		canvas.drawCircle(0, 0, mRadius, paint);
	}

	@Override
	public void onUpdate(final long elapsedMillis) {
		if (mWeirdRotation) {
			addRotate(mRotateAroundParentSpeed * elapsedMillis);
			addRotateAroundCenter(mRotateArountCenterSpeed * elapsedMillis);
		}
	}

	@Override
	public void doUpdate() {
		if (mWeirdRotation) {
			mColor = mColor == Color.RED ? Color.GREEN : Color.RED;
		}
	}
}

package de.fieben.fengine.showroom.elements;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import de.fieben.fengine.surface.FeSurfaceElement;

public class CircleElement extends FeSurfaceElement {

	private final int mRadius;
	private int mColor;

	public CircleElement(final int radius) {
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

	private boolean mRandomMovement;
	private long mLastMoveTime;
	// speed in pixel/second
	private int mSpeed;
	private int mMoveX = 5;
	private int mMoveY = -5;

	@Override
	public void onUpdate(final long elapsedMillis) {
		if (mRandomMovement) {
			// if (mLastMoveTime + mSpeed <= System.currentTimeMillis()) {
			// mLastMoveTime = System.currentTimeMillis();
			if (mMoveX > -20) {
				addTranslate(mMoveX--, mMoveY++);
			} else {
				addTranslate(mMoveX++, mMoveY--);
			}
			// }
		}
	}

	@Override
	public void doUpdate() {
		// mColor = mColor == Color.BLUE ? Color.RED : Color.BLUE;
	}

	public void setRandomMovement(final boolean movement) {
		mRandomMovement = movement;
	}
}

package de.fieben.feengine.showroom.scenes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import de.fieben.feengine.FeSurface;
import de.fieben.feengine.FeSurfaceElement;
import de.fieben.feengine.showroom.elements.CircleElement;

public class UpdateTestScene extends FeSurface {
	private final CircleElement mInnerCircle;

	public UpdateTestScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		mInnerCircle = new CircleElement(this, 50);
		mInnerCircle.setColor(Color.BLUE);

		final CircleElement orbitEllipse = new CircleElement(this, 15);
		orbitEllipse.setTranslate(0, -125);
		orbitEllipse.setScaleFromCenter(0.5f, 3f);
		orbitEllipse.enableUpdateTestRotation();

		mInnerCircle.addChild(orbitEllipse);
		addElement(mInnerCircle);
	}

	@Override
	public void surfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height) {
		final float scale = 1000 / getResources().getDisplayMetrics().densityDpi;
		setSurfaceScale(scale, scale, width / 2, height / 2);

		mInnerCircle.setTranslate(width / 2, height / 2);

		addBackgroundElement(new CrossElement(mInnerCircle.getAbsoluteSurfacePosition(), height, width));

		super.surfaceChanged(holder, format, width, height);
	}

	private class CrossElement extends FeSurfaceElement {
		private final Point mVerticalTop;
		private final Point mVerticalBottom;
		private final Point mHorizontalLeft;
		private final Point mHorizontalRight;

		public CrossElement(final Point center, final int height, final int width) {
			super(UpdateTestScene.this, -1);
			mVerticalTop = new Point(center.x, 0);
			mVerticalBottom = new Point(center.x, height);
			mHorizontalLeft = new Point(0, center.y);
			mHorizontalRight = new Point(width, center.y);
		}

		@Override
		public void onDraw(final Canvas canvas, final Paint paint) {
			paint.setColor(Color.BLACK);
			canvas.drawLine(mVerticalTop.x, mVerticalTop.y, mVerticalBottom.x, mVerticalBottom.y, paint);
			canvas.drawLine(mHorizontalLeft.x, mHorizontalLeft.y, mHorizontalRight.x, mHorizontalRight.y, paint);
		}

		@Override
		public void onUpdate(final long elapsedMillis) {
		}

		@Override
		public void doUpdate() {
		}
	}
}
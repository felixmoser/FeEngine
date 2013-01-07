package de.fieben.feengine.showroom.elements;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import de.fieben.feengine.FeSurfaceElement;

// TODO class needed?
public class RectangleElement extends FeSurfaceElement {

	private final Rect mDimensions;
	private int mColor;

	public RectangleElement(final int width, final int height,
			final boolean centerAnchor) {
		super(width, height);
		// TODO replace centerAnchor with flag. eg. x-achses center
		if (centerAnchor) {
			mDimensions = new Rect(-width / 2, -height / 2, width / 2,
					height / 2);
		} else {
			mDimensions = new Rect(0, 0, width, height);
		}
		mColor = Color.WHITE;
	}

	public void setColor(final int color) {
		mColor = color;
	}

	@Override
	public void onDraw(final Canvas canvas, final Paint paint) {
		paint.setColor(mColor);
		canvas.drawRect(mDimensions, paint);
	}

	@Override
	public void onUpdate(final long elapsedMillis) {
	}

	@Override
	public void doUpdate() {
	}
}

package de.fieben.fengine.surface.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import de.fieben.fengine.surface.FeSurfaceElement;

public class FeRootElementImpl extends FeSurfaceElement {
	private int mBackgroundColor;

	public FeRootElementImpl(final int backgroundColor) {
		mBackgroundColor = backgroundColor;
	}

	@Override
	public void onDraw(final Canvas canvas, final Paint paint) {
		canvas.drawColor(mBackgroundColor);
	}

	@Override
	public void onUpdate(final long elapsedMillis) {
		// android.util.Log.i("testing", "::Root.onUpdate:: ");
	}

	@Override
	public void doUpdate() {
		// android.util.Log.i("testing", "::Root.doUpdate:: ");
	}
}

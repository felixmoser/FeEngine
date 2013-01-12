package de.fieben.feengine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class FeSurfaceTile {
	final int mBitmapKey;

	// TODO comment like element
	public FeSurfaceTile(final int bitmapKey) {
		mBitmapKey = bitmapKey;
	}

	protected final void draw(final Canvas canvas, final int x, final int y,
			final Paint paint) {
		if (mBitmapKey != -1) {
			canvas.drawBitmap(getBitmap(), x, y, paint);
		}
		onDraw(canvas, x, y, paint);
	}

	Bitmap getBitmap() {
		return FeBitmapPool.getBitmap(mBitmapKey);
	}

	public abstract void onDraw(final Canvas canvas, final int x, final int y,
			final Paint paint);
}
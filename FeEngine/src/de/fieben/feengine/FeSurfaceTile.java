package de.fieben.feengine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class FeSurfaceTile {
	final Bitmap mBitmap;

	public FeSurfaceTile(final Bitmap bitmap) {
		mBitmap = bitmap;

	}

	protected final void draw(final Canvas canvas, final int x, final int y,
			final Paint paint) {
		if (mBitmap != null) {
			canvas.drawBitmap(mBitmap, x, y, paint);
		}
		onDraw(canvas, x, y, paint);
	}

	public abstract void onDraw(final Canvas canvas, final int x, final int y,
			final Paint paint);
}
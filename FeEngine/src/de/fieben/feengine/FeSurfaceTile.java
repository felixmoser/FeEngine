package de.fieben.feengine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class FeSurfaceTile {
	private Bitmap mBitmap = null;
	protected int mWidth;
	protected int mHeight;

	public FeSurfaceTile(final Bitmap bitmap) {
		mWidth = bitmap.getWidth();
		mHeight = bitmap.getHeight();
		mBitmap = bitmap;
	}

	protected final void draw(final Canvas canvas, final int x, final int y,
			final Paint paint) {
		canvas.drawBitmap(mBitmap, x, y, paint);
		onDraw(canvas, x, y, paint);
	}

	public abstract void onDraw(final Canvas canvas, final int x, final int y,
			final Paint paint);
}
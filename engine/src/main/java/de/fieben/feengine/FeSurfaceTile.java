package de.fieben.feengine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * This class delivers the content for {@link FeSurfaceMap} and represents a
 * single cell in its grid. It doesn't hold position parameters, they get's
 * delivered as parameter in {@link #onDraw(canvas, x, y, paint)}.
 * 
 * @author Felix Moser - felix.ernesto.moser@googlemail.com
 * 
 */
public abstract class FeSurfaceTile {
	final int mBitmapKey;

	/**
	 * @param bitmapKey
	 *            The key retrieved from {@link FeBitmapPool} after adding a
	 *            bitmap. -1 if no bitmap should be used.
	 */
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

	/**
	 * This method is used to do custom drawing of this element.
	 * 
	 * @param canvas
	 *            The {@link Canvas} to draw on.
	 * @param x
	 *            The x-coordinate to draw this tile on.
	 * @param y
	 *            The y-coordinate to draw this tile on.
	 * @param paint
	 *            The {@link Paint} used for drawing.
	 */
	public abstract void onDraw(final Canvas canvas, final int x, final int y,
			final Paint paint);
}
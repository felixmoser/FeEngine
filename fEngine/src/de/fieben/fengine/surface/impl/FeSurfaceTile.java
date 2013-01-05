package de.fieben.fengine.surface.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

// TODO make comments everywhere!
/**
 * @author Felix Moser - felix.ernesto.moser@gmail.com
 */
public abstract class FeSurfaceTile {

	protected Bitmap mBitmap = null;
	protected int mId;
	protected int mBackgroundColor;
	protected int mWidth;
	protected int mHeight;

	// TODO extend surface element?
	public FeSurfaceTile(final int id, final int backgroundColor,
			final int width, final int height) {
		mId = id;
		mBackgroundColor = backgroundColor;
		mWidth = width;
		mHeight = height;
	}

	public FeSurfaceTile(final int id, final Bitmap bitmap) {
		mId = id;
		mBackgroundColor = Color.WHITE;
		mWidth = bitmap.getWidth();
		mHeight = bitmap.getHeight();
		mBitmap = bitmap;
	}

	public void draw(final Canvas canvas, final int x, final int y,
			final Paint paint) {
		paint.setColor(mBackgroundColor);
		if (mBitmap != null) {
			canvas.drawBitmap(mBitmap, x, y, paint);
		} else {
			canvas.drawRect(x, y, x + mWidth, y + mHeight, paint);
		}
	}
}
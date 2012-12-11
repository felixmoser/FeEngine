package de.fieben.fengine.surface;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

// TODO make comments everywhere!
/**
 * Tiles are always in the background.
 * 
 * @author Felix Moser - felix.ernesto.moser@gmail.com
 */
public class FeSurfaceTile {

	private int mId;
	private int mBackgroundColor;
	public int mWidth;
	public int mHeight;

	public FeSurfaceTile(final int id, final int backgroundColor,
			final int width, final int height) {
		mId = id;
		mBackgroundColor = backgroundColor;
		mWidth = width;
		mHeight = height;
	}

	public void draw(final Canvas canvas, final int x, final int y,
			final Paint paint) {
		paint.setColor(mBackgroundColor);
		canvas.drawRect(x, y, x + mWidth, y + mHeight, paint);

		// WIP
		paint.setColor(mBackgroundColor == Color.BLACK ? Color.WHITE
				: Color.BLACK);
		canvas.drawText(String.valueOf(mId + 1), x + 5, y + 10, paint);
	}
}
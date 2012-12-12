package de.fieben.fengine.showroom.elements;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import de.fieben.fengine.surface.FeSurfaceElement;

public class GridElement extends FeSurfaceElement {

	private int mColor = Color.BLACK;
	private int mSpacing = 0;
	private float[] mGrid;

	public GridElement(final int spacing, final int width, final int height) {
		mSpacing = spacing;
		// TODO method needed?
		calculateGrid(width, height);
	}

	public void setColor(final int gridColor) {
		mColor = gridColor;
	}

	@Override
	public void onDraw(final Canvas canvas, final Paint paint) {
		drawGrid(canvas, paint);
	}

	// TODO the grid needs to be "floating"
	private void drawGrid(final Canvas canvas, final Paint paint) {
		paint.setColor(mColor);
		canvas.drawLines(mGrid, paint);
	}

	private void calculateGrid(final int width, final int height) {
		if (mSpacing > 0) {
			mGrid = new float[(width / mSpacing + height / mSpacing) * 4];
			int nextGridIndex = 0;
			for (int nextXValue = mSpacing; nextXValue < width; nextXValue += mSpacing) {
				mGrid[nextGridIndex++] = nextXValue;
				mGrid[nextGridIndex++] = 0;
				mGrid[nextGridIndex++] = nextXValue;
				mGrid[nextGridIndex++] = height;
			}
			for (int nextYValue = mSpacing; nextYValue < height; nextYValue += mSpacing) {
				mGrid[nextGridIndex++] = 0;
				mGrid[nextGridIndex++] = nextYValue;
				mGrid[nextGridIndex++] = width;
				mGrid[nextGridIndex++] = nextYValue;
			}
		}
	}

	@Override
	public void onUpdate(final long elapsedMillis) {
		// TODO update grid (offset) here?
	}

	@Override
	public void doUpdate() {
	}
}

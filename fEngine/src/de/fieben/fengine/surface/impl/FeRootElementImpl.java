package de.fieben.fengine.surface.impl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import de.fieben.fengine.surface.FeSurfaceElement;

public class FeRootElementImpl extends FeSurfaceElement {
	private int mBackgroundColor;
	private int mGridColor = Color.BLACK;
	private float[] mGrid;
	private int mGridSpacing;

	public FeRootElementImpl(final int backgroundColor, final int gridSpacing) {
		mBackgroundColor = backgroundColor;
		if (gridSpacing > 0) {
			mGridSpacing = gridSpacing;
		}
	}

	@Override
	public void onDraw(final Canvas canvas, final Paint paint) {
		canvas.drawColor(mBackgroundColor);
		if (mGrid != null) {
			paint.setColor(mGridColor);
			canvas.drawLines(mGrid, paint);
		}
	}

	public void calculateGrid(final int width, final int height) {
		if (mGridSpacing > 0) {
			mGrid = new float[(width / mGridSpacing + height / mGridSpacing) * 4];
			int nextGridIndex = 0;
			for (int nextXValue = mGridSpacing; nextXValue < width; nextXValue += mGridSpacing) {
				mGrid[nextGridIndex++] = nextXValue;
				mGrid[nextGridIndex++] = 0;
				mGrid[nextGridIndex++] = nextXValue;
				mGrid[nextGridIndex++] = height;
			}
			for (int nextYValue = mGridSpacing; nextYValue < height; nextYValue += mGridSpacing) {
				mGrid[nextGridIndex++] = 0;
				mGrid[nextGridIndex++] = nextYValue;
				mGrid[nextGridIndex++] = width;
				mGrid[nextGridIndex++] = nextYValue;
			}
		}
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

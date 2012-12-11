package de.fieben.fengine.surface.impl;

import java.util.HashMap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import de.fieben.fengine.surface.FeSurfaceElement;
import de.fieben.fengine.surface.FeSurfaceTile;

public class FeRootElementImpl extends FeSurfaceElement {
	private int mVoidColor;

	// WIP what to do with the grid? stash?
	// private int mGridColor = Color.BLACK;
	// private float[] mGrid;
	// private int mGridSpacing;

	private final int mRowCount;
	private final int mColumnCount;
	private final HashMap<Integer, HashMap<Integer, FeSurfaceTile>> mBackground;

	private HashMap<Integer, FeSurfaceTile> mNextRow;
	private FeSurfaceTile mNextTile = null;
	private int mBackgroundDrawOffsetX = 0;
	private int mBackgroundDrawOffsetY = 0;

	// TODO better construktor param usage/handling
	public FeRootElementImpl(final int rowCount, final int columnCount) {
		mRowCount = rowCount;
		mColumnCount = columnCount;
		mBackground = new HashMap<Integer, HashMap<Integer, FeSurfaceTile>>();

		if (mRowCount > 0 && mColumnCount > 0) {
			int id = 0;
			boolean colorBlack = true;
			for (int i = 0; i < mRowCount; i++) {
				mNextRow = new HashMap<Integer, FeSurfaceTile>();
				for (int j = 0; j < mColumnCount; j++) {
					// TODO set tiles with params or method (and add a mTileMode
					// inside)
					mNextRow.put(j, new FeSurfaceTile(id++,
							colorBlack ? Color.BLACK : Color.WHITE, 100, 100));
					colorBlack = !colorBlack;
				}
				mBackground.put(i, mNextRow);
				if (mRowCount % 2 == 0) {
					colorBlack = !colorBlack;
				}
			}
		}
	}

	public void setVoidColor(final int VoidColor) {
		mVoidColor = VoidColor;
	}

	public void enableGrid(final int gridSpacing) {
		// mGridSpacing = gridSpacing;
	}

	public void setGridColor(final int gridColor) {
		// mGridColor = gridColor;
	}

	public void disableGrid() {
		// mGridSpacing = 0;
	}

	@Override
	public void onDraw(final Canvas canvas, final Paint paint) {
		canvas.drawColor(mVoidColor);

		if (mRowCount > 0 && mColumnCount > 0) {
			mBackgroundDrawOffsetY = 0;
			for (int i = 0; i < mRowCount; i++) {
				mBackgroundDrawOffsetX = 0;
				mNextRow = mBackground.get(i);
				for (int j = 0; j < mColumnCount; j++) {
					mNextTile = mNextRow.get(j);
					mNextTile.draw(canvas, mBackgroundDrawOffsetX,
							mBackgroundDrawOffsetY, paint);
					// WIP remove spacing
					mBackgroundDrawOffsetX += mNextTile.mWidth + 5;
				}
				mBackgroundDrawOffsetY += mNextTile.mHeight + 5;
			}
		}

		// if (mGrid != null) {
		// paint.setColor(mGridColor);
		// drawGrid(canvas, paint);
		// }
	}

	// TODO the grid needs to be "floating"
	// private void drawGrid(final Canvas canvas, final Paint paint) {
	// canvas.drawLines(mGrid, paint);
	// }

	public void calculateGrid(final int width, final int height) {
		// if (mGridSpacing > 0) {
		// mGrid = new float[(width / mGridSpacing + height / mGridSpacing) *
		// 4];
		// int nextGridIndex = 0;
		// for (int nextXValue = mGridSpacing; nextXValue < width; nextXValue +=
		// mGridSpacing) {
		// mGrid[nextGridIndex++] = nextXValue;
		// mGrid[nextGridIndex++] = 0;
		// mGrid[nextGridIndex++] = nextXValue;
		// mGrid[nextGridIndex++] = height;
		// }
		// for (int nextYValue = mGridSpacing; nextYValue < height; nextYValue
		// += mGridSpacing) {
		// mGrid[nextGridIndex++] = 0;
		// mGrid[nextGridIndex++] = nextYValue;
		// mGrid[nextGridIndex++] = width;
		// mGrid[nextGridIndex++] = nextYValue;
		// }
		// }
	}

	@Override
	public void onUpdate(final long elapsedMillis) {
		// TODO update grid (offset) here?
	}

	@Override
	public void doUpdate() {
	}
}

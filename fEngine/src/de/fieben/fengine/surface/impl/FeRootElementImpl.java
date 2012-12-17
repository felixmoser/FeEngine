package de.fieben.fengine.surface.impl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.SparseArray;
import de.fieben.fengine.surface.FeSurfaceElement;
import de.fieben.fengine.surface.FeSurfaceTile;

public class FeRootElementImpl extends FeSurfaceElement {
	private int mVoidColor;

	// TODO set tiles with params or method, and add a mTileMode
	private final SparseArray<SparseArray<FeSurfaceTile>> mBackgroundTiles;
	// TODO same with tile size for X and Y;
	private int mTileSize = 100;

	// TODO better construktor param usage/handling
	public FeRootElementImpl(final int rowCount, final int columnCount) {
		if (rowCount > 0 && columnCount > 0) {
			mBackgroundTiles = new SparseArray<SparseArray<FeSurfaceTile>>(
					rowCount);

			int id = 0;
			boolean colorBlack = true;
			for (int i = 0; i < rowCount; i++) {

				final SparseArray<FeSurfaceTile> row = new SparseArray<FeSurfaceTile>(
						columnCount);
				for (int j = 0; j < columnCount; j++) {
					row.append(j, new FeSurfaceTile(id++,
							colorBlack ? Color.LTGRAY : Color.WHITE, mTileSize,
							mTileSize));
					colorBlack = !colorBlack;
				}
				mBackgroundTiles.append(i, row);
				if (rowCount % 2 == 0) {
					colorBlack = !colorBlack;
				}
			}
		} else {
			mBackgroundTiles = null;
		}
	}

	public void setVoidColor(final int VoidColor) {
		mVoidColor = VoidColor;
	}

	@Override
	public void onDraw(final Canvas canvas, final Paint paint) {
		canvas.drawColor(mVoidColor);

		if (mBackgroundTiles != null) {
			mTileCountDrawn = 0;

			final int firstVisibleRow = limit(
					((int) -mTranslateOffsetY / mTileSize),
					mBackgroundTiles.size());
			final int lastVisibleRow = limit((mSurfaceHeight
					- (int) mTranslateOffsetY + mTileSize)
					/ mTileSize, mBackgroundTiles.size());
			int drawOffsetY = firstVisibleRow * mTileSize;

			for (int i = firstVisibleRow; i < lastVisibleRow; i++) {
				final SparseArray<FeSurfaceTile> row = mBackgroundTiles.get(i);

				final int firstVisibleColumn = limit(
						((int) -mTranslateOffsetX / mTileSize), row.size());
				final int lastVisibleColumn = limit((mSurfaceWidth
						- (int) mTranslateOffsetX + mTileSize)
						/ mTileSize, row.size());
				int drawOffsetX = firstVisibleColumn * mTileSize;

				for (int j = firstVisibleColumn; j < lastVisibleColumn; j++) {
					row.get(j).draw(canvas, drawOffsetX, drawOffsetY, paint);
					mTileCountDrawn++;
					drawOffsetX += mTileSize;
				}
				drawOffsetY += mTileSize;
			}
		}
	}

	int mTileCountDrawn = 0;

	// WIP enable in debug mode
	public String getDebugOutput() {
		return "drawn tiles: " + mTileCountDrawn;
	}

	private int limit(final int value, final int limit) {
		return Math.max(0, Math.min(value, limit));
	}

	private int mSurfaceWidth;
	private int mSurfaceHeight;

	public void updateSurfaceSize(final int width, final int height) {
		mSurfaceWidth = width;
		mSurfaceHeight = height;
	}

	@Override
	public void onUpdate(final long elapsedMillis) {
	}

	@Override
	public void doUpdate() {
	}
}

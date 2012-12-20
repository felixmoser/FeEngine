package de.fieben.fengine.surface.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.SparseArray;
import de.fieben.fengine.surface.FeSurfaceElement;

public class FeSurfaceMap extends FeSurfaceElement {

	public static enum MODE {
		NORMAL, ISOMETRIC
	};

	private final MODE mMode;

	private final FeRootElementImpl mRootElement;

	// TODO set tiles with params or method, and add a mTileMode
	private final SparseArray<SparseArray<FeSurfaceTile>> mBackgroundTiles;
	// TODO same with tile size for X and Y;
	private int mTileSize = 250;

	private int mRowCount = 0;
	private int mColumnCount = 0;

	public FeSurfaceMap(final FeRootElementImpl rootElement, final MODE mode,
			final int rowCount, final int columnCount, final Bitmap tileBitmap) {
		mRootElement = rootElement;

		mBackgroundTiles = new SparseArray<SparseArray<FeSurfaceTile>>(rowCount);
		// WIP this creation needs to get triggerd from FeSurface
		// implementations
		int id = 0;
		boolean colorBlack = true;
		for (int i = 0; i < rowCount; i++) {

			final SparseArray<FeSurfaceTile> row = new SparseArray<FeSurfaceTile>(
					columnCount);
			for (int j = 0; j < columnCount; j++) {
				// WIP tile background color
				if (tileBitmap != null) {
					row.append(j,
							new FeSurfaceTile(id++, colorBlack ? Color.LTGRAY
									: Color.WHITE, tileBitmap));
				} else {
					row.append(j, new FeSurfaceTile(id++,
							colorBlack ? Color.LTGRAY : Color.WHITE, mTileSize,
							mTileSize));
				}
				colorBlack = !colorBlack;
			}
			mBackgroundTiles.append(i, row);
			if (rowCount % 2 == 0) {
				colorBlack = !colorBlack;
			}
		}

		if (mode == MODE.ISOMETRIC) {
			setRotateAroundCenter(45f);
			postScale(1f, 0.5f);
		}

		mMode = mode;
		mRowCount = rowCount;
		mColumnCount = columnCount;
	}

	@Override
	public void onDraw(final Canvas canvas, final Paint paint) {
		mTileCountDrawn = 0;

		int firstVisibleRow = 0;
		int lastVisibleRow = 0;

		int firstVisibleColumn = 0;
		int lastVisibleColumn = 0;

		// WIP calculate
		// switch (mMode) {
		// case NORMAL:
		firstVisibleRow = limitToRowCount(((int) -mRootElement.getTranslateY() / mTileSize));
		lastVisibleRow = limitToRowCount((mRootElement.mSurfaceHeight
				- (int) mRootElement.getTranslateY() + mTileSize)
				/ mTileSize);

		firstVisibleColumn = limitToColumnCount(((int) -mRootElement
				.getTranslateX() / mTileSize));
		lastVisibleColumn = limitToColumnCount((mRootElement.mSurfaceWidth
				- (int) mRootElement.getTranslateX() + mTileSize)
				/ mTileSize);
		// break;
		// case ISOMETRIC:
		// firstVisibleRow = 0;
		// lastVisibleRow = mRowCount;
		//
		// firstVisibleColumn = 0;
		// lastVisibleColumn = mColumnCount;
		// break;
		// }

		int drawOffsetY = firstVisibleRow * mTileSize;
		for (int i = firstVisibleRow; i < lastVisibleRow; i++) {

			int drawOffsetX = firstVisibleColumn * mTileSize;
			for (int j = firstVisibleColumn; j < lastVisibleColumn; j++) {
				mBackgroundTiles.get(i).get(j)
						.draw(canvas, drawOffsetX, drawOffsetY, paint);

				mTileCountDrawn++;
				drawOffsetX += mTileSize;
			}
			drawOffsetY += mTileSize;
		}
	}

	private int limitToRowCount(final int value) {
		return Math.max(0, Math.min(value, mRowCount));
	}

	private int limitToColumnCount(final int value) {
		return Math.max(0, Math.min(value, mColumnCount));
	}

	private int mTileCountDrawn = 0;

	// WIP enable in debug mode
	public String getDebugOutput() {
		return "drawn tiles: " + mTileCountDrawn;
	}

	@Override
	public void onUpdate(final long elapsedMillis) {

	}

	@Override
	public void doUpdate() {

	}
}
package de.fieben.fengine.surface.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.SparseArray;
import de.fieben.fengine.surface.FeSurfaceElement;

public class FeSurfaceMap extends FeSurfaceElement {

	public static enum MODE {
		NORMAL, ISOMETRIC
	};

	private final MODE mMode;

	private final FeRootElementImpl mRootElement;

	private final SparseArray<SparseArray<? extends FeSurfaceTile>> mBackgroundTiles;
	private int mTileWidth = 0;
	private int mTileHeight = 0;

	private int mRowCount = 0;
	private int mColumnCount = 0;

	public FeSurfaceMap(final FeRootElementImpl rootElement, final MODE mode,
			final SparseArray<SparseArray<? extends FeSurfaceTile>> tiles) {
		mRootElement = rootElement;

		mBackgroundTiles = tiles;

		// TODO complete isometric mode
		if (mode == MODE.ISOMETRIC) {
			setRotateAroundCenter(45f);
			postScale(1f, 0.5f);
		}

		mMode = mode;
		mRowCount = tiles.size();
		mColumnCount = tiles.get(0).size();
		mTileWidth = tiles.get(0).get(0).mWidth;
		mTileHeight = tiles.get(0).get(0).mHeight;
	}

	@Override
	public void onDraw(final Canvas canvas, final Paint paint) {
		mTileCountDrawn = 0;

		int firstVisibleRow = 0;
		int lastVisibleRow = 0;

		int firstVisibleColumn = 0;
		int lastVisibleColumn = 0;

		// TODO calculate visible tiles in isometric mode
		// switch (mMode) {
		// case NORMAL:
		firstVisibleRow = limitToRowCount(((int) -mRootElement.getTranslateY() / mTileHeight));
		lastVisibleRow = limitToRowCount((mRootElement.mSurfaceHeight
				- (int) mRootElement.getTranslateY() + mTileHeight)
				/ mTileHeight);

		firstVisibleColumn = limitToColumnCount(((int) -mRootElement
				.getTranslateX() / mTileWidth));
		lastVisibleColumn = limitToColumnCount((mRootElement.mSurfaceWidth
				- (int) mRootElement.getTranslateX() + mTileWidth)
				/ mTileWidth);
		// break;
		// case ISOMETRIC:
		// firstVisibleRow = 0;
		// lastVisibleRow = mRowCount;
		//
		// firstVisibleColumn = 0;
		// lastVisibleColumn = mColumnCount;
		// break;
		// }

		int drawOffsetY = firstVisibleRow * mTileHeight;
		for (int i = firstVisibleRow; i < lastVisibleRow; i++) {

			int drawOffsetX = firstVisibleColumn * mTileWidth;
			for (int j = firstVisibleColumn; j < lastVisibleColumn; j++) {
				mBackgroundTiles.get(i).get(j)
						.draw(canvas, drawOffsetX, drawOffsetY, paint);

				mTileCountDrawn++;
				drawOffsetX += mTileWidth;
			}
			drawOffsetY += mTileHeight;
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
package de.fieben.fengine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.SparseArray;
import de.fieben.fengine.FeSurface.MapMode;

final class FeSurfaceMap extends FeSurfaceElement {

	private final MapMode mMode;

	private final SparseArray<SparseArray<? extends FeSurfaceTile>> mBackgroundTiles;
	private int mTileWidth = 0;
	private int mTileHeight = 0;

	private int mRowCount = 0;
	private int mColumnCount = 0;

	public FeSurfaceMap(final MapMode mode,
			final SparseArray<SparseArray<? extends FeSurfaceTile>> tiles) {
		super(tiles.get(0).size() * tiles.get(0).get(0).mWidth, tiles.size()
				* tiles.get(0).get(0).mHeight);
		mBackgroundTiles = tiles;

		// TODO complete isometric mode -> recalculation of child before drawing
		if (mode == MapMode.ISOMETRIC) {
			setRotateAroundCenter(45f);
			addScale(1f, 0.5f);
		}

		mMode = mode;

		final SparseArray<? extends FeSurfaceTile> firstColumn = tiles.get(0);
		final FeSurfaceTile firstTile = firstColumn.get(0);

		mRowCount = tiles.size();
		mColumnCount = firstColumn.size();
		mTileWidth = firstTile.mWidth;
		mTileHeight = firstTile.mHeight;
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
		firstVisibleRow = limitTo((int) -FeSurface.OFFSET_Y / mTileHeight,
				mRowCount);
		lastVisibleRow = limitTo(
				(FeSurface.HEIGHT - (int) FeSurface.OFFSET_Y + mTileHeight)
						/ mTileHeight, mRowCount);

		firstVisibleColumn = limitTo((int) -FeSurface.OFFSET_X / mTileWidth,
				mColumnCount);
		lastVisibleColumn = limitTo(
				(FeSurface.WIDTH - (int) FeSurface.OFFSET_X + mTileWidth)
						/ mTileWidth, mColumnCount);
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

	private int limitTo(final int value, final int maxValue) {
		return Math.max(0, Math.min(value, maxValue));
	}

	// DEBUG
	private int mTileCountDrawn = 0;

	protected String getDebugOutput() {
		return "drawn tiles: " + mTileCountDrawn;
	}

	@Override
	public void onUpdate(final long elapsedMillis) {
	}

	@Override
	public void doUpdate() {
	}
}
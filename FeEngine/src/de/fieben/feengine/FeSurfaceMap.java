package de.fieben.feengine;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.SparseArray;

class FeSurfaceMap extends FeSurfaceElement {

	private final SparseArray<SparseArray<? extends FeSurfaceTile>> mBackgroundTiles;
	private final int mTileWidth;
	private final int mTileHeight;

	private final int mRowCount;
	private final int mColumnCount;

	public FeSurfaceMap(
			final SparseArray<SparseArray<? extends FeSurfaceTile>> tiles) {
		super(tiles.get(0).size() * tiles.get(0).get(0).mBitmap.getWidth(),
				tiles.size() * tiles.get(0).get(0).mBitmap.getHeight());

		mBackgroundTiles = tiles;

		final SparseArray<? extends FeSurfaceTile> firstColumn = tiles.get(0);
		final FeSurfaceTile firstTile = firstColumn.get(0);

		mRowCount = tiles.size();
		mColumnCount = firstColumn.size();
		mTileWidth = firstTile.mBitmap.getWidth();
		mTileHeight = firstTile.mBitmap.getHeight();
	}

	@Override
	public void onDraw(final Canvas canvas, final Paint paint) {
		mTileCountDrawn = 0;

		int firstVisibleRow = 0;
		int lastVisibleRow = 0;

		int firstVisibleColumn = 0;
		int lastVisibleColumn = 0;

		int drawOffsetY;

		// DEBUG scale: tile dimension * surfaceScale?

		firstVisibleRow = limitTo(
				(int) -FeSurface.SURFACE.getSurfaceTranslationY() / mTileHeight,
				mRowCount);
		lastVisibleRow = limitTo(
				(FeSurface.SURFACE.getSurfaceHeight()
						- (int) FeSurface.SURFACE.getSurfaceTranslationY() + mTileHeight)
						/ mTileHeight, mRowCount);

		firstVisibleColumn = limitTo(
				(int) -FeSurface.SURFACE.getSurfaceTranslationX() / mTileWidth,
				mColumnCount);
		lastVisibleColumn = limitTo(
				(FeSurface.SURFACE.getSurfaceWidth()
						- (int) FeSurface.SURFACE.getSurfaceTranslationX() + mTileWidth)
						/ mTileWidth, mColumnCount);

		drawOffsetY = firstVisibleRow * mTileHeight;
		for (int i = firstVisibleRow; i < lastVisibleRow; i++) {
			final SparseArray<? extends FeSurfaceTile> row = mBackgroundTiles
					.get(i);
			int drawOffsetX = firstVisibleColumn * mTileWidth;
			for (int j = firstVisibleColumn; j < lastVisibleColumn; j++) {
				row.get(j).draw(canvas, drawOffsetX, drawOffsetY, paint);
				mTileCountDrawn++;
				drawOffsetX += mTileWidth;
			}
			drawOffsetY += mTileHeight;
		}
	}

	private int limitTo(final int value, final int maxValue) {
		return Math.max(0, Math.min(value, maxValue));
	}

	// DEBUG isometric
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
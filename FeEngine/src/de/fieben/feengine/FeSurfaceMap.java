package de.fieben.feengine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class FeSurfaceMap extends FeSurfaceElement {

	// private final SparseArray<SparseArray<? extends FeSurfaceTile>>
	// mBackgroundTiles;
	private FeSurfaceTile[][] mBackgroundTiles;

	private final int mRowCount;
	private final int mColumnCount;

	private final int mTileWidth;
	private final int mTileHeight;

	public FeSurfaceMap(final FeSurfaceTile[][] tiles) {
		super(-1);

		mBackgroundTiles = tiles;

		final FeSurfaceTile[] firstColumn = mBackgroundTiles[0];
		mRowCount = mBackgroundTiles.length;
		mColumnCount = firstColumn.length;

		final FeSurfaceTile firstTile = firstColumn[0];
		mTileWidth = firstTile.getBitmap().getWidth();
		mTileHeight = firstTile.getBitmap().getHeight();
	}

	@Override
	public void onDraw(final Canvas canvas, final Paint paint) {
		// WIP scale: tile dimension * surfaceScale?
		final int firstVisibleRow = limitTo(
				(int) -(FeSurface.SURFACE.getSurfaceTranslationY() * FeSurface.SURFACE.getSurfaceScaleX())
						/ mTileHeight, mRowCount);
		final int lastVisibleRow = limitTo(
				(FeSurface.SURFACE.getSurfaceHeight()
						- (int) (FeSurface.SURFACE.getSurfaceTranslationY() * FeSurface.SURFACE.getSurfaceScaleX()) + mTileHeight)
						/ mTileHeight, mRowCount);

		final int firstVisibleColumn = limitTo(
				(int) (-FeSurface.SURFACE.getSurfaceTranslationX() * FeSurface.SURFACE.getSurfaceScaleX())
						/ mTileWidth, mColumnCount);
		final int lastVisibleColumn = limitTo(
				(FeSurface.SURFACE.getSurfaceWidth()
						- (int) (FeSurface.SURFACE.getSurfaceTranslationX() * FeSurface.SURFACE.getSurfaceScaleX()) + mTileWidth)
						/ mTileWidth, mColumnCount);

		int tilesDrawn = 0;
		int drawOffsetY = firstVisibleRow * mTileHeight;
		for (int i = firstVisibleRow; i < lastVisibleRow; i++) {
			final FeSurfaceTile[] row = mBackgroundTiles[i];
			int drawOffsetX = firstVisibleColumn * mTileWidth;
			for (int j = firstVisibleColumn; j < lastVisibleColumn; j++) {
				row[j].draw(canvas, drawOffsetX, drawOffsetY, paint);
				drawOffsetX += mTileWidth;
				tilesDrawn++;
			}
			drawOffsetY += mTileHeight;
		}

		paint.setColor(Color.YELLOW);
		canvas.drawText(String.valueOf(tilesDrawn),
				(-FeSurface.SURFACE.getSurfaceTranslationX() + 35)
						/ FeSurface.SURFACE.getSurfaceScaleX(),
				(-FeSurface.SURFACE.getSurfaceTranslationY() + 100)
						/ FeSurface.SURFACE.getSurfaceScaleY(), paint);
	}

	private int limitTo(final int value, final int maxValue) {
		return Math.max(0, Math.min(value, maxValue));
	}

	@Override
	public void onUpdate(final long elapsedMillis) {
	}

	@Override
	public void doUpdate() {
	}
}
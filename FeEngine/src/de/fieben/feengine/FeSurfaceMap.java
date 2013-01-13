package de.fieben.feengine;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Holds and draws a tiled map on the {@link FeSurface} set with
 * {@link FeSurface#addMap(FeSurfaceTile[][])}.
 * 
 * @author Felix Moser - felix.ernesto.moser@googlemail.com
 * 
 */
class FeSurfaceMap extends FeSurfaceElement {

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
		final int scaledTileHeight = (int) (mTileHeight * FeSurface.SURFACE
				.getSurfaceScaleY());
		final int scaledTileWidth = (int) (mTileWidth * FeSurface.SURFACE
				.getSurfaceScaleX());
		final float surfaceTranslationX = FeSurface.SURFACE
				.getSurfaceTranslationX();
		final float surfaceTranslationY = FeSurface.SURFACE
				.getSurfaceTranslationY();

		final int firstVisibleRow = limitTo(
				(-surfaceTranslationY / scaledTileHeight) - 1, mRowCount);
		final int lastVisibleRow = limitTo(
				((FeSurface.SURFACE.getSurfaceHeight() - surfaceTranslationY + scaledTileHeight) / scaledTileHeight) + 1,
				mRowCount);
		final int firstVisibleColumn = limitTo(
				(-surfaceTranslationX / scaledTileWidth) - 1, mColumnCount);
		final int lastVisibleColumn = limitTo(
				((FeSurface.SURFACE.getSurfaceWidth() - surfaceTranslationX + scaledTileWidth) / scaledTileWidth) + 1,
				mColumnCount);

		int drawOffsetY = firstVisibleRow * mTileHeight;
		for (int i = firstVisibleRow; i < lastVisibleRow; i++) {
			final FeSurfaceTile[] row = mBackgroundTiles[i];
			int drawOffsetX = firstVisibleColumn * mTileWidth;
			for (int j = firstVisibleColumn; j < lastVisibleColumn; j++) {
				row[j].draw(canvas, drawOffsetX, drawOffsetY, paint);
				drawOffsetX += mTileWidth;
			}
			drawOffsetY += mTileHeight;
		}
	}

	private int limitTo(final float f, final int maxValue) {
		return Math.max(0, Math.min((int) f, maxValue));
	}

	@Override
	public void onUpdate(final long elapsedMillis) {
	}

	@Override
	public void doUpdate() {
	}
}
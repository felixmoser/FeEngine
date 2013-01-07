package de.fieben.feengine.showroom.scenes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.SparseArray;
import de.fieben.feengine.FeSurface;
import de.fieben.feengine.FeSurfaceTile;
import de.fieben.feengine.showroom.R;
import de.fieben.feengine.showroom.tiles.ForestTile;

public class IsometricForestScene extends FeSurface {

	public IsometricForestScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		addMap(FeSurface.MapMode.ISOMETRIC, buildTiledBackground());
	}

	private SparseArray<SparseArray<? extends FeSurfaceTile>> buildTiledBackground() {
		final SparseArray<SparseArray<? extends FeSurfaceTile>> tiles = new SparseArray<SparseArray<? extends FeSurfaceTile>>(
				100);

		final Bitmap tileBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.tile_large00);
		for (int i = 0; i < 100; i++) {
			final SparseArray<ForestTile> row = new SparseArray<ForestTile>(100);
			for (int j = 0; j < 100; j++) {
				row.append(j, new ForestTile(tileBitmap, null, null));
			}
			tiles.append(i, row);
		}
		return tiles;
	}
}
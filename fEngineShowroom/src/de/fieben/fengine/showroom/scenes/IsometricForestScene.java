package de.fieben.fengine.showroom.scenes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.SparseArray;
import de.fieben.fengine.showroom.R;
import de.fieben.fengine.surface.FeSurface;
import de.fieben.fengine.surface.impl.FeSurfaceMap;
import de.fieben.fengine.surface.impl.FeSurfaceTile;

public class IsometricForestScene extends FeSurface {

	public IsometricForestScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		addMap(FeSurfaceMap.MapMode.ISOMETRIC, buildTiledBackground());
	}

	private SparseArray<SparseArray<? extends FeSurfaceTile>> buildTiledBackground() {
		final SparseArray<SparseArray<? extends FeSurfaceTile>> tiles = new SparseArray<SparseArray<? extends FeSurfaceTile>>(
				100);

		final Bitmap tileBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.tile_large00);
		int id = 0;
		for (int i = 0; i < 100; i++) {

			final SparseArray<CustomSurfaceTile> row = new SparseArray<CustomSurfaceTile>(
					100);
			for (int j = 0; j < 100; j++) {
				row.append(j, new CustomSurfaceTile(id++, tileBitmap));
			}
			tiles.append(i, row);
		}
		return tiles;
	}

	public class CustomSurfaceTile extends FeSurfaceTile {

		public CustomSurfaceTile(final int id, final Bitmap bitmap) {
			super(id, bitmap);
		}

		@Override
		public void draw(final Canvas canvas, final int x, final int y,
				final Paint paint) {
			super.draw(canvas, x, y, paint);

			paint.setColor(Color.BLACK + (Color.WHITE ^ mBackgroundColor));
			paint.setTextSize(20);
			canvas.drawText(String.valueOf(mId + 1), x + 10, y + 25, paint);
		}
	}
}
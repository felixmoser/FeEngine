package de.fieben.fengine.showroom.scenes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.SparseArray;
import de.fieben.fengine.showroom.R;
import de.fieben.fengine.showroom.tiles.ForestTile;
import de.fieben.fengine.surface.FeSurface;
import de.fieben.fengine.surface.impl.FeSurfaceMap;
import de.fieben.fengine.surface.impl.FeSurfaceTile;

public class TiledForestScene extends FeSurface {

	// TODO impl preparation thread and "loading" support

	public TiledForestScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		addMap(FeSurfaceMap.MapMode.NORMAL, buildTiledBackground());
	}

	private SparseArray<SparseArray<? extends FeSurfaceTile>> buildTiledBackground() {
		final SparseArray<SparseArray<? extends FeSurfaceTile>> tiles = new SparseArray<SparseArray<? extends FeSurfaceTile>>(
				100);

		final Bitmap forestBackground = BitmapFactory.decodeResource(
				getResources(), R.drawable.tile_large00);
		final Bitmap treeBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.tree00);
		ArrayList<Pair<Integer, Integer>> trees;
		final Random random = new Random();
		final Comparator<Pair<Integer, Integer>> yLastComparator = new Comparator<Pair<Integer, Integer>>() {
			@Override
			public int compare(final Pair<Integer, Integer> lhs,
					final Pair<Integer, Integer> rhs) {
				return lhs.second - rhs.second;
			}
		};

		int id = 0;
		boolean colorBlack = true;
		for (int i = 0; i < 100; i++) {
			final SparseArray<ForestTile> row = new SparseArray<ForestTile>(100);
			for (int j = 0; j < 100; j++) {
				trees = new ArrayList<Pair<Integer, Integer>>();
				for (int k = random.nextInt(20); k > 0; k--) {
					final int xValue;
					final int yValue;
					if (i == 0) {
						yValue = random.nextInt(forestBackground.getHeight()
								- treeBitmap.getHeight());
					} else {
						yValue = random.nextInt(forestBackground.getHeight())
								- treeBitmap.getHeight();
					}
					if (j == 0) {
						xValue = random.nextInt(forestBackground.getWidth()
								- treeBitmap.getWidth());
					} else {
						xValue = random.nextInt(forestBackground.getWidth())
								- treeBitmap.getWidth();
					}
					trees.add(Pair.create(xValue, yValue));
				}
				// TODO integrate y-axis ordering into FeSurface?
				Collections.sort(trees, yLastComparator);
				row.append(j, new ForestTile(id++, forestBackground,
						treeBitmap, trees));
				colorBlack = !colorBlack;
			}
			tiles.append(i, row);
			colorBlack = !colorBlack;
		}
		return tiles;
	}
}
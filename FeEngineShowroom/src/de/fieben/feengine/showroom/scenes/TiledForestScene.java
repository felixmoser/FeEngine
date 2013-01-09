package de.fieben.feengine.showroom.scenes;

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
import de.fieben.feengine.FeSurface;
import de.fieben.feengine.FeSurfaceTile;
import de.fieben.feengine.showroom.R;
import de.fieben.feengine.showroom.tiles.ForestTile;

public class TiledForestScene extends FeSurface {

	// TODO impl preparation thread and "loading" support

	public TiledForestScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		addMap(FeSurface.MapMode.NORMAL, buildTiledBackground());
	}

	private SparseArray<SparseArray<? extends FeSurfaceTile>> buildTiledBackground() {
		final SparseArray<SparseArray<? extends FeSurfaceTile>> tiles = new SparseArray<SparseArray<? extends FeSurfaceTile>>(
				100);

		final Bitmap forestBackground = BitmapFactory.decodeResource(
				getResources(), R.drawable.forest_tile);
		final Bitmap treeBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.tree);
		ArrayList<Pair<Integer, Integer>> trees;
		final Random random = new Random();
		final Comparator<Pair<Integer, Integer>> yLastComparator = new Comparator<Pair<Integer, Integer>>() {
			@Override
			public int compare(final Pair<Integer, Integer> lhs,
					final Pair<Integer, Integer> rhs) {
				return lhs.second - rhs.second;
			}
		};

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
				// TAI integrate y-axis ordering into FeSurface?
				Collections.sort(trees, yLastComparator);
				row.append(j, new ForestTile(forestBackground, treeBitmap,
						trees));
			}
			tiles.append(i, row);
		}
		return tiles;
	}
}
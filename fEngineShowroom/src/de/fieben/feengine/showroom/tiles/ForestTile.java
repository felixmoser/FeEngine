package de.fieben.feengine.showroom.tiles;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Pair;
import de.fieben.feengine.FeSurfaceTile;

public class ForestTile extends FeSurfaceTile {
	private Bitmap mTreeBitmap;
	private final ArrayList<Pair<Integer, Integer>> mTrees;

	public ForestTile(final int id, final Bitmap background, final Bitmap tree,
			final ArrayList<Pair<Integer, Integer>> trees) {
		super(id, background);
		mTreeBitmap = tree;
		mTrees = trees;
	}

	@Override
	public void draw(final Canvas canvas, final int x, final int y,
			final Paint paint) {
		super.draw(canvas, x, y, paint);
		for (final Pair<Integer, Integer> treeCords : mTrees) {
			canvas.drawBitmap(mTreeBitmap, x + treeCords.first, y
					+ treeCords.second, paint);
		}
	}
}
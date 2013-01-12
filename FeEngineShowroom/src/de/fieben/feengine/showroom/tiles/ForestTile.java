package de.fieben.feengine.showroom.tiles;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Pair;
import de.fieben.feengine.FeBitmapPool;
import de.fieben.feengine.FeSurfaceTile;

public class ForestTile extends FeSurfaceTile {
	private int mTreeBitmapKey;
	private final ArrayList<Pair<Integer, Integer>> mTrees;

	public ForestTile(final int backgroundBitmapKey, final int treeBitmapKey,
			final ArrayList<Pair<Integer, Integer>> trees) {
		super(backgroundBitmapKey);
		mTreeBitmapKey = treeBitmapKey;
		mTrees = trees;
	}

	@Override
	public void onDraw(final Canvas canvas, final int x, final int y,
			final Paint paint) {
		for (final Pair<Integer, Integer> treeCords : mTrees) {
			canvas.drawBitmap(FeBitmapPool.getBitmap(mTreeBitmapKey), x
					+ treeCords.first, y + treeCords.second, paint);
		}
	}
}
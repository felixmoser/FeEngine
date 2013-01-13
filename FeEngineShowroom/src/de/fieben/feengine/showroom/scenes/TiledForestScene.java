package de.fieben.feengine.showroom.scenes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Pair;
import de.fieben.feengine.FeBitmapPool;
import de.fieben.feengine.FeSurface;
import de.fieben.feengine.FeSurfaceTile;
import de.fieben.feengine.showroom.R;

public class TiledForestScene extends FeSurface {
	private ProgressDialog mLoadingDialog;

	public TiledForestScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		new TileLoadingTask(context).execute();
	}

	@Override
	public void addSurfaceScale(final float scaleX, final float scaleY,
			final float pointX, final float pointY) {
		super.addSurfaceScale(scaleX, scaleY, pointX, pointY);
		final float lowerScaleLimit = 0.5f;
		if (getSurfaceScaleX() < lowerScaleLimit
				|| getSurfaceScaleY() < lowerScaleLimit) {
			setSurfaceScale(lowerScaleLimit, lowerScaleLimit, pointX, pointY);
		}
	}

	private class TileLoadingTask extends
			AsyncTask<Void, Void, FeSurfaceTile[][]> {

		public TileLoadingTask(final Context context) {
			mLoadingDialog = ProgressDialog.show(context,
					context.getString(R.string.loading_scene_title),
					context.getString(R.string.loading_scene_message_tiles),
					false, false);
		}

		@Override
		protected FeSurfaceTile[][] doInBackground(final Void... params) {
			final int size = 100;
			final FeSurfaceTile[][] tiles = new FeSurfaceTile[size][size];

			final Bitmap forestBackground = BitmapFactory.decodeResource(
					getResources(), R.drawable.forest_tile);
			final Bitmap treeBitmap = BitmapFactory.decodeResource(
					getResources(), R.drawable.tree);

			final int forestBackgroundKey = FeBitmapPool
					.addBitmap(forestBackground);
			final int treeBitmapKey = FeBitmapPool.addBitmap(treeBitmap);

			ArrayList<Pair<Integer, Integer>> trees;
			final Random random = new Random();
			final Comparator<Pair<Integer, Integer>> yLastComparator = new Comparator<Pair<Integer, Integer>>() {
				@Override
				public int compare(final Pair<Integer, Integer> lhs,
						final Pair<Integer, Integer> rhs) {
					return lhs.second - rhs.second;
				}
			};

			for (int i = 0; i < size; i++) {
				final ForestTile[] row = new ForestTile[size];
				for (int j = 0; j < size; j++) {
					trees = new ArrayList<Pair<Integer, Integer>>();
					for (int k = random.nextInt(20); k > 0; k--) {
						final int xValue;
						final int yValue;
						if (i == 0) {
							yValue = random.nextInt(forestBackground
									.getHeight() - treeBitmap.getHeight());
						} else {
							yValue = random.nextInt(forestBackground
									.getHeight()) - treeBitmap.getHeight();
						}
						if (j == 0) {
							xValue = random.nextInt(forestBackground.getWidth()
									- treeBitmap.getWidth());
						} else {
							xValue = random
									.nextInt(forestBackground.getWidth())
									- treeBitmap.getWidth();
						}
						trees.add(Pair.create(xValue, yValue));
					}
					// TAI integrate y-axis ordering into FeSurface?
					Collections.sort(trees, yLastComparator);
					row[j] = new ForestTile(forestBackgroundKey, treeBitmapKey,
							trees);
				}
				tiles[i] = row;
			}
			return tiles;
		}

		@Override
		protected void onPostExecute(final FeSurfaceTile[][] tiles) {
			try {
				addMap(tiles);
			} finally {
				if (mLoadingDialog.isShowing()) {
					mLoadingDialog.dismiss();
				}
			}
		}
	}

	private class ForestTile extends FeSurfaceTile {
		private int mTreeBitmapKey;
		private final ArrayList<Pair<Integer, Integer>> mTrees;

		public ForestTile(final int backgroundBitmapKey,
				final int treeBitmapKey,
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
}
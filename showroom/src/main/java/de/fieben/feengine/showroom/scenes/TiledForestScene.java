package de.fieben.feengine.showroom.scenes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import de.fieben.feengine.FeBitmapPool;
import de.fieben.feengine.FeSurface;
import de.fieben.feengine.FeSurfaceTile;
import de.fieben.feengine.showroom.R;

public class TiledForestScene extends FeSurface {

	private ProgressDialog mLoadingDialog;

	private String mFrameInformation;
	private long[] mLastElapsed = new long[20];
	private int mElapsedIndex = 0;

	public TiledForestScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		new TileLoadingTask(context).execute();
	}

	@Override
	public void addSurfaceScale(final float scaleX, final float scaleY, final float pointX, final float pointY) {
		super.addSurfaceScale(scaleX, scaleY, pointX, pointY);
		final float lowerScaleLimit = 0.5f;
		if (getSurfaceScaleX() < lowerScaleLimit || getSurfaceScaleY() < lowerScaleLimit) {
			setSurfaceScale(lowerScaleLimit, lowerScaleLimit, pointX, pointY);
		}
	}

	@Override
	protected void onDraw(final Canvas canvas) {
		super.onDraw(canvas);

		mPaint.setColor(Color.MAGENTA);
		mPaint.setTextSize(34);
		canvas.drawText(mFrameInformation, 35, 50, mPaint);
	}

	@Override
	protected void onUpdate(final long elapsedMillis) {
		super.onUpdate(elapsedMillis);

		calculateFPS(elapsedMillis);
	}

	private void calculateFPS(final long elapsedMillis) {
		if (mElapsedIndex >= 20) {
			mElapsedIndex = 0;
		}
		mLastElapsed[mElapsedIndex++] = elapsedMillis;
		long averageFPS = 0;
		for (int i = 0; i < 20; i++) {
			averageFPS += mLastElapsed[i];
		}
		mFrameInformation = "FPS: " + String.valueOf(20000 / averageFPS);
	}

	private class TileLoadingTask extends
			AsyncTask<Void, Void, FeSurfaceTile[][]> {

		private Resources mResources;

		public TileLoadingTask(final Context context) {
			mLoadingDialog = ProgressDialog.show(context, context.getString(R.string.loading_scene_title), context.getString(R.string.loading_scene_message_tiles), false, false);
			mResources = getResources();
		}

		@Override
		protected FeSurfaceTile[][] doInBackground(final Void... params) {
			final int size = 100;
			final FeSurfaceTile[][] tiles = new FeSurfaceTile[size][size];

			final Bitmap forestBackground = BitmapFactory.decodeResource(mResources, R.drawable.forest_tile);
			final Bitmap treeBitmap = BitmapFactory.decodeResource(mResources, R.drawable.tree);

			final int forestBackgroundKey = FeBitmapPool.addBitmap(forestBackground);
			final int treeBitmapKey = FeBitmapPool.addBitmap(treeBitmap);

			ArrayList<Pair<Integer, Integer>> trees;
			final Random random = new Random();
			final Comparator<Pair<Integer, Integer>> yLastComparator = new Comparator<Pair<Integer, Integer>>() {
				@Override
				public int compare(final Pair<Integer, Integer> lhs, final Pair<Integer, Integer> rhs) {
					return lhs.second - rhs.second;
				}
			};

			for (int i = 0; i < size; i++) {
				final ForestTile[] row = new ForestTile[size];
				for (int j = 0; j < size; j++) {
					trees = new ArrayList<>();
					for (int k = random.nextInt(20); k > 0; k--) {
						final int xValue;
						final int yValue;
						if (i == 0) {
							yValue = random.nextInt(forestBackground.getHeight() - treeBitmap.getHeight());
						} else {
							yValue = random.nextInt(forestBackground.getHeight()) - treeBitmap.getHeight();
						}
						if (j == 0) {
							xValue = random.nextInt(forestBackground.getWidth() - treeBitmap.getWidth());
						} else {
							xValue = random.nextInt(forestBackground.getWidth()) - treeBitmap.getWidth();
						}
						trees.add(Pair.create(xValue, yValue));
					}
					// WIP integrate y-axis ordering into FeSurface?
					Collections.sort(trees, yLastComparator);
					row[j] = new ForestTile(forestBackgroundKey, treeBitmapKey, trees);
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

		public ForestTile(final int backgroundBitmapKey, final int treeBitmapKey, final ArrayList<Pair<Integer, Integer>> trees) {
			super(backgroundBitmapKey);
			mTreeBitmapKey = treeBitmapKey;
			mTrees = trees;
		}

		@Override
		public void onDraw(final Canvas canvas, final int x, final int y, final Paint paint) {
			for (final Pair<Integer, Integer> treeCords : mTrees) {
				canvas.drawBitmap(FeBitmapPool.getBitmap(mTreeBitmapKey), x + treeCords.first, y + treeCords.second, paint);
			}
		}
	}
}
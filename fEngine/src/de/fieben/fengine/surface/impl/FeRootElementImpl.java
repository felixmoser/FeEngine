package de.fieben.fengine.surface.impl;

import java.util.HashMap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import de.fieben.fengine.surface.FeSurfaceElement;
import de.fieben.fengine.surface.FeSurfaceTile;

public class FeRootElementImpl extends FeSurfaceElement {
	private int mVoidColor;

	private final int mRowCount;
	private final int mColumnCount;
	private final HashMap<Integer, HashMap<Integer, FeSurfaceTile>> mBackground;

	// WIP useful? check memory and performance behavior at declaration and
	// initialization.
	private HashMap<Integer, FeSurfaceTile> mNextRow;
	private FeSurfaceTile mNextTile = null;
	private int mBackgroundDrawOffsetX = 0;
	private int mBackgroundDrawOffsetY = 0;

	// TODO better construktor param usage/handling
	public FeRootElementImpl(final int rowCount, final int columnCount) {
		mRowCount = rowCount;
		mColumnCount = columnCount;
		mBackground = new HashMap<Integer, HashMap<Integer, FeSurfaceTile>>();

		if (mRowCount > 0 && mColumnCount > 0) {
			int id = 0;
			boolean colorBlack = true;
			for (int i = 0; i < mRowCount; i++) {
				mNextRow = new HashMap<Integer, FeSurfaceTile>();
				for (int j = 0; j < mColumnCount; j++) {
					// TODO set tiles with params or method (and add a mTileMode
					// inside)
					mNextRow.put(j, new FeSurfaceTile(id++,
							colorBlack ? Color.BLACK : Color.WHITE, 100, 100));
					colorBlack = !colorBlack;
				}
				mBackground.put(i, mNextRow);
				if (mRowCount % 2 == 0) {
					colorBlack = !colorBlack;
				}
			}
		}
	}

	public void setVoidColor(final int VoidColor) {
		mVoidColor = VoidColor;
	}

	@Override
	public void onDraw(final Canvas canvas, final Paint paint) {
		canvas.drawColor(mVoidColor);

		if (mRowCount > 0 && mColumnCount > 0) {
			mBackgroundDrawOffsetY = 0;
			for (int i = 0; i < mRowCount; i++) {
				mBackgroundDrawOffsetX = 0;
				mNextRow = mBackground.get(i);
				for (int j = 0; j < mColumnCount; j++) {
					mNextTile = mNextRow.get(j);
					mNextTile.draw(canvas, mBackgroundDrawOffsetX,
							mBackgroundDrawOffsetY, paint);
					// TODO spacing as param or alpha offset using png?
					mBackgroundDrawOffsetX += mNextTile.mWidth + 5;
				}
				mBackgroundDrawOffsetY += mNextTile.mHeight + 5;
			}
		}
	}

	@Override
	public void onUpdate(final long elapsedMillis) {
	}

	@Override
	public void doUpdate() {
	}
}

package de.fieben.fengine.surface.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.SparseArray;
import de.fieben.fengine.surface.FeSurfaceElement;

public class FeRootElementImpl extends FeSurfaceElement {
	private int mVoidColor;
	private FeSurfaceMap mMap = null;
	protected int mSurfaceWidth = 0;
	protected int mSurfaceHeight = 0;

	// TODO better construktor param usage/handling
	public FeRootElementImpl(final int voidColor) {
		mVoidColor = voidColor;
	}

	@Override
	public void onDraw(final Canvas canvas, final Paint paint) {
		canvas.drawColor(mVoidColor);
	}

	public void updateSurfaceSize(final int width, final int height) {
		mSurfaceWidth = width;
		mSurfaceHeight = height;
	}

	@Override
	public void onUpdate(final long elapsedMillis) {
	}

	@Override
	public void doUpdate() {
	}

	public void addMap(final FeSurfaceMap.MODE mode,
			final SparseArray<SparseArray<? extends FeSurfaceTile>> tiles) {
		mMap = new FeSurfaceMap(this, mode, tiles);
		addChild(BACKGROUND_LAYER, mMap);
	}

	public String getDebugOutput() {
		if (mMap != null) {
			return mMap.getDebugOutput();
		}
		return "";
	}
}
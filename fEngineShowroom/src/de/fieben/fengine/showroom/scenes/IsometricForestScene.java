package de.fieben.fengine.showroom.scenes;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import de.fieben.fengine.showroom.R;
import de.fieben.fengine.showroom.elements.GridElement;
import de.fieben.fengine.surface.FeSurface;
import de.fieben.fengine.surface.impl.FeSurfaceMap;

public class IsometricForestScene extends FeSurface {

	public IsometricForestScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		buildTiledBackground(FeSurfaceMap.MODE.ISOMETRIC, 100, 100,
				BitmapFactory.decodeResource(getResources(),
						R.drawable.tile_large00));
	}

	@Override
	public void surfaceChanged(final SurfaceHolder holder, final int format,
			final int width, final int height) {

		final GridElement mBackgroundGrid;
		mBackgroundGrid = new GridElement(177);
		mBackgroundGrid.setColor(Color.YELLOW);
		addElement(mBackgroundGrid);
		mBackgroundGrid.calculateGrid(width, height);

		super.surfaceChanged(holder, format, width, height);
	}
}
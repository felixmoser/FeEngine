package de.fieben.fengine.showroom.scenes;

import android.content.Context;
import android.util.AttributeSet;
import de.fieben.fengine.surface.FeSurface;
import de.fieben.fengine.surface.impl.FeSurfaceMap;

public class TiledForestScene extends FeSurface {

	public TiledForestScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		buildTiledBackground(FeSurfaceMap.MODE.NORMAL, 100, 100, null);
	}
}
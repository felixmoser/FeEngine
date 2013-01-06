package de.fieben.fengine.showroom.scenes;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import de.fieben.fengine.showroom.elements.CircleElement;
import de.fieben.fengine.showroom.elements.GridElement;
import de.fieben.fengine.surface.FeSurface;

public class UpdateTestScene extends FeSurface {

	private final CircleElement mInnerCircle;
	private final CircleElement mOrbitElipse;
	private GridElement mBackgroundGrid;

	public UpdateTestScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		mBackgroundGrid = new GridElement(100);
		mBackgroundGrid.setColor(Color.YELLOW);
		// TODO impl addBackgroundElement() to FeSurface
		addElement(mBackgroundGrid);

		mInnerCircle = new CircleElement(200);
		mInnerCircle.setTranslation(700, 600);
		mInnerCircle.setColor(Color.BLUE);

		mOrbitElipse = new CircleElement(50);
		mOrbitElipse.setTranslation(0, -400);
		mOrbitElipse.setScaleFromCenter(0.5f, 3f);
		mOrbitElipse.setUpdateTestRotation(true);

		mInnerCircle.addChild(mOrbitElipse);

		addElement(mInnerCircle);
	}

	@Override
	public void surfaceChanged(final SurfaceHolder holder, final int format,
			final int width, final int height) {
		mBackgroundGrid.calculateGrid(width, height);
		super.surfaceChanged(holder, format, width, height);
	}
}
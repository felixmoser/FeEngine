package de.fieben.feengine.showroom.scenes;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import de.fieben.feengine.FeSurface;
import de.fieben.feengine.showroom.elements.CircleElement;
import de.fieben.feengine.showroom.elements.GridElement;

public class UpdateTestScene extends FeSurface {

	private final CircleElement mInnerCircle;
	private final CircleElement mOrbitElipse;
	private GridElement mBackgroundGrid;

	public UpdateTestScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		mInnerCircle = new CircleElement(200);
		mInnerCircle.setTranslate(700, 600);
		mInnerCircle.setColor(Color.BLUE);

		mOrbitElipse = new CircleElement(50);
		mOrbitElipse.setTranslate(0, -400);
		mOrbitElipse.setScaleFromCenter(0.5f, 3f);
		mOrbitElipse.setUpdateTestRotation(true);

		mInnerCircle.addChild(mOrbitElipse);

		addElement(mInnerCircle);

		mBackgroundGrid = new GridElement(100);
		mBackgroundGrid.setColor(Color.YELLOW);
		addBackgroundElement(mBackgroundGrid);
	}

	@Override
	public void surfaceChanged(final SurfaceHolder holder, final int format,
			final int width, final int height) {
		mBackgroundGrid.calculateGrid(width, height);
		super.surfaceChanged(holder, format, width, height);
	}
}
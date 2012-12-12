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
		mInnerCircle = new CircleElement(200);
		mInnerCircle.setTranslate(700, 600);
		mInnerCircle.setColor(Color.BLUE);

		mOrbitElipse = new CircleElement(50);
		mOrbitElipse.setTranslate(0, -400);
		mOrbitElipse.setScale(0.5f, 3);
		mOrbitElipse.setWeirdRotation(true);

		mInnerCircle.addChild(mOrbitElipse);

		addElement(mInnerCircle);
	}

	@Override
	public void surfaceChanged(final SurfaceHolder holder, final int format,
			final int width, final int height) {
		mBackgroundGrid = new GridElement(100, width, height);
		mBackgroundGrid.setColor(Color.YELLOW);
		// TODO impl addBackgroundElement() to FeSurface
		addElement(mBackgroundGrid);
		super.surfaceChanged(holder, format, width, height);
	}
}
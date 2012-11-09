package de.fieben.fengine.showroom.scenes;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import de.fieben.fengine.showroom.elements.CircleElement;
import de.fieben.fengine.showroom.elements.RectangleElement;
import de.fieben.fengine.surface.FeSurface;

// TODO possible abstract?
public class AbstractScene extends FeSurface {

	private final CircleElement mRootCircle;
	private final CircleElement mOrbitElipse;
	private final RectangleElement mPointerRectangle;

	public AbstractScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		mRootCircle = new CircleElement(75);
		mRootCircle.setTranslate(300, 300);
		mRootCircle.setColor(Color.BLUE);

		mOrbitElipse = new CircleElement(20);
		mOrbitElipse.setTranslate(0, -150);
		mOrbitElipse.setScale(0.5f, 3);
		mOrbitElipse.setWeirdRotation(true);

		mPointerRectangle = new RectangleElement(5, 65, false);
		mPointerRectangle.setColor(Color.YELLOW);

		mRootCircle.addChild(mOrbitElipse);
		// mRootCircle.addChild(mPointerRectangle);
		addElement(mRootCircle);
	}
}
package de.fieben.fengine.showroom.scenes;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import de.fieben.fengine.showroom.elements.CircleElement;
import de.fieben.fengine.showroom.elements.RectangleElement;

public class SolarSystemScene extends AbstractScene {

	private final CircleElement mSun;
	private final CircleElement mEarth;
	private final RectangleElement mPointerRectangle;

	public SolarSystemScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		mSun = new CircleElement(75);
		mSun.setTranslate(200, 200);
		mSun.setColor(Color.RED);

		mEarth = new CircleElement(20);
		mEarth.setTranslate(0, -150);
		// mOrbitElipse.setScale(0.5f, 3);
		mEarth.setColor(Color.BLUE);
		// mOrbitElipse.setWeirdRotation(true);
		mEarth.setNormalRotation(true);

		mPointerRectangle = new RectangleElement(5, -65, false);
		mPointerRectangle.setColor(Color.YELLOW);

		final CircleElement moon = new CircleElement(5);
		moon.setColor(Color.MAGENTA);
		moon.setTranslate(0, 35);
		// mond.setWeirdRotation(true);
		moon.setNormalRotation(true);

		mEarth.addChild(moon);

		mSun.addChild(mEarth);

		// TODO scaling correct?
		setScale(1.5f, 1.5f);
		// mSun.addChild(new CircleElement(1));
		// mRootCircle.addChild(mPointerRectangle);

		// final CircleElement point = new CircleElement(5);
		// point.setColor(Color.BLACK);
		// addElement(point);
		addElement(mSun);

		setDrag(true);
	}
}
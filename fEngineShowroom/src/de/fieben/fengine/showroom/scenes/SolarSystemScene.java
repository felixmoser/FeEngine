package de.fieben.fengine.showroom.scenes;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import de.fieben.fengine.showroom.elements.CircleElement;
import de.fieben.fengine.showroom.elements.RectangleElement;
import de.fieben.fengine.surface.FeSurface;

public class SolarSystemScene extends FeSurface {

	// TODO add asteroids and moons
	private final CircleElement mSun;
	// TODO set realistic behaviors
	// private final CircleElement mMercury;
	// private final CircleElement mVenus;
	private final CircleElement mEarth;
	// private final CircleElement mMars;
	// private final CircleElement mJupiter;
	// private final CircleElement mSaturn;
	// private final CircleElement mUranus;
	// private final CircleElement mNeptune;
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
	}
}
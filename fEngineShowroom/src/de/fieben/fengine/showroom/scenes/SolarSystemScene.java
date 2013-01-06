package de.fieben.fengine.showroom.scenes;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import de.fieben.fengine.FeSurface;
import de.fieben.fengine.FeSurfaceElement;
import de.fieben.fengine.showroom.R;
import de.fieben.fengine.showroom.elements.CircleElement;

public class SolarSystemScene extends FeSurface {

	// WIP build solar system
	private FeSurfaceElement mAnimationSun;
	// private final CircleElement mMercury;
	// private final CircleElement mVenus;
	private final CircleElement mEarth;
	private final CircleElement mMars;

	// private final CircleElement mJupiter;
	// private final CircleElement mSaturn;
	// private final CircleElement mUranus;
	// private final CircleElement mNeptune;

	public SolarSystemScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		mAnimationSun = new FeSurfaceElement(BitmapFactory.decodeResource(
				getResources(), R.drawable.sun_rotation01)) {
			@Override
			public void onUpdate(final long elapsedMillis) {
			}

			@Override
			public void onDraw(final Canvas canvas, final Paint paint) {
			}

			@Override
			public void doUpdate() {
			}
		};
		mAnimationSun.setTranslation(450, 450);
		mAnimationSun.setAnimation(4, 100);
		addElement(mAnimationSun);

		mEarth = new CircleElement(20);
		mEarth.setTranslation(0, -350);
		mEarth.setColor(Color.BLUE);
		mEarth.setNormalTestRotation(true);
		mAnimationSun.addChild(mEarth);

		final CircleElement moon = new CircleElement(5);
		moon.setColor(Color.MAGENTA);
		moon.setTranslation(0, 35);
		moon.setNormalTestRotation(true);
		mEarth.addChild(moon);

		mMars = new CircleElement(15);
		mMars.setColor(Color.RED);
		mMars.setTranslation(0, -450);
		mMars.setFastTestRotation(true);
		mAnimationSun.addChild(mMars);
	}
}
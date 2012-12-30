package de.fieben.fengine.showroom.scenes;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import de.fieben.fengine.showroom.R;
import de.fieben.fengine.showroom.elements.CircleElement;
import de.fieben.fengine.showroom.elements.RectangleElement;
import de.fieben.fengine.surface.FeSurface;
import de.fieben.fengine.surface.FeSurfaceElement;

public class SolarSystemScene extends FeSurface {

	// TODO build solar system
	private final CircleElement mSun;
	// private final CircleElement mMercury;
	// private final CircleElement mVenus;
	private final CircleElement mEarth;
	// private final CircleElement mMars;
	// private final CircleElement mJupiter;
	// private final CircleElement mSaturn;
	// private final CircleElement mUranus;
	// private final CircleElement mNeptune;
	private final RectangleElement mPointerRectangle;

	private FeSurfaceElement mAnimationSun;

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
		mAnimationSun.setTranslate(500, 500);
		mAnimationSun.setAnimation(4, 100);
		addElement(mAnimationSun);

		mSun = new CircleElement(75);
		mSun.setTranslate(200, 200);
		mSun.setColor(Color.RED);

		mEarth = new CircleElement(20);
		mEarth.setTranslate(0, -150);
		mEarth.setColor(Color.BLUE);
		mEarth.setNormalRotation(true);

		mPointerRectangle = new RectangleElement(5, -65, false);
		mPointerRectangle.setColor(Color.YELLOW);

		final CircleElement moon = new CircleElement(5);
		moon.setColor(Color.MAGENTA);
		moon.setTranslate(0, 35);
		moon.setNormalRotation(true);

		mEarth.addChild(moon);

		mSun.addChild(mEarth);

		// TODO scaling correct?
		setScale(1.5f, 1.5f);
		addElement(mSun);
	}
}
package de.fieben.feengine.showroom.scenes;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import de.fieben.feengine.FeBitmapPool;
import de.fieben.feengine.FeSurface;
import de.fieben.feengine.FeSurfaceElement;
import de.fieben.feengine.showroom.R;
import de.fieben.feengine.showroom.elements.CircleElement;

public class SolarSystemScene extends FeSurface {

	private AnimationTestElement mAnimationSun;
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

		mAnimationSun = new AnimationTestElement(
				FeBitmapPool.addBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.sun_animation)), 4, 100);
		mAnimationSun.setTranslate(450, 450);
		addElement(mAnimationSun);

		mEarth = new CircleElement(20);
		mEarth.setTranslate(0, -350);
		mEarth.setColor(Color.BLUE);
		mEarth.setNormalTestRotation(true);
		mAnimationSun.addChild(mEarth);

		final CircleElement moon = new CircleElement(5);
		moon.setColor(Color.MAGENTA);
		moon.setTranslate(0, 35);
		moon.setNormalTestRotation(true);
		mEarth.addChild(moon);

		mMars = new CircleElement(15);
		mMars.setColor(Color.RED);
		mMars.setTranslate(0, -450);
		mMars.setFastTestRotation(true);
		mAnimationSun.addChild(mMars);
	}

	private class AnimationTestElement extends FeSurfaceElement {
		public AnimationTestElement(final int animationBitmapKey,
				final int stepCount, final int animationInterval) {
			super(animationBitmapKey, stepCount, animationInterval);
		}

		@Override
		public void onDraw(final Canvas canvas, final Paint paint) {
		}

		@Override
		public void onUpdate(final long elapsedMillis) {
		}

		@Override
		public void doUpdate() {
		}
	}
}
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

	// TODO punkte/sterne im hintergrund verteilen (nicht mitzoomend. farbe weiß
	// bis hellgrau "flackernd")

	public SolarSystemScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		// Sun
		final int bitmapKey = FeBitmapPool.addBitmap(BitmapFactory
				.decodeResource(getResources(), R.drawable.sun_animation));
		final AnimationTestElement mAnimationSun = new AnimationTestElement(
				bitmapKey, 4, 100);
		mAnimationSun.setTranslate(450, 450);
		addElement(mAnimationSun);

		// Mercury
		addSatelite(mAnimationSun, 5, 0.4f, 4, Color.GRAY);

		// Venus
		addSatelite(mAnimationSun, 9, 0.7f, 7, Color.YELLOW);

		// Earth
		final CircleElement earth = addSatelite(mAnimationSun, 10, 1f, 9,
				Color.BLUE);
		// Earth Moon
		addSatelite(earth, 2, 0.03f, 2, Color.LTGRAY);

		// Mars
		addSatelite(mAnimationSun, 7, 1.5f, 15, Color.RED);

		// Jupiter
		final CircleElement jupiter = addSatelite(mAnimationSun, 40, 2.5f, 30,
				Color.GREEN);
		addSatelite(jupiter, 3, 0.1f, 2, Color.YELLOW);
		addSatelite(jupiter, 2, 0.15f, 4, Color.WHITE);
		addSatelite(jupiter, 5, 0.25f, 7, Color.GRAY);
		addSatelite(jupiter, 4, 0.3f, 15, Color.BLUE);

		// Saturn
		final CircleElement saturnOuterRing = addSatelite(mAnimationSun, 38,
				3.5f, 50, Color.GRAY);
		final CircleElement saturnInnerGap = new CircleElement(29);
		saturnInnerGap.setColor(Color.BLACK);
		saturnOuterRing.addChild(saturnInnerGap);
		final CircleElement saturnPlanet = new CircleElement(21);
		saturnPlanet.setColor(Color.YELLOW);
		saturnOuterRing.addChild(saturnPlanet);

		// Uranus
		addSatelite(mAnimationSun, 20, 5.2f, 120, Color.MAGENTA);

		// Neptune
		addSatelite(mAnimationSun, 20, 8.5f, 150, Color.CYAN);
	}

	private CircleElement addSatelite(final FeSurfaceElement parent,
			final int sateliteRadius, final float orbitRadiusInAU,
			final int orbitDuration, final int color) {
		final CircleElement child = new CircleElement(sateliteRadius);
		child.setTranslate(orbitRadiusInAU * 500, 0);
		child.setContinuousRotationAroundParent(orbitDuration);
		child.setColor(color);
		parent.addChild(child);
		return child;
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
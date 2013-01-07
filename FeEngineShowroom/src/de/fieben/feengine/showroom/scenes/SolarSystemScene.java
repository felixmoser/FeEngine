package de.fieben.feengine.showroom.scenes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
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

		// WIP check all resources and clean them
		mAnimationSun = new AnimationTestElement(BitmapFactory.decodeResource(
				getResources(), R.drawable.sun_rotation01), 4, 100);
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

		// registerTouchable(mAnimationSun);
	}

	private class AnimationTestElement extends FeSurfaceElement {
		// private class AnimationElement extends FeSurfaceElement implements
		// FeSurfaceTouchable {

		public AnimationTestElement(final Bitmap animationBitmap,
				final int stepCount, final int animationInterval) {
			super(animationBitmap, stepCount, animationInterval);
		}

		// private boolean mDragMode = false;
		// private float mLastTouchX;
		// private float mLastTouchY;
		//
		// @Override
		// public boolean onTouch(final MotionEvent event) {
		// final Point cords = getAbsoluteSurfacePosition();
		//
		// if (!mDragMode
		// && Math.hypot(event.getX() - cords.x, event.getY()
		// - cords.y) > (mHeight + mWidth) / 4) {
		// return false;
		// }
		//
		// switch (event.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// mLastTouchX = event.getX();
		// mLastTouchY = event.getY();
		//
		// mDragMode = true;
		// break;
		// case MotionEvent.ACTION_MOVE:
		// addTranslation(event.getX() - mLastTouchX, event.getY()
		// - mLastTouchY);
		// mLastTouchX = event.getX();
		// mLastTouchY = event.getY();
		// break;
		// case MotionEvent.ACTION_UP:
		// mDragMode = false;
		// break;
		// }
		// limitTranslation();
		// return true;
		// }
		//
		// private void limitTranslation() {
		// final float translationX = getTranslationX();
		// final float translationY = getTranslationY();
		// if (translationX < 0 || translationX > WIDTH || translationY < 0
		// || translationY > HEIGHT) {
		// setTranslation(Math.max(0, Math.min(translationX, WIDTH)),
		// Math.max(0, Math.min(translationY, HEIGHT)));
		// }
		// }

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
package de.fieben.feengine.showroom.scenes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import de.fieben.feengine.FeBitmapPool;
import de.fieben.feengine.FeSurface;
import de.fieben.feengine.FeSurfaceElement;
import de.fieben.feengine.showroom.R;

public class IsoCubeScene extends FeSurface {

	private final IsoCubeElement mIsoCube;

	public IsoCubeScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		mIsoCube = new IsoCubeElement();
		mIsoCube.addElements();
	}

	@Override
	public void surfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height) {
		// WIP
		final float scale = 1000 / getResources().getDisplayMetrics().densityDpi;
		setSurfaceScale(scale, scale, width / 2, height / 2);

		// WIP
		mIsoCube.setTranslate(width / 2, height / 2);

		super.surfaceChanged(holder, format, width, height);
	}

	private class IsoCubeElement {

		private final FeSurfaceElement[] mLevelElements;

		// WIP
		public IsoCubeElement() {
			final int[] levelDrawableIds;//= getResources().getIntArray(R.array.cube);

			final TypedArray levelDrawablesTypedArray = getResources().obtainTypedArray(R.array.cube);
			levelDrawableIds = new int[levelDrawablesTypedArray.getIndexCount()];
			for (int i = 0; i < levelDrawableIds.length; i++) {
				levelDrawableIds[i] = levelDrawablesTypedArray.getResourceId(i, 0);
			}

			mLevelElements = new FeSurfaceElement[levelDrawableIds.length];
			for (int i = 0; i < levelDrawableIds.length; i++) {
				final int bitmapKey = FeBitmapPool.addBitmap(BitmapFactory.decodeResource(getResources(), levelDrawableIds[i]));
				final IsoCubeLevel levelElement = new IsoCubeLevel(bitmapKey, i);
				mLevelElements[i] = levelElement;
			}

			levelDrawablesTypedArray.recycle();
		}

		public void addElements() {
			for (FeSurfaceElement element : mLevelElements) {
				addElement(element);
			}
		}

		public void setTranslate(int x, int y) {
			for (FeSurfaceElement element : mLevelElements) {
				element.setTranslate(x, y);
			}
		}

		private class IsoCubeLevel extends FeSurfaceElement {
			public IsoCubeLevel(int bitmapKey, int level) {
				super(IsoCubeScene.this, bitmapKey);

				// WIP
//				addTranslate(0, level);
			}
		}
	}
}
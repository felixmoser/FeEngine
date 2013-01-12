package de.fieben.feengine;

import android.graphics.Bitmap;
import android.util.SparseArray;

public class FeBitmapPool {
	private static int mNextKey = 0;

	private static SparseArray<Bitmap> mBitmapPool = new SparseArray<Bitmap>();

	// WIP rename without pool
	public static int addBitmap(final Bitmap bitmap) {
		mBitmapPool.append(mNextKey, bitmap);
		return mNextKey++;
	}

	public static Bitmap getBitmap(final int key) {
		return mBitmapPool.get(key);
	}

	public static void removeBitmap(final int key) {
		mBitmapPool.remove(key);
	}

	public static void resetBitmapPool() {
		mBitmapPool = new SparseArray<Bitmap>();
	}
}

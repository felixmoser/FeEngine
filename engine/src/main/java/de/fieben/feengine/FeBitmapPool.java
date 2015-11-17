package de.fieben.feengine;

import android.graphics.Bitmap;
import android.util.SparseArray;

/**
 * Use this class to deliver {@link Bitmap}s to the components of the FeEngine.
 * 
 * @author Felix Moser - felix.ernesto.moser@googlemail.com
 * 
 */
public class FeBitmapPool {
	private static int mNextKey = 0;

	private static SparseArray<Bitmap> mBitmapPool = new SparseArray<Bitmap>();

	/**
	 * Stores {@link Bitmap} into the {@link FeBitmapPool} and returns a key.
	 * Use the key to deliver {@link Bitmap}s to {@link FeSurfaceElement}s and
	 * {@link FeSurfaceTile}s.
	 * 
	 * @param bitmap
	 *            The {@link Bitmap} to store.
	 * @return The key of the {@link Bitmap} stored.
	 */
	public static int addBitmap(final Bitmap bitmap) {
		mBitmapPool.append(mNextKey, bitmap);
		return mNextKey++;
	}

	/**
	 * @param key
	 *            The {@link Bitmap}s key returned from
	 *            {@link #addBitmap(Bitmap)}.
	 * @return The {@link Bitmap} previous stored.
	 */
	public static Bitmap getBitmap(final int key) {
		return mBitmapPool.get(key);
	}

	/**
	 * Removes a {@link Bitmap} from the {@link FeBitmapPool}.
	 * 
	 * @param key
	 *            The key of the {@link Bitmap} stored.
	 */
	public static void removeBitmap(final int key) {
		mBitmapPool.remove(key);
	}

	/**
	 * Resets the {@link FeBitmapPool}. This deletes all previous stored
	 * {@link Bitmap}s and key mappings.
	 */
	public static void resetBitmapPool() {
		mBitmapPool = new SparseArray<Bitmap>();
	}
}

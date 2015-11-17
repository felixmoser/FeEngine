package de.fieben.feengine;

import android.view.MotionEvent;

/**
 * This interface needs to be implemented if a {@link FeSurfaceElement} should get touch responsive.
 * 
 * @author Felix Moser - felix.ernesto.moser@googlemail.com
 */
public interface FeSurfaceTouchable {
	/**
	 * @param event The {@link MotionEvent} with an offset to the absolute position on the {@link FeSurface}.
	 * @return true if {@link MotionEvent} is consumed, false otherwise.
	 */
	boolean onTouch(final MotionEvent event);
}
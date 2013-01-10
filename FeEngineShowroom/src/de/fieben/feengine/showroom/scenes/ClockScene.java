package de.fieben.feengine.showroom.scenes;

import android.content.Context;
import android.util.AttributeSet;
import de.fieben.feengine.FeSurface;
import de.fieben.feengine.showroom.elements.CircleElement;

public class ClockScene extends FeSurface {
	// TODO remove clock scene or rename to performance test? (requires fps impl
	// in this scene)
	public ClockScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		for (int i = 0; i < 5000; i++) {
			// TODO how does clipping behave with bitmaps?
			final CircleElement e = new CircleElement(5);
			e.setTranslate(-100 * ((i + 1000) / 1000), 100);
			addElement(e);
		}
	}
}
package de.fieben.feengine.showroom.scenes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import de.fieben.feengine.FeSurface;
import de.fieben.feengine.showroom.elements.CircleElement;

public class ClockScene extends FeSurface {
	public ClockScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		for (int i = 0; i < 5000; i++) {
			final CircleElement e = new CircleElement(5);
			e.setTranslate(-100 * ((i + 1000) / 1000), 100);
			addElement(e);
		}
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		// if (!super.onTouchEvent(event)
		// && event.getAction() == MotionEvent.ACTION_MOVE) {
		// final CircleElement e = new CircleElement(5);
		// e.setTranslate(event.getX(), event.getY());
		// addElement(e);
		// }
		// return true;

		return super.onTouchEvent(event);
	}
}
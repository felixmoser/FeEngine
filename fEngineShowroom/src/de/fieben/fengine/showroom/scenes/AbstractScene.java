package de.fieben.fengine.showroom.scenes;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import de.fieben.fengine.showroom.elements.CircleElement;
import de.fieben.fengine.showroom.elements.RectangleElement;
import de.fieben.fengine.surface.FeSurface;

// TODO possible abstract?
public class AbstractScene extends FeSurface {

	private final CircleElement mRootCircle;
	private final CircleElement mOrbitCircle;
	private final RectangleElement mPointerRectangle;

	public AbstractScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		mRootCircle = new CircleElement(75);
		mRootCircle.setTranslate(300, 300);
		mRootCircle.setUpdateInterval(500);
		mRootCircle.setRandomMovement(false);
		mRootCircle.setColor(Color.BLUE);

		mOrbitCircle = new CircleElement(20);
		mOrbitCircle.setColor(Color.RED);
		mOrbitCircle.setRotate(90);
		mOrbitCircle.setTranslate(-150, 0);
		mOrbitCircle.setScale(3, 0.5f);

		mPointerRectangle = new RectangleElement(65, 5, false);
		mPointerRectangle.setColor(Color.YELLOW);

		mRootCircle.addChild(mOrbitCircle);
		mRootCircle.addChild(mPointerRectangle);
		addElement(mRootCircle);
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		// TODO how to handle touchs?
		return super.onTouchEvent(event);
	}
}
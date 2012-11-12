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
	private final CircleElement mOrbitElipse;
	private final RectangleElement mPointerRectangle;

	public AbstractScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		mRootCircle = new CircleElement(75);
		mRootCircle.setTranslate(200, 200);
		mRootCircle.setColor(Color.BLUE);

		mOrbitElipse = new CircleElement(20);
		mOrbitElipse.setTranslate(0, -150);
		mOrbitElipse.setScale(0.5f, 3);
		mOrbitElipse.setWeirdRotation(true);

		mPointerRectangle = new RectangleElement(5, -65, false);
		mPointerRectangle.setColor(Color.YELLOW);

		// mRootCircle.addChild(mOrbitElipse);

		// TODO scaling correct?
		setScale(2, 2);
		mRootCircle.addChild(new CircleElement(1));
		mRootCircle.addChild(mPointerRectangle);

		// final CircleElement point = new CircleElement(5);
		// point.setColor(Color.BLACK);
		// addElement(point);
		addElement(mRootCircle);
	}

	// TODO WIP zoom gesture?
	// TODO rename
	private float mLastX;
	private float mLastY;

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastX = event.getX();
			mLastY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			addTranslate(event.getX() - mLastX, event.getY() - mLastY);
			mLastX = event.getX();
			mLastY = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			addTranslate(event.getX() - mLastX, event.getY() - mLastY);
			break;
		}
		return true;
	}

}
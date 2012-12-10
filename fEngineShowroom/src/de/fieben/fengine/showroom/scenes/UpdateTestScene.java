package de.fieben.fengine.showroom.scenes;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import de.fieben.fengine.showroom.elements.CircleElement;

public class UpdateTestScene extends AbstractScene {

	private final CircleElement mInnerCircle;
	private final CircleElement mOrbitElipse;

	public UpdateTestScene(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		mInnerCircle = new CircleElement(200);
		mInnerCircle.setTranslate(700, 600);
		mInnerCircle.setColor(Color.BLUE);

		mOrbitElipse = new CircleElement(50);
		mOrbitElipse.setTranslate(0, -400);
		mOrbitElipse.setScale(0.5f, 3);
		mOrbitElipse.setWeirdRotation(true);

		mInnerCircle.addChild(mOrbitElipse);

		addElement(mInnerCircle);
	}
}
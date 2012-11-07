package de.fieben.fengine.showroom.scenes;

import java.util.ArrayList;
import java.util.List;

public class SceneProvider {
	public static List<SceneItem> SCENE_ITEMS = new ArrayList<SceneItem>();

	public static class SceneItem {
		public int mId;
		public Class<? extends AbstractScene> mClass;

		public SceneItem(final int id,
				final Class<? extends AbstractScene> sceneClass) {
			mId = id;
			mClass = sceneClass;
		}

		@Override
		public String toString() {
			return mClass.getSimpleName();
		}
	}

	static {
		int i = 1;
		addScene(new SceneItem(i++, TopDownScene.class));
	}

	private static void addScene(final SceneItem scene) {
		SCENE_ITEMS.add(scene.mId - 1, scene);
	}
}
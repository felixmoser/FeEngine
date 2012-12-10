package de.fieben.fengine.showroom.scenes;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import de.fieben.fengine.showroom.R;

public class SceneProvider {
	private static List<SceneItem> SCENE_ITEMS = new ArrayList<SceneItem>();

	static {
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_solar,
				SolarSystemScene.class));
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_clock,
				ClockScene.class));
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_sea,
				StormySeaScene.class));
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_isometric_forest,
				IsometricForestScene.class));
	}

	public static SceneItem findSceneById(final int id) {
		for (final SceneItem s : SCENE_ITEMS) {
			if (s.mId == id) {
				return s;
			}
		}
		return null;
	}

	public static int getIdByPosition(final int position) {
		return SCENE_ITEMS.get(position).mId;
	}

	public static ListAdapter getSceneAdapter(final Context context) {
		return new ArrayAdapter<SceneProvider.SceneItem>(context,
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, SCENE_ITEMS);
	}

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
}
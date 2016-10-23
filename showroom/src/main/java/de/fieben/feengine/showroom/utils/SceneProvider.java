package de.fieben.feengine.showroom.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import de.fieben.feengine.showroom.R;

public class SceneProvider {
	private static List<SceneItem> SCENE_ITEMS = new ArrayList<>();

	// WIP make "build-packages" for scenes. maybe xml files. this packages include all informations to build a scene.
	// also these scene descriptions can be used to handle loading of resources etc.

	static {
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_update, "UpdateScene"));
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_iso_cube, "IsoCubeScene"));
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_solar, "SolarSystemScene"));
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_tiled_forest, "TiledForestScene"));
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_sound, "SoundExampleScene"));
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
		return new ArrayAdapter<>(context, android.R.layout.simple_list_item_activated_1, android.R.id.text1, SCENE_ITEMS);
	}

	public static class SceneItem {
		private int mId;
		private String mName;

		public SceneItem(final int id, final String name) {
			mId = id;
			mName = name;
		}

		public int getId() {
			return mId;
		}

		public String getName() {
			return mName;
		}

		@Override
		public String toString() {
			return getName();
		}
	}
}
package de.fieben.fengine.showroom.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import de.fieben.fengine.FeSurface;
import de.fieben.fengine.showroom.R;
import de.fieben.fengine.showroom.scenes.ClockScene;
import de.fieben.fengine.showroom.scenes.IsometricForestScene;
import de.fieben.fengine.showroom.scenes.JumpNRunScene;
import de.fieben.fengine.showroom.scenes.SolarSystemScene;
import de.fieben.fengine.showroom.scenes.SoundExampleScene;
import de.fieben.fengine.showroom.scenes.StormySeaScene;
import de.fieben.fengine.showroom.scenes.TiledForestScene;
import de.fieben.fengine.showroom.scenes.UpdateTestScene;

public class SceneProvider {
	private static List<SceneItem> SCENE_ITEMS = new ArrayList<SceneItem>();

	// TAI make "build-packages" for scenes. maybe xml files. this packages
	// include all informations to build a scene. also these scene descriptions
	// can be used to handle loading of resources etc.

	static {
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_update,
				UpdateTestScene.class));
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_solar,
				SolarSystemScene.class));
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_tiled_forest,
				TiledForestScene.class));
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_isometric_forest,
				IsometricForestScene.class));
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_sound,
				SoundExampleScene.class));
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_clock,
				ClockScene.class));
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_sea,
				StormySeaScene.class));
		SCENE_ITEMS.add(new SceneItem(R.layout.view_scene_jump,
				JumpNRunScene.class));
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
		public Class<? extends FeSurface> mClass;

		public SceneItem(final int id,
				final Class<? extends FeSurface> sceneClass) {
			mId = id;
			mClass = sceneClass;
		}

		@Override
		public String toString() {
			return mClass.getSimpleName();
		}
	}

	// class Test<t> {
	// t value;
	//
	// public void put(final t fe) {
	// value = fe;
	// }
	//
	// public t get() {
	// return value;
	// }
	// }
	//
	// class A {
	// };
	//
	// class B extends A {
	// };
	//
	// void main1() {
	// final Test<? extends B> testB = new Test<B>();
	// final Test<? extends A> test = testB;
	// final A a = new A();
	// final B b = new B();
	// test.put(a);
	// b = testB.get();
	// }
	//
	// void main2() {
	// final Test<? super A> testA = new Test<A>();
	// final Test<? super B> test = testA;
	// final A a = new A();
	// final B b = new B();
	// testA.put(a);
	// b = test.get();
	// }
}
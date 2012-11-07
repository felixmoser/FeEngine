package de.fieben.fengine.showroom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.fieben.fengine.showroom.scenes.SceneProvider;

public class SceneDetailFragment extends Fragment {

	public static final String ARG_ITEM_ID = "item_id";

	private SceneProvider.SceneItem mSceneItem;

	// private AbstractScene mScene;

	public SceneDetailFragment() {
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			mSceneItem = SceneProvider.SCENE_ITEMS.get(getArguments().getInt(
					ARG_ITEM_ID) - 1);
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_scene_detail,
				container, false);
		if (mSceneItem != null) {
			// mScene =
			// mSceneItem.mClass.cast(rootView.findViewById(R.id.scene));

			// rootView.
			//
			// final View v = rootView.findViewById(R.id.scene);
			// android.util.Log.i("testing", "::onCreateView:: "
			// + v.getClass().getSimpleName());
			// mSceneItem.mClass.cast(v);
			// android.util.Log.i("testing", "::onCreateView:: "
			// + v.getClass().getSimpleName());
			// if (v instanceof TopDownScene) {
			// ((TopDownScene) v).testOutput();
			// }

			((TextView) rootView.findViewById(R.id.scene_name))
					.setText(mSceneItem.toString());
		}
		return rootView;
	}
}

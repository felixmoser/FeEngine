package de.fieben.fengine.showroom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import de.fieben.fengine.showroom.scenes.SceneProvider;

public class SceneDetailFragment extends Fragment {

	public static final String ARG_ITEM_ID = "item_id";

	private SceneProvider.SceneItem mSceneItem;

	public SceneDetailFragment() {
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			mSceneItem = SceneProvider.findSceneById(getArguments().getInt(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_scene_detail,
				container, false);
		if (mSceneItem != null) {
			final ViewStub stub = (ViewStub) rootView.findViewById(R.id.stub);
			stub.setLayoutResource(mSceneItem.mId);
			mSceneItem.mClass.cast(stub.inflate().findViewById(R.id.scene));
		}
		return rootView;
	}
}

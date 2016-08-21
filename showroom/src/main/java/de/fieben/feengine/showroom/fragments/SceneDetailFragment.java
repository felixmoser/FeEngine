package de.fieben.feengine.showroom.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import de.fieben.feengine.showroom.R;
import de.fieben.feengine.showroom.utils.SceneProvider;

public class SceneDetailFragment extends Fragment {

	public static final String ARG_ITEM_ID = "item_id";

	private SceneProvider.SceneItem mSceneItem;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(ARG_ITEM_ID)) {
			mSceneItem = SceneProvider.findSceneById(getArguments().getInt(ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_scene_detail, container, false);
		if (mSceneItem != null) {
			final ViewStub stub = (ViewStub) rootView.findViewById(R.id.stub);
			stub.setLayoutResource(mSceneItem.getId());
			stub.inflate();
		}
		return rootView;
	}
}

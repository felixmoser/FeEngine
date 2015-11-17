package de.fieben.feengine.showroom.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;

import de.fieben.feengine.showroom.R;
import de.fieben.feengine.showroom.fragments.SceneDetailFragment;
import de.fieben.feengine.showroom.fragments.SceneListFragment;

public class SceneListActivity extends Activity implements SceneListFragment.Callbacks {

	private boolean mTwoPane;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scene_list);

		if (findViewById(R.id.scene_detail_container) != null) {
			mTwoPane = true;
			((SceneListFragment) getFragmentManager().findFragmentById(R.id.scene_list)).setActivateOnItemClick(true);

			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	@Override
	public void onItemSelected(final int id) {
		if (mTwoPane) {
			final Bundle arguments = new Bundle();
			arguments.putInt(SceneDetailFragment.ARG_ITEM_ID, id);
			final SceneDetailFragment fragment = new SceneDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction().replace(R.id.scene_detail_container, fragment).commit();
		} else {
			final Intent detailIntent = new Intent(this, SceneDetailActivity.class);
			detailIntent.putExtra(SceneDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}

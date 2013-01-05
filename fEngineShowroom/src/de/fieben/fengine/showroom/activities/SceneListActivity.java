package de.fieben.fengine.showroom.activities;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import de.fieben.fengine.showroom.R;
import de.fieben.fengine.showroom.fragments.SceneDetailFragment;
import de.fieben.fengine.showroom.fragments.SceneListFragment;

public class SceneListActivity extends FragmentActivity implements
		SceneListFragment.Callbacks {

	private boolean mTwoPane;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scene_list);

		if (findViewById(R.id.scene_detail_container) != null) {
			mTwoPane = true;
			((SceneListFragment) getSupportFragmentManager().findFragmentById(
					R.id.scene_list)).setActivateOnItemClick(true);
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
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.scene_detail_container, fragment).commit();
		} else {
			final Intent detailIntent = new Intent(this,
					SceneDetailActivity.class);
			detailIntent.putExtra(SceneDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
}

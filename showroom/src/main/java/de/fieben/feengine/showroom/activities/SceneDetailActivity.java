package de.fieben.feengine.showroom.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import de.fieben.feengine.showroom.R;
import de.fieben.feengine.showroom.fragments.SceneDetailFragment;
import de.fieben.feengine.showroom.utils.SceneProvider;

public class SceneDetailActivity extends Activity {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scene_detail);

		final int sceneId = getIntent().getIntExtra(SceneDetailFragment.ARG_ITEM_ID, -1);

		final ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			final SceneProvider.SceneItem sceneItem = SceneProvider.findSceneById(sceneId);
			if (sceneItem != null) {
				actionBar.setTitle(sceneItem.toString());
			}
		}

		if (savedInstanceState == null) {
			final Bundle arguments = new Bundle();
			arguments.putInt(SceneDetailFragment.ARG_ITEM_ID, sceneId);
			final SceneDetailFragment fragment = new SceneDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction().add(R.id.scene_detail_container, fragment).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			navigateUpTo(new Intent(this, SceneListActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}

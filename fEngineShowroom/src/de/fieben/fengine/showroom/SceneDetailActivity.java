package de.fieben.fengine.showroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class SceneDetailActivity extends FragmentActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scene_detail);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			final Bundle arguments = new Bundle();
			arguments.putInt(SceneDetailFragment.ARG_ITEM_ID, getIntent()
					.getIntExtra(SceneDetailFragment.ARG_ITEM_ID, -1));
			final SceneDetailFragment fragment = new SceneDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.scene_detail_container, fragment).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			NavUtils.navigateUpTo(this, new Intent(this,
					SceneListActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}

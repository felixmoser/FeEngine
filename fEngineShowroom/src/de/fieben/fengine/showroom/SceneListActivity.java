package de.fieben.fengine.showroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class SceneListActivity extends FragmentActivity
        implements SceneListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_list);

        if (findViewById(R.id.scene_detail_container) != null) {
            mTwoPane = true;
            ((SceneListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.scene_list))
                    .setActivateOnItemClick(true);
        }
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(SceneDetailFragment.ARG_ITEM_ID, id);
            SceneDetailFragment fragment = new SceneDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.scene_detail_container, fragment)
                    .commit();

        } else {
            Intent detailIntent = new Intent(this, SceneDetailActivity.class);
            detailIntent.putExtra(SceneDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}

package budny.moneykeeper.ui.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import budny.moneykeeper.R;
import budny.moneykeeper.ui.fragments.impl.FragmentPreferences;

/**
 * An activity used to edit app's preferences.
 */
public class ActivityPreferences extends AppCompatActivity {
    @SuppressWarnings("unused")
    private static final String TAG = ActivityPreferences.class.getSimpleName();

    @SuppressWarnings("FieldCanBeLocal")
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        initViews();
        initFragments();
    }

    private void initViews() {
        // toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDefaultDisplayHomeAsUpEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initFragments() {
        getFragmentManager()
                .beginTransaction()
                .add(R.id.activity_preferences_fragment_container, new FragmentPreferences())
                .commit();
    }
}

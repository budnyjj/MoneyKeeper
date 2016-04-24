package budny.moneykeeper.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import budny.moneykeeper.R;
import budny.moneykeeper.ui.fragments.FragmentPreferences;

public class ActivityPreferences extends AppCompatActivity {
    @SuppressWarnings("unused")
    private static final String TAG = ActivityPreferences.class.getSimpleName();

    @SuppressWarnings("FieldCanBeLocal")
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setupActionBar(getSupportActionBar());

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_camera_content, new FragmentPreferences())
                .commit();
    }

    private void setupActionBar(@Nullable ActionBar bar) {
        if (bar != null) {
            bar.setDefaultDisplayHomeAsUpEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }
}

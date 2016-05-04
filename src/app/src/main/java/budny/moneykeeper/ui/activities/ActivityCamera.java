package budny.moneykeeper.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import budny.moneykeeper.R;
import budny.moneykeeper.ui.fragments.impl.FragmentCamera;

/**
 * TODO: remove it
 */
public class ActivityCamera extends AppCompatActivity {
    @SuppressWarnings("unused")
    private static final String TAG = ActivityCamera.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setupFragments();
    }

    private void setupFragments() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_camera_fragment_container, new FragmentCamera())
                .commit();
    }
}

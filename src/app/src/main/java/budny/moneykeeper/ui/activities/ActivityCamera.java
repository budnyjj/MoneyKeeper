package budny.moneykeeper.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import budny.moneykeeper.R;
import budny.moneykeeper.ui.fragments.FragmentCamera;

public class ActivityCamera extends AppCompatActivity {
    @SuppressWarnings("unused")
    private static final String TAG = ActivityCamera.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_camera_content, new FragmentCamera())
                .commit();
    }
}

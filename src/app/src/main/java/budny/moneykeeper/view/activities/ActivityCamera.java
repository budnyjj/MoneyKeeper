package budny.moneykeeper.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import budny.moneykeeper.R;
import budny.moneykeeper.view.fragments.FragmentCamera;

public class ActivityCamera extends AppCompatActivity {
    private static final String TAG = ActivityCamera.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.layout_camera, new FragmentCamera())
                .commit();
    }
}

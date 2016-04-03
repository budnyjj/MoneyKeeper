package budny.moneykeeper.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import budny.moneykeeper.R;
import budny.moneykeeper.cv.OpenCVLoader;

public class ActivityInput extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        OpenCVLoader.init();
    }
}

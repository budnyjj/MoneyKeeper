package budny.moneykeeper.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import budny.moneykeeper.R;
import budny.moneykeeper.cv.DigitRecognizer;

public class InputActivity extends AppCompatActivity {
    private DigitRecognizer digitRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        digitRecognizer = DigitRecognizer.getInstance();
    }
}

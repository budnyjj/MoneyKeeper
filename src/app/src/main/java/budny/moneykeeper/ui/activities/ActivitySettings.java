package budny.moneykeeper.ui.activities;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.PresenterMainPreferences;

public class ActivitySettings extends AppCompatActivity {
    private static final String TAG = ActivitySettings.class.getSimpleName();

    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_settings_content, new FragmentMainPreferences())
                .commit();
    }

    public static class FragmentMainPreferences extends PreferenceFragment {
        private PresenterMainPreferences mPresenter;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            mPresenter = PresenterMainPreferences.getInstance();

            final String defaultCurrencyKey =
                    getResources().getString(R.string.pref_key_default_currency);
            final ListPreference defaultCurrencyPreference =
                    (ListPreference) findPreference(defaultCurrencyKey);
            defaultCurrencyPreference.setEntries(mPresenter.getCurrencyNames());
            defaultCurrencyPreference.setEntryValues(mPresenter.getCurrencyCodes());
            defaultCurrencyPreference.setDefaultValue(mPresenter.getDefaultCurrencyCode());
        }
    }
}

package budny.moneykeeper.ui.fragments;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterMainPreferences;
import budny.moneykeeper.bl.presenters.impl.PresenterPreferences;

public class FragmentPreferences extends PreferenceFragment {
    private IPresenterMainPreferences mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        mPresenter = PresenterPreferences.getInstance();

        final String defaultCurrencyKey =
                getResources().getString(R.string.pref_key_default_currency);
        final ListPreference defaultCurrencyPreference =
                (ListPreference) findPreference(defaultCurrencyKey);
        defaultCurrencyPreference.setEntries(mPresenter.getCurrencyNames());
        defaultCurrencyPreference.setEntryValues(mPresenter.getCurrencyCodes());
        defaultCurrencyPreference.setDefaultValue(mPresenter.getDefaultCurrencyCode());
    }
}
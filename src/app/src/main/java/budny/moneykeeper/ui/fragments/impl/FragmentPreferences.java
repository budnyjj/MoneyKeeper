package budny.moneykeeper.ui.fragments.impl;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterFragmentPreferences;
import budny.moneykeeper.bl.presenters.impl.PresenterFragmentPreferences;

public class FragmentPreferences extends PreferenceFragment {
    private IPresenterFragmentPreferences mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        mPresenter = new PresenterFragmentPreferences();

        final String defaultCurrencyKey =
                getResources().getString(R.string.pref_key_default_currency);
        final ListPreference defaultCurrencyPreference =
                (ListPreference) findPreference(defaultCurrencyKey);
        defaultCurrencyPreference.setEntries(mPresenter.getCurrencyNames());
        defaultCurrencyPreference.setEntryValues(mPresenter.getCurrencyCodes());
        defaultCurrencyPreference.setDefaultValue(mPresenter.getDefaultCurrencyCode());
    }
}
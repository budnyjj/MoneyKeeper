package budny.moneykeeper.bl.presenters.impl;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Set;

import budny.moneykeeper.bl.presenters.IPresenterFragmentPreferences;

public class PresenterFragmentPreferences implements IPresenterFragmentPreferences {
    @SuppressWarnings("unused")
    private static final String TAG = PresenterFragmentPreferences.class.getSimpleName();

    private final List<CharSequence> mCurrencyNames;
    private final List<CharSequence> mCurrencyCodes;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public PresenterFragmentPreferences() {
        Set<Currency> currencies = Currency.getAvailableCurrencies();
        mCurrencyNames = new ArrayList<>(currencies.size());
        for (Currency c : currencies) {
            mCurrencyNames.add(c.getDisplayName());
        }
        mCurrencyCodes = new ArrayList<>(currencies.size());
        for (Currency c : currencies) {
            mCurrencyCodes.add(c.getCurrencyCode());
        }
    }

    @Override
    public CharSequence[] getCurrencyNames() {
        return mCurrencyNames.toArray(new CharSequence[mCurrencyNames.size()]);
    }

    @Override
    public CharSequence[] getCurrencyCodes() {
        return mCurrencyCodes.toArray(new CharSequence[mCurrencyCodes.size()]);
    }

    @Override
    public CharSequence getDefaultCurrencyCode() {
        return "USD";
    }
}

package budny.moneykeeper.bl.presenters.impl;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Set;

import budny.moneykeeper.bl.presenters.IPresenterMainPreferences;

public class PresenterPreferences implements IPresenterMainPreferences {
    @SuppressWarnings("unused")
    private static final String TAG = PresenterPreferences.class.getSimpleName();

    private final List<CharSequence> mCurrencyNames;
    private final List<CharSequence> mCurrencyCodes;

    public static PresenterPreferences getInstance() {
        return Holder.INSTANCE;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private PresenterPreferences() {
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

    private static class Holder {
        private static final PresenterPreferences INSTANCE = new PresenterPreferences();
    }
}

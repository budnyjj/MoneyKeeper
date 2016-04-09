package budny.moneykeeper.presenter;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Set;

public class PresenterMainPreferences {
    private static final String TAG = PresenterMainPreferences.class.getSimpleName();

    private final List<CharSequence> mCurrencyNames;
    private final List<CharSequence> mCurrencyCodes;

    public static PresenterMainPreferences getInstance() {
        return Holder.INSTANCE;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private PresenterMainPreferences() {
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

    public CharSequence[] getCurrencyNames() {
        return mCurrencyNames.toArray(new CharSequence[mCurrencyNames.size()]);
    }

    public CharSequence[] getCurrencyCodes() {
        return mCurrencyCodes.toArray(new CharSequence[mCurrencyCodes.size()]);
    }

    public CharSequence getDefaultCurrencyCode() {
        return "USD";
    }

    private static class Holder {
        private static final PresenterMainPreferences INSTANCE = new PresenterMainPreferences();
    }
}

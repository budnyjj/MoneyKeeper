package budny.moneykeeper.bl.presenters;

public interface IPresenterMainPreferences {
    CharSequence[] getCurrencyNames();

    CharSequence[] getCurrencyCodes();

    CharSequence getDefaultCurrencyCode();
}

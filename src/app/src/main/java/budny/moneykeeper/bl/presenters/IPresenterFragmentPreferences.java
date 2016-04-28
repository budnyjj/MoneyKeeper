package budny.moneykeeper.bl.presenters;

public interface IPresenterFragmentPreferences {
    CharSequence[] getCurrencyNames();

    CharSequence[] getCurrencyCodes();

    CharSequence getDefaultCurrencyCode();
}

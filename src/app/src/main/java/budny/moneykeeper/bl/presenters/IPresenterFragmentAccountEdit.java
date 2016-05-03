package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterFragmentAccountEdit {
    void onStart();

    void onStop();

    void createAccount(String name, String currencyCode);

    void updateAccount(String name, String currencyCode);

    Account getAccount();

    String getCurrencyName(int index);

    void selectCurrency(int index);

    boolean isSelectedCurrency(int index);

    String getSelectedCurrencyCode();

    void addCurrencySelectedListener(IDataChangeListener listener);

    int getNumCurrencies();
}

package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.model.BalanceChange;
import budny.moneykeeper.db.model.Currency;
import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterFragmentAccountView {
    void onStart();

    void onStop();

    void addDataChangeListener(IDataChangeListener listener);

    int getNumBalanceChanges();

    BalanceChange getBalanceChange(int index);

    String getCurrencyCode();

    long getTotalAmount();
}

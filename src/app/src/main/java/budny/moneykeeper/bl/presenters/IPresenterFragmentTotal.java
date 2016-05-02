package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterFragmentTotal {
    void onStart();

    void onStop();

    void addDataChangeListener(IDataChangeListener listener);

    long getTotalAmount();

    int getNumAccounts();

    Account getAccount(int index);
}

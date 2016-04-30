package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterFragmentAccounts {
    void onStart();

    void onStop();

    void addDataChangeListener(IDataChangeListener listener);

    int getNumAccounts();

    Account getAccount(int position);

    void deleteAccount(int position);

    void swapAccounts(int fromPosition, int toPosition);
}

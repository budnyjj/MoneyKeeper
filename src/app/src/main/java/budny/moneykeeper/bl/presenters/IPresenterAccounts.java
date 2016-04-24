package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.model.Account;

public interface IPresenterAccounts {
    void onStart();

    void onStop();

    int getNumAccounts();

    Account getAccount(int position);

    void removeAccount(int position);

    void swapAccounts(int fromPosition, int toPosition);
}

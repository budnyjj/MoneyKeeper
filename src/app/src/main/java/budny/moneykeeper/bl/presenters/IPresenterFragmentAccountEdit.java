package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.model.Account;

public interface IPresenterFragmentAccountEdit {
    void onStart();

    void onStop();

    void createAccount(Account account);

    void updateAccount(Account srcAccount, int dstIndex);

    Account getAccount(int index);
}

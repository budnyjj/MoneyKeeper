package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.model.Account;

public interface IPresenterFragmentAccountEdit {
    void onStart();

    void onStop();

    void addAccount(Account account);
}

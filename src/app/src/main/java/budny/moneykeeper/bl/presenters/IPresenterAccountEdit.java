package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.model.Account;

public interface IPresenterAccountEdit {
    void onStart();

    void onStop();

    void addAccount(Account account);
}

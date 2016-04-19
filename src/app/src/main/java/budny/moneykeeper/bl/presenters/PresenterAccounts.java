package budny.moneykeeper.bl.presenters;

import java.util.List;

import budny.moneykeeper.db.DBManager;
import budny.moneykeeper.db.model.Account;

public class PresenterAccounts {
    private final DBManager mDbManager = DBManager.getInstance();

    public List<Account> getAccounts() {
        return mDbManager.getAccounts();
    }
}

package budny.moneykeeper.db.operations;

import budny.moneykeeper.db.model.Account;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A collection of frequently-used {@linkplain budny.moneykeeper.db.model.Account}-related operations.
 */
public class AccountOperations {
    /**
     * Retrieves accounts from database.
     */
    public static RealmResults<Account> getAccounts(Realm realm) {
        RealmResults<Account> accounts = realm.where(Account.class).findAll();
        accounts.sort(Account.FIELD_INDEX);
        return accounts;
    }

    /**
     * Swaps two accounts in database.
     */
    public static void swapAccounts(Realm realm, final Account fromAccount, final Account toAccount) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                int fromIndex = fromAccount.getIndex();
                fromAccount.setIndex(toAccount.getIndex());
                toAccount.setIndex(fromIndex);
            }
        });
    }
}

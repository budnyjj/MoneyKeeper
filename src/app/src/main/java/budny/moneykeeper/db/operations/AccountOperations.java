package budny.moneykeeper.db.operations;

import budny.moneykeeper.db.model.Account;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A collection of frequently-used {@linkplain budny.moneykeeper.db.model.Account}-related operations.
 */
public class AccountOperations {
    /**
     * Stores account in database.
     * Automatically setups account's index.
     */
    public static void createAccount(Realm realm, final Account account) {
        // get maximal index value of existing accounts
        Number maxIndex = realm.where(Account.class).max(Account.FIELD_INDEX);
        // set greater index value in the new account
        int accountIndex = maxIndex == null ? 0 : maxIndex.intValue() + 1;
        account.setIndex(accountIndex);
        // store account
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(account);
            }
        });
    }

    /**
     * Retrieves accounts from database.
     */
    public static RealmResults<Account> getAccounts(Realm realm) {
        return realm.where(Account.class).findAllSorted(Account.FIELD_INDEX);
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

    /**
     * Updates contents of specified account.
     *
     * @param realm Realm instance
     * @param srcAccount source account with new values
     * @param dstAccount account to be updated
     */
    public static void updateAccount(Realm realm, final Account srcAccount, final Account dstAccount) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dstAccount.setName(srcAccount.getName());
            }
        });
    }
}

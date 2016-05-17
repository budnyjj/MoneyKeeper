package budny.moneykeeper.db.operations;

import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.model.BalanceChange;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A collection of frequently-used {@linkplain budny.moneykeeper.db.model.Account}-related operations.
 */
public class AccountOperations {
    /**
     * Stores account in database.
     * Initializes account's fields from method parameters.
     *
     * @return managed {@linkplain Account} instance
     */
    public static Account create(Realm realm,
                                 String name, String currencyCode) {
        Account account = new Account();
        account.setName(name);
        account.setCurrencyCode(currencyCode);
        // get maximal index value of existing accounts
        Number maxIndex = realm.where(Account.class).max(Account.FIELD_INDEX);
        // set greater index value in the new account
        int accountIndex = maxIndex == null ? 0 : maxIndex.intValue() + 1;
        account.setIndex(accountIndex);
        // store in database
        realm.beginTransaction();
        Account managedAccount = realm.copyToRealm(account);
        realm.commitTransaction();
        return managedAccount;
    }

    /**
     * Retrieves accounts from database.
     */
    public static RealmResults<Account> read(Realm realm) {
        return realm.where(Account.class).findAllSorted(Account.FIELD_INDEX);
    }

    /**
     * Swaps two accounts in database.
     */
    public static void swap(Realm realm, final Account fromAccount, final Account toAccount) {
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
     * @param dstAccount account to be updated
     */
    public static void update(Realm realm,
                              final Account dstAccount, final String name, final String currencyCode) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dstAccount.setName(name);
                dstAccount.setCurrencyCode(currencyCode);
            }
        });
    }

    /**
     * Adds balance change to specified account.
     *
     * @param realm Realm instance
     * @param account account to update, managed by Realm
     * @param change balance change to add, managed by Realm
     */
    public static void addBalanceChange(Realm realm, final Account account, final BalanceChange change) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                account.getBalanceChanges().add(change);
            }
        });
    }

    /**
     * Adds balance change to specified account.
     *
     * This method doesn't delete balance change from database.
     *
     * @param realm Realm instance
     * @param account account to update, managed by Realm
     * @param change balance change to remove, managed by Realm
     */
    public static void removeBalanceChange(Realm realm, final Account account, final BalanceChange change) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                account.getBalanceChanges().remove(change);
            }
        });
    }
}

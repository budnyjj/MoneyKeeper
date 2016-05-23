package budny.moneykeeper.db.operations;

import java.util.Date;
import java.util.Set;

import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.model.BalanceChange;
import budny.moneykeeper.db.model.Category;
import io.realm.Realm;

public class BalanceChangeOperations {
    /**
     * Stores balance change in database.
     * Initializes change's fields from method parameters.
     *
     * @return managed {@linkplain budny.moneykeeper.db.model.BalanceChange} instance
     */
    public static BalanceChange create(Realm realm, long amount, Date date) {
        BalanceChange change = new BalanceChange();
        change.setAmount(amount);
        change.setDate(date);
        return BalanceChangeOperations.create(realm, change);
    }

    /**
     * Stores balance change in database.
     * Initializes change's fields from prototype.
     *
     * @return managed {@linkplain budny.moneykeeper.db.model.BalanceChange} instance
     */
    public static BalanceChange create(Realm realm, BalanceChange change) {
        // store in database
        realm.beginTransaction();
        BalanceChange managedChange = realm.copyToRealm(change);
        realm.commitTransaction();
        return managedChange;
    }

    /**
     * Updates contents of specified balance change.
     *
     * @param realm Realm instance
     * @param change balance change to be updated
     */
    public static void update(
            Realm realm, final BalanceChange change, final long amount, final Date date) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                change.setAmount(amount);
                change.setDate(date);
            }
        });
    }

    /**
     * Adds category to the specified balance change.
     *
     * @param realm Realm instance
     * @param change balance change to update, managed by Realm
     * @param category to add, managed by Realm
     */
    public static void addCategory(Realm realm, final BalanceChange change, final Category category) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                change.getCategories().add(category);
            }
        });
    }

    /**
     * Removes all categories from the specified balance change.
     *
     * @param realm Realm instance
     * @param change balance change to update, managed by Realm
     */
    public static void clearCategories(Realm realm, final BalanceChange change) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                change.getCategories().clear();
            }
        });
    }
}

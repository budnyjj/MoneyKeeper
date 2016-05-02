package budny.moneykeeper.db.operations;

import java.util.Date;

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
    public static BalanceChange create(Realm realm, long amount, Category category, Date date) {
        BalanceChange change = new BalanceChange();
        change.setAmount(amount);
        change.setCategory(category);
        change.setDate(date);
        return CommonOperations.create(realm, change);
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
}

package budny.moneykeeper.db.operations;

import budny.moneykeeper.db.model.BalanceChange;
import budny.moneykeeper.db.model.Category;
import io.realm.Realm;

/**
 * Created by budnyjj on 5/1/16.
 */
public class BalanceChangeOperations {
    /**
     * Stores balance change in database.
     * Initializes change's fields from method parameters.
     *
     * @return managed {@linkplain budny.moneykeeper.db.model.BalanceChange} instance
     */
    public static BalanceChange createBalanceChange(Realm realm, long amount) {
        BalanceChange change = new BalanceChange();
        change.setAmount(amount);
        return CommonOperations.createObject(realm, change);
    }
}

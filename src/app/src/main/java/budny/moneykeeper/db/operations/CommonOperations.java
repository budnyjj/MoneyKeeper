package budny.moneykeeper.db.operations;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * A collection of frequently-used operations on Realm objects.
 */
public class CommonOperations {
    /**
     * Stores object in database.
     */
    public static <T extends RealmObject> void add(Realm realm, final T obj) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(obj);
            }
        });
    }

    /**
     * Stores object in database.
     */
    public static <T extends RealmObject> void remove(Realm realm, final T obj) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                obj.removeFromRealm();
            }
        });
    }
}

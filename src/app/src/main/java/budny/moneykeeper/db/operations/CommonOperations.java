package budny.moneykeeper.db.operations;

import budny.moneykeeper.db.model.Category;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * A collection of frequently-used operations on Realm objects.
 */
public class CommonOperations {
    /**
     * Stores object in database.
     *
     * @return object, managed by Realm
     */
    public static <T extends RealmObject> T createObject(Realm realm, final T obj) {
        realm.beginTransaction();
        T managedObj = realm.copyToRealm(obj);
        realm.commitTransaction();
        return managedObj;
    }

    /**
     * Deletes object from database.
     */
    public static <T extends RealmObject> void deleteObject(Realm realm, final T obj) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                obj.deleteFromRealm();
            }
        });
    }

    /**
     * Deletes object from database (for lists).
     */
    public static void deleteObject(Realm realm, final RealmResults results, final int position) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteFromRealm(position);
            }
        });
    }
}

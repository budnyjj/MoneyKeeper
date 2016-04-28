package budny.moneykeeper.db.operations;

import budny.moneykeeper.db.util.IDataChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * A collection of frequently-used operations on Realm objects.
 */
public class CommonOperations {
    /**
     * Stores object in database.
     */
    public static <T extends RealmObject> void addObject(Realm realm, final T obj) {
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
    public static <T extends RealmObject> void deleteObject(Realm realm, final T obj) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                obj.deleteFromRealm();
            }
        });
    }

    /**
     * Adds change data listener to database engine.
     */
    public static void addDataChangeListener(Realm realm, final IDataChangeListener listener) {
        realm.addChangeListener(new RealmChangeListener() {
            @Override
            public void onChange() {
                listener.onChange();
            }
        });
    }
}

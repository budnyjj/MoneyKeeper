package budny.moneykeeper.db.util;

import android.content.Context;

import io.realm.Realm;

public interface IDBManager {
    void init(Context context);
    Realm getRealm();
}

package budny.moneykeeper.db.util.impl;

import android.content.Context;

import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.db.operations.AccountOperations;
import budny.moneykeeper.db.operations.CommonOperations;
import budny.moneykeeper.db.util.IDBManager;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DBManager implements IDBManager {
    @SuppressWarnings("unused")
    private static final String TAG = DBManager.class.getSimpleName();
    private static final String DEFAULT_ACCOUNT_NAME = "Default";
    private static final String[] DEFAULT_CATEGORIES = new String[]{"Food", "Auto", "Salary", "Home", "Misc"};
    private static final String REALM_FILE_NAME = "data.realm";

    private volatile boolean mInitialized = false;

    public static DBManager getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void init(Context context) {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context)
                .name(REALM_FILE_NAME)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm.deleteRealm(realmConfiguration);

        Realm realm = Realm.getDefaultInstance();
        fillDatabaseWithDefaultData(realm);
        mInitialized = true;
    }

    @Override
    public Realm getRealm() {
        if (!mInitialized) {
            throw new IllegalStateException("DBManager is not initialized");
        }
        return Realm.getDefaultInstance();
    }

    private void fillDatabaseWithDefaultData(Realm realm) {
        Account defaultAccount = new Account();
        defaultAccount.setName(DEFAULT_ACCOUNT_NAME);
        // TODO: set default currency automatically
        defaultAccount.setCurrencyCode("USD");
        AccountOperations.createAccount(realm, defaultAccount);

        Account defaultAccount2 = new Account();
        defaultAccount2.setName(DEFAULT_ACCOUNT_NAME + "2");
        // TODO: set default currency automatically
        defaultAccount2.setCurrencyCode("USD");
        AccountOperations.createAccount(realm, defaultAccount2);

        for (String categoryName : DEFAULT_CATEGORIES) {
            Category category = new Category();
            category.setName(categoryName);
            CommonOperations.createObject(realm, category);
        }
    }

    private DBManager() {
        // to do not be instantiated by mistake
    }

    private static class Holder {
        private static final DBManager INSTANCE = new DBManager();
    }
}

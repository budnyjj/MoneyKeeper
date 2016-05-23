package budny.moneykeeper.db.util.impl;

import android.content.Context;

import java.util.Date;
import java.util.List;

import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.model.BalanceChange;
import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.db.operations.AccountOperations;
import budny.moneykeeper.db.operations.BalanceChangeOperations;
import budny.moneykeeper.db.operations.CategoryOperations;
import budny.moneykeeper.db.util.IDBManager;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DBManager implements IDBManager {
    @SuppressWarnings("unused")
    private static final String TAG = DBManager.class.getSimpleName();
    private static final String REALM_FILE_NAME = "data.realm";

    private static final String DEFAULT_ACCOUNT_NAME = "Default";
    private static final String[] DEFAULT_CATEGORIES_INCOME =
            new String[]{"Salary"};
    private static final String[] DEFAULT_CATEGORIES_OUTCOME =
            new String[]{"Food", "Auto", "Home", "Misc"};

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
        for (String categoryName : DEFAULT_CATEGORIES_INCOME) {
            CategoryOperations.create(realm, categoryName, Category.Type.INCOME);
        }
        for (String categoryName : DEFAULT_CATEGORIES_OUTCOME) {
            CategoryOperations.create(realm, categoryName, Category.Type.OUTCOME);
        }
        List<Category> categories = CategoryOperations.read(realm);

        BalanceChange bc1 = BalanceChangeOperations.create(realm, 100_150_000_000L, new Date());
        BalanceChangeOperations.addCategory(realm, bc1, categories.get(0));
        BalanceChangeOperations.addCategory(realm, bc1, categories.get(1));
        BalanceChange bc2 = BalanceChangeOperations.create(realm, -200_300_000_000L, new Date());
        BalanceChangeOperations.addCategory(realm, bc2, categories.get(0));
        BalanceChangeOperations.addCategory(realm, bc2, categories.get(2));
        BalanceChange bc3 = BalanceChangeOperations.create(realm, 50_100_000_000L, new Date());

        Account defaultAccount = AccountOperations.create(realm, DEFAULT_ACCOUNT_NAME, "USD");
        AccountOperations.addBalanceChange(realm, defaultAccount, bc1);
        AccountOperations.addBalanceChange(realm, defaultAccount, bc2);
        AccountOperations.addBalanceChange(realm, defaultAccount, bc3);

        Account defaultAccount2 = AccountOperations.create(realm, DEFAULT_ACCOUNT_NAME + "2", "EUR");
        AccountOperations.addBalanceChange(realm, defaultAccount2, bc1);
        AccountOperations.addBalanceChange(realm, defaultAccount2, bc2);

        Account defaultAccount3 = AccountOperations.create(realm, DEFAULT_ACCOUNT_NAME + "3", "BYR");
    }

    private DBManager() {
        // to do not be instantiated by mistake
    }

    private static class Holder {
        private static final DBManager INSTANCE = new DBManager();
    }
}

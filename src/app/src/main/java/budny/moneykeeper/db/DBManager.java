package budny.moneykeeper.db;

import android.content.Context;

import java.util.List;

import budny.moneykeeper.db.model.Account;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DBManager {
    private static final String TAG = DBManager.class.getSimpleName();
    private static final String DEFAULT_ACCOUNT_NAME = "Default";
    private static final String REALM_FILE_NAME = "data.realm";

    private boolean mInitialized = false;

    private Realm mRealmInstance;

    public static DBManager getInstance() {
        return Holder.INSTANCE;
    }

    public void init(Context context) {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context)
                .name(REALM_FILE_NAME)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm.deleteRealm(realmConfiguration);

        mRealmInstance = Realm.getDefaultInstance();
        fillDatabaseWithDefaultData();

        mInitialized = true;
    }

    public List<Account> getAccounts() {
        return mRealmInstance.where(Account.class).findAll();
    }

    public void addAccount(final Account account) {
        mRealmInstance.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(account);
            }
        });
    }

    private void fillDatabaseWithDefaultData() {
        Account defaultAccount = new Account();
        defaultAccount.setName(DEFAULT_ACCOUNT_NAME);
        // TODO: set default currency automatically
        defaultAccount.setCurrencyCode("USD");
        addAccount(defaultAccount);
    }

    private DBManager() {
        // to do not be instantiated by mistake
    }

    private static class Holder {
        private static final DBManager INSTANCE = new DBManager();
    }
}

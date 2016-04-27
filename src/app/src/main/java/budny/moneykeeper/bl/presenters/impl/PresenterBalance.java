package budny.moneykeeper.bl.presenters.impl;

import android.support.v4.app.Fragment;

import budny.moneykeeper.bl.presenters.IPresenterBalance;
import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.operations.AccountOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.impl.DBManager;
import budny.moneykeeper.ui.fragments.FragmentAccount;
import io.realm.Realm;
import io.realm.RealmResults;

public class PresenterBalance implements IPresenterBalance {
    private static final String TAG = PresenterBalance.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";

    private final IDBManager mDbManager = DBManager.getInstance();

    private Realm mRealm;
    private RealmResults<Account> mAccounts;

    private volatile boolean mInitialized;

    @Override
    public void onCreate() {
        mRealm = mDbManager.getRealm();
        mAccounts = AccountOperations.getAccounts(mRealm);
        mInitialized = true;
    }

    @Override
    public void onDestroy() {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        mAccounts = null;
        mRealm.close();
        mInitialized = false;
    }

    @Override
    public Fragment getAccountFragment(int position) {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        return new FragmentAccount();
    }

    @Override
    public CharSequence getAccountName(int position) {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        return mAccounts.get(position).getName();
    }

    @Override
    public int getNumAccounts() {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        return mAccounts.size();
    }
}

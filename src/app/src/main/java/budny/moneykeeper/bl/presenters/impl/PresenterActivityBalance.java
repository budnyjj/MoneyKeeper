package budny.moneykeeper.bl.presenters.impl;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import budny.moneykeeper.bl.presenters.IPresenterActivityBalance;
import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.operations.AccountOperations;
import budny.moneykeeper.db.operations.CommonOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.db.util.impl.DBManager;
import budny.moneykeeper.ui.fragments.FragmentAccount;
import io.realm.Realm;
import io.realm.RealmResults;

public class PresenterActivityBalance implements IPresenterActivityBalance {
    private static final String TAG = PresenterActivityBalance.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";

    private final IDBManager mDbManager = DBManager.getInstance();
    private final List<IDataChangeListener> mDataChangeListeners = new ArrayList<>();

    private Realm mRealm;
    private RealmResults<Account> mAccounts;

    private volatile boolean mInitialized;

    @Override
    public void onCreate() {
        mRealm = mDbManager.getRealm();
        mAccounts = AccountOperations.getAccounts(mRealm);
        for (final IDataChangeListener listener : mDataChangeListeners) {
            CommonOperations.addDataChangeListener(mRealm, listener);
        }
        mInitialized = true;
    }

    @Override
    public void onDestroy() {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        mAccounts = null;
        mDataChangeListeners.clear();
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

    @Override
    public void addDataChangeListener(final IDataChangeListener listener) {
        // if presenter was already initialized, addObject change listener immediately,
        if (mInitialized) {
            CommonOperations.addDataChangeListener(mRealm, listener);
        }
        // store listener in temporary list to be added during initialization
        mDataChangeListeners.add(listener);
    }
}

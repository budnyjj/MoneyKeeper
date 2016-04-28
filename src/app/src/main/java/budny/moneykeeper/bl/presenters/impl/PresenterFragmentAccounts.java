package budny.moneykeeper.bl.presenters.impl;

import java.util.ArrayList;
import java.util.List;

import budny.moneykeeper.bl.presenters.IPresenterFragmentAccounts;
import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.operations.AccountOperations;
import budny.moneykeeper.db.operations.CommonOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.db.util.impl.DBManager;
import io.realm.Realm;
import io.realm.RealmResults;

public class PresenterFragmentAccounts implements IPresenterFragmentAccounts {
    private static final String TAG = PresenterFragmentAccounts.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";

    private final IDBManager mDbManager = DBManager.getInstance();
    private final List<IDataChangeListener> mDataChangeListeners = new ArrayList<>();

    private Realm mRealm;
    private RealmResults<Account> mAccounts;

    private volatile boolean mInitialized;

    @Override
    public void onStart() {
        mRealm = mDbManager.getRealm();
        mAccounts = AccountOperations.getAccounts(mRealm);
        for (final IDataChangeListener listener : mDataChangeListeners) {
            CommonOperations.addDataChangeListener(mRealm, listener);
        }
        mInitialized = true;
    }

    @Override
    public void onStop() {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        mAccounts = null;
        mRealm.close();
        mInitialized = false;
    }

    @Override
    public int getNumAccounts() {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        return mAccounts.size();
    }

    @Override
    public Account getAccount(int position) {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        return mAccounts.get(position);
    }

    @Override
    public void removeAccount(int position) {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        CommonOperations.deleteObject(mRealm, mAccounts.get(position));
    }

    @Override
    public void swapAccounts(int fromPosition, int toPosition) {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        AccountOperations.swapAccounts(mRealm, mAccounts.get(fromPosition), mAccounts.get(toPosition));
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

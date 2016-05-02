package budny.moneykeeper.bl.presenters.impl;

import java.util.ArrayList;
import java.util.List;

import budny.moneykeeper.bl.presenters.IPresenterFragmentAccountView;
import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.model.BalanceChange;
import budny.moneykeeper.db.operations.AccountOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.db.util.impl.DBManager;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class PresenterFragmentAccountView implements IPresenterFragmentAccountView {
    private static final String TAG = PresenterFragmentAccountView.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";

    private final int mAccountIndex;
    private final IDBManager mDbManager = DBManager.getInstance();
    // preserves data change listeners from being garbage collected
    // and saves them from being removed during fragment lifecycle
    private final List<RealmChangeListener> mChangeListeners = new ArrayList<>();

    private Realm mRealm;
    private Account mAccount;

    private volatile boolean mInitialized;

    public PresenterFragmentAccountView(int accountIndex) {
        mAccountIndex = accountIndex;
    }

    @Override
    public void onStart() {
        mRealm = mDbManager.getRealm();
        mAccount = AccountOperations.read(mRealm).get(mAccountIndex);
        // add pending listeners
        for (final RealmChangeListener listener : mChangeListeners) {
            mAccount.addChangeListener(listener);
        }
        mInitialized = true;
    }

    @Override
    public void onStop() {
        checkInitialized();
        mAccount.removeChangeListeners();
        mAccount = null;
        mRealm.close();
        mInitialized = false;
    }

    @Override
    public void addDataChangeListener(final IDataChangeListener listener) {
        // wrap data change listener into realm data change listener
        RealmChangeListener realmListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                listener.onChange();
            }
        };
        // store listener in list to preserve it from GC
        mChangeListeners.add(realmListener);
        // if already initialized, add change listener immediately
        if (mInitialized) {
            mAccount.addChangeListener(realmListener);
        }
    }

    @Override
    public int getNumBalanceChanges() {
        return mAccount.getBalanceChanges().size();
    }

    @Override
    public BalanceChange getBalanceChange(int index) {
        return mAccount.getBalanceChanges().get(index);
    }

    @Override
    public String getCurrencyCode() {
        return mAccount.getCurrencyCode();
    }

    @Override
    public long getTotalAmount() {
        return mAccount.getTotalAmount();
    }

    /**
     * Checks, if presenter is in active state.
     */
    private void checkInitialized() {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
    }
}

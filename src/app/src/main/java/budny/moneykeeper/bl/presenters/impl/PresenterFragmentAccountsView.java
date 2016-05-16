package budny.moneykeeper.bl.presenters.impl;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import budny.moneykeeper.bl.presenters.IPresenterFragmentAccountsView;
import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.operations.AccountOperations;
import budny.moneykeeper.db.operations.CommonOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.db.util.impl.DBManager;
import budny.moneykeeper.ui.activities.ActivityAccountEdit;
import budny.moneykeeper.ui.misc.IntentExtras;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class PresenterFragmentAccountsView implements IPresenterFragmentAccountsView {
    private static final String TAG = PresenterFragmentAccountsView.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";

    private final Context mContext;
    private final IDBManager mDbManager = DBManager.getInstance();
    // array of indexes of accounts which are exposed to presenter client
    private final ArrayList<Integer> mAccountActiveIndexes = new ArrayList<>();
    // stack of indexes of accounts which were selected to be deleted
    private final List<Integer> mAccountToDeleteIndexes = new ArrayList<>();
    // preserves data change listeners from being garbage collected
    // and saves them from being removed during fragment lifecycle
    private final List<RealmChangeListener> mChangeListeners = new ArrayList<>();

    private Realm mRealm;
    private RealmResults<Account> mAccounts;

    private volatile boolean mInitialized;

    public PresenterFragmentAccountsView(Context context) {
        mContext = context;
    }

    @Override
    public void onStart() {
        mRealm = mDbManager.getRealm();
        mAccounts = AccountOperations.read(mRealm);
        // fill list of indexes of active accounts
        for (int accountIndex = 0; accountIndex < mAccounts.size(); accountIndex++) {
            mAccountActiveIndexes.add(accountIndex);
        }
        // add pending listeners
        for (final RealmChangeListener listener : mChangeListeners) {
            mAccounts.addChangeListener(listener);
        }
        mInitialized = true;
    }

    @Override
    public void onStop() {
        checkInitialized();
        // delete pending accounts
        for (int accountIndex : mAccountToDeleteIndexes) {
            CommonOperations.delete(mRealm, mAccounts, accountIndex);
        }
        mAccounts.removeChangeListeners();
        mAccounts = null;
        mRealm.close();
        mInitialized = false;
    }

    @Override
    public int getNumAccounts() {
        checkInitialized();
        return mAccountActiveIndexes.size();
    }

    @Override
    public String getAccountName(int index) {
        checkInitialized();
        int internalIndex = mAccountActiveIndexes.get(index);
        return mAccounts.get(internalIndex).getName();
    }

    /**
     * Starts {@linkplain budny.moneykeeper.ui.activities.ActivityAccountEdit}
     * to update specified category.
     *
     * @param index   index of category to edit
     */
    @Override
    public void updateAccount(int index) {
        int internalIndex = mAccountActiveIndexes.get(index);
        Intent intent = new Intent(mContext, ActivityAccountEdit.class);
        intent.putExtra(IntentExtras.FIELD_ACTION, IntentExtras.ACTION_UPDATE);
        intent.putExtra(IntentExtras.FIELD_INDEX_ACCOUNT, internalIndex);
        mContext.startActivity(intent);
    }

    /**
     * Deletes specified account.
     *
     * @param position index of account to delete
     * @return true, if the actual deletion was performed
     */
    @Override
    public boolean deleteAccount(int position) {
        checkInitialized();
        mAccountToDeleteIndexes.add(mAccountActiveIndexes.remove(position));
        // notify clients about changes
        for (RealmChangeListener listener : mChangeListeners) {
            listener.onChange();
        }
        return true;
    }

    @Override
    public boolean unDeleteLastAccount(int position) {
        checkInitialized();
        int numAccountsToDelete = mAccountToDeleteIndexes.size();
        if (numAccountsToDelete == 0) {
            // there are no deleted accounts
            return false;
        }
        mAccountActiveIndexes.add(position, mAccountToDeleteIndexes.remove(numAccountsToDelete - 1));
        // notify clients about changes
        for (RealmChangeListener listener : mChangeListeners) {
            listener.onChange();
        }
        return true;
    }

    @Override
    public void swapAccounts(int fromIndex, int toIndex) {
        checkInitialized();
        int internalFromIndex = mAccountActiveIndexes.get(fromIndex);
        int internalToIndex = mAccountActiveIndexes.get(toIndex);
        Account fromAccount = mAccounts.get(internalFromIndex);
        Account toAccount = mAccounts.get(internalToIndex);
        AccountOperations.swap(mRealm, fromAccount, toAccount);
        // notify clients about changes
        for (RealmChangeListener listener : mChangeListeners) {
            listener.onChange();
        }
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
            mAccounts.addChangeListener(realmListener);
        }
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

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

/**
 * The primary implementation of {@linkplain IPresenterFragmentAccountsView}.
 * <p/>
 * It supports the actual list of accounts, queried from Realm,
 * the list of account indexes, which are exposed to the client and
 * the stack of account indexes which are marked to be deleted.
 */
public class PresenterFragmentAccountsView implements IPresenterFragmentAccountsView {
    private static final String TAG = PresenterFragmentAccountsView.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";

    private final Context mContext;
    private final IDBManager mDbManager = DBManager.getInstance();
    // array of indexes of accounts which are exposed to client
    private final List<Integer> mExposedAccountIndexes = new ArrayList<>();
    // stack of indexes of accounts which were marked to be deleted
    private final List<Integer> mDeletedAccountIndexes = new ArrayList<>();
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
        for (int index = 0; index < mAccounts.size(); index++) {
            mExposedAccountIndexes.add(index);
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
        deleteSelectedAccounts();
        // clear indexes
        mExposedAccountIndexes.clear();
        mAccounts.removeChangeListeners();
        mAccounts = null;
        mRealm.close();
        mInitialized = false;
    }

    @Override
    public int getNumAccounts() {
        checkInitialized();
        return mExposedAccountIndexes.size();
    }

    @Override
    public String getAccountName(int position) {
        checkInitialized();
        int index = mExposedAccountIndexes.get(position);
        return mAccounts.get(index).getName();
    }

    /**
     * Starts {@linkplain budny.moneykeeper.ui.activities.ActivityAccountEdit}
     * to update specified category.
     *
     * @param position position of category item from client's point of view
     */
    @Override
    public void updateAccount(int position) {
        int index = mExposedAccountIndexes.get(position);
        Intent intent = new Intent(mContext, ActivityAccountEdit.class);
        intent.putExtra(IntentExtras.FIELD_ACTION, IntentExtras.ACTION_UPDATE);
        intent.putExtra(IntentExtras.FIELD_INDEX_ACCOUNT, index);
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
        // remove account with specified position and add its index to the delete stack
        mDeletedAccountIndexes.add(mExposedAccountIndexes.remove(position));
        // notify clients about changes
        for (RealmChangeListener listener : mChangeListeners) {
            listener.onChange();
        }
        return true;
    }

    @Override
    public boolean unDeleteLastAccount(int position) {
        checkInitialized();
        int numDeletedAccounts = mDeletedAccountIndexes.size();
        if (numDeletedAccounts == 0) {
            // there are no accounts to delete
            return false;
        }
        // pop index of account from stack and add it to specified position
        mExposedAccountIndexes.add(
                position, mDeletedAccountIndexes.remove(numDeletedAccounts - 1));
        // notify clients about changes
        for (RealmChangeListener listener : mChangeListeners) {
            listener.onChange();
        }
        return true;
    }

    @Override
    public void swapAccounts(int fromIndex, int toIndex) {
        checkInitialized();
        int internalFromIndex = mExposedAccountIndexes.get(fromIndex);
        int internalToIndex = mExposedAccountIndexes.get(toIndex);
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
     * Performs actual deletion of selected categories.
     */
    private void deleteSelectedAccounts() {
        while (!mDeletedAccountIndexes.isEmpty()) {
            // pop first index
            int index = mDeletedAccountIndexes.remove(0);
            CommonOperations.delete(mRealm, mAccounts, index);
            updateDeletedAccountIndexes(index);
        }
    }

    /**
     * Adjusts indexes of categories to delete after delete one of them.
     *
     * @param deletedIndex index of deleted category
     */
    private void updateDeletedAccountIndexes(int deletedIndex) {
        for (int i = 0; i < mDeletedAccountIndexes.size(); i++) {
            // decrease indexes, which were higher than deleted one
            int curIndex = mDeletedAccountIndexes.get(i);
            if (curIndex > deletedIndex) {
                mDeletedAccountIndexes.set(i, curIndex - 1);
            }
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

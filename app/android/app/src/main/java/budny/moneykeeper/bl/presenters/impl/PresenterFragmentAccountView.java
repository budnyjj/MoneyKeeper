package budny.moneykeeper.bl.presenters.impl;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import budny.moneykeeper.bl.presenters.IPresenterFragmentAccountView;
import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.model.BalanceChange;
import budny.moneykeeper.db.operations.AccountOperations;
import budny.moneykeeper.db.operations.CommonOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.db.util.impl.DBManager;
import budny.moneykeeper.ui.activities.ActivityBalanceChangeEdit;
import budny.moneykeeper.ui.misc.IntentExtras;
import io.realm.Realm;
import io.realm.RealmChangeListener;

public class PresenterFragmentAccountView implements IPresenterFragmentAccountView {
    private static final String TAG = PresenterFragmentAccountView.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";

    private final Context mContext;
    private final int mAccountIndex;
    private final IDBManager mDbManager = DBManager.getInstance();
    // array of indexes of balance changes which are exposed to client
    private final List<Integer> mExposedBalanceChangeIndexes = new ArrayList<>();
    // stack of indexes of balance changes which were marked to be deleted
    private final List<Integer> mDeletedBalanceChangeIndexes = new ArrayList<>();
    // preserves data change listeners from being garbage collected
    // and saves them from being removed during fragment lifecycle
    private final List<RealmChangeListener> mChangeListeners = new ArrayList<>();

    private Realm mRealm;
    private Account mAccount;

    private volatile boolean mInitialized;

    public PresenterFragmentAccountView(Context context, int accountIndex) {
        mContext = context;
        mAccountIndex = accountIndex;
    }

    @Override
    public void onStart() {
        mRealm = mDbManager.getRealm();
        mAccount = AccountOperations.read(mRealm).get(mAccountIndex);
        // fill list of indexes of active categories
        for (int index = 0; index < mAccount.getBalanceChanges().size(); index++) {
            mExposedBalanceChangeIndexes.add(index);
        }
        // add pending listeners
        for (final RealmChangeListener listener : mChangeListeners) {
            mAccount.addChangeListener(listener);
        }
        mInitialized = true;
    }

    @Override
    public void onStop() {
        checkInitialized();
        deleteSelectedBalanceChanges();
        // clear indexes
        mDeletedBalanceChangeIndexes.clear();
        mExposedBalanceChangeIndexes.clear();
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
        checkInitialized();
        return mExposedBalanceChangeIndexes.size();
    }

    @Override
    public BalanceChange getBalanceChange(int position) {
        checkInitialized();
        int index = mExposedBalanceChangeIndexes.get(position);
        return mAccount.getBalanceChanges().get(index);
    }

    @Override
    public void updateBalanceChange(int position) {
        checkInitialized();
        int index = mExposedBalanceChangeIndexes.get(position);
        Intent intent = new Intent(mContext, ActivityBalanceChangeEdit.class);
        intent.putExtra(IntentExtras.FIELD_ACTION, IntentExtras.ACTION_UPDATE);
        intent.putExtra(IntentExtras.FIELD_INDEX_ACCOUNT, mAccountIndex);
        intent.putExtra(IntentExtras.FIELD_INDEX_BALANCE_CHANGE, index);
        mContext.startActivity(intent);
    }

    @Override
    public boolean deleteBalanceChange(int position) {
        checkInitialized();
        // remove balance change with specified position and add its index to the delete stack
        mDeletedBalanceChangeIndexes.add(mExposedBalanceChangeIndexes.remove(position));
        // notify clients about changes
        for (RealmChangeListener listener : mChangeListeners) {
            listener.onChange();
        }
        return true;
    }

    @Override
    public boolean unDeleteLastBalanceChange(int position) {
        checkInitialized();
        int numDeletedBalanceChanges = mDeletedBalanceChangeIndexes.size();
        if (numDeletedBalanceChanges == 0) {
            // there are no balance changes to delete
            return false;
        }
        // pop index of balance change from stack and add it to specified position
        mExposedBalanceChangeIndexes.add(
                position, mDeletedBalanceChangeIndexes.remove(numDeletedBalanceChanges - 1));
        // notify clients about changes
        for (RealmChangeListener listener : mChangeListeners) {
            listener.onChange();
        }
        return true;
    }

    @Override
    public String getCurrencyCode() {
        checkInitialized();
        return mAccount.getCurrencyCode();
    }

    /**
     * Returns sum of the exposed balance changes only.
     */
    @Override
    public long getTotalAmount() {
        checkInitialized();
        long totalAmount = 0;
        List<BalanceChange> balanceChanges = mAccount.getBalanceChanges();
        for (int index : mExposedBalanceChangeIndexes) {
            totalAmount += balanceChanges.get(index).getAmount();
        }
        return totalAmount;
    }

    /**
     * Performs actual deletion of selected categories.
     */
    private void deleteSelectedBalanceChanges() {
        while (!mDeletedBalanceChangeIndexes.isEmpty()) {
            // pop first index
            int index = mDeletedBalanceChangeIndexes.remove(0);
            BalanceChange change = mAccount.getBalanceChanges().get(index);
            AccountOperations.removeBalanceChange(mRealm, mAccount, change);
            CommonOperations.delete(mRealm, change);
            updateDeletedBalanceChangeIndexes(index);
        }
    }

    /**
     * Adjusts indexes of categories to delete after delete one of them.
     *
     * @param deletedIndex index of deleted category
     */
    private void updateDeletedBalanceChangeIndexes(int deletedIndex) {
        for (int i = 0; i < mDeletedBalanceChangeIndexes.size(); i++) {
            // decrease indexes, which were higher than deleted one
            int curIndex = mDeletedBalanceChangeIndexes.get(i);
            if (curIndex > deletedIndex) {
                mDeletedBalanceChangeIndexes.set(i, curIndex - 1);
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

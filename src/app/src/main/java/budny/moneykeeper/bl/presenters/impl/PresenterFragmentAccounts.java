package budny.moneykeeper.bl.presenters.impl;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import budny.moneykeeper.bl.presenters.IPresenterFragmentAccounts;
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

public class PresenterFragmentAccounts implements IPresenterFragmentAccounts {
    private static final String TAG = PresenterFragmentAccounts.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";

    private final IDBManager mDbManager = DBManager.getInstance();
    // preserves data change listeners from being garbage collected
    // and saves them from being removed during fragment lifecycle
    private final List<RealmChangeListener> mChangeListeners = new ArrayList<>();

    private Realm mRealm;
    private RealmResults<Account> mAccounts;

    private volatile boolean mInitialized;

    @Override
    public void onStart() {
        mRealm = mDbManager.getRealm();
        mAccounts = AccountOperations.getAccounts(mRealm);
        // add pending listeners
        for (final RealmChangeListener listener : mChangeListeners) {
            mAccounts.addChangeListener(listener);
        }
        mInitialized = true;
    }

    @Override
    public void onStop() {
        checkInitialized();
        mAccounts.removeChangeListeners();
        mAccounts = null;
        mRealm.close();
        mInitialized = false;
    }

    @Override
    public int getNumAccounts() {
        checkInitialized();
        return mAccounts.size();
    }

    @Override
    public Account getAccount(int position) {
        checkInitialized();
        return mAccounts.get(position);
    }

    /**
     * Starts {@linkplain budny.moneykeeper.ui.activities.ActivityAccountEdit}
     * to update specified category.
     *
     * @param context activity context
     * @param index   index of category to edit
     */
    @Override
    public void updateAccount(Context context, int index) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ActivityAccountEdit.class);
        intent.putExtra(IntentExtras.FIELD_ACTION, IntentExtras.ACTION_UPDATE);
        intent.putExtra(IntentExtras.FIELD_INDEX, index);
        context.startActivity(intent);
    }

    @Override
    public void deleteAccount(int position) {
        checkInitialized();
        CommonOperations.deleteObject(mRealm, mAccounts, position);
    }

    @Override
    public void swapAccounts(int fromPosition, int toPosition) {
        checkInitialized();
        Account fromAccount = mAccounts.get(fromPosition);
        Account toAccount = mAccounts.get(toPosition);
        AccountOperations.swapAccounts(mRealm, fromAccount, toAccount);
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

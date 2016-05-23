package budny.moneykeeper.bl.presenters.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import budny.moneykeeper.bl.presenters.IPresenterActivityBalanceChangesView;
import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.operations.AccountOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.db.util.impl.DBManager;
import budny.moneykeeper.ui.activities.ActivityBalanceChangeEdit;
import budny.moneykeeper.ui.fragments.impl.FragmentAccountView;
import budny.moneykeeper.ui.misc.IntentExtras;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class PresenterActivityBalanceChangesView implements IPresenterActivityBalanceChangesView {
    private static final String TAG = PresenterActivityBalanceChangesView.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";

    private final Context mContext;
    private final IDBManager mDbManager = DBManager.getInstance();
    // preserves data change listeners from being garbage collected
    // and saves them from being removed during activity lifecycle
    private final List<RealmChangeListener> mChangeListeners = new ArrayList<>();

    private Realm mRealm;
    private RealmResults<Account> mAccounts;

    private volatile boolean mInitialized;

    public PresenterActivityBalanceChangesView(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        mRealm = mDbManager.getRealm();
        mAccounts = AccountOperations.read(mRealm);
        // add pending listeners
        for (final RealmChangeListener listener : mChangeListeners) {
            mAccounts.addChangeListener(listener);
        }
        mInitialized = true;
    }

    @Override
    public void onDestroy() {
        checkInitialized();
        mAccounts.removeChangeListeners();
        mAccounts = null;
        mRealm.close();
        mInitialized = false;
    }

    /**
     * Returns an {@linkplain FragmentAccountView}, initialized with specified account.
     *
     * @param index index of account to initialize with
     * @return initialized fragment
     */
    @Override
    public Fragment getFragmentAccountView(int index) {
        checkInitialized();
        // create argument
        Bundle args = new Bundle();
        args.putInt(IntentExtras.FIELD_INDEX_ACCOUNT, index);
        // create fragment and pass arguments into it
        Fragment fragment = new FragmentAccountView();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public CharSequence getFragmentAccountViewName(int position) {
        checkInitialized();
        return mAccounts.get(position).getName();
    }

    @Override
    public int getNumAccounts() {
        checkInitialized();
        return mAccounts.size();
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

    @Override
    public void createBalanceChange(int accountIndex) {
        Intent intent = new Intent(mContext, ActivityBalanceChangeEdit.class);
        intent.putExtra(IntentExtras.FIELD_INDEX_ACCOUNT, accountIndex);
        intent.putExtra(IntentExtras.FIELD_ACTION, IntentExtras.ACTION_CREATE);
        mContext.startActivity(intent);
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

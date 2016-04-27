package budny.moneykeeper.bl.presenters.impl;

import budny.moneykeeper.bl.presenters.IPresenterAccounts;
import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.operations.AccountOperations;
import budny.moneykeeper.db.operations.CommonOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.impl.DBManager;
import io.realm.Realm;
import io.realm.RealmResults;

public class PresenterAccounts implements IPresenterAccounts {
    private static final String TAG = PresenterAccounts.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";

    private final IDBManager mDbManager = DBManager.getInstance();

    private Realm mRealm;
    private RealmResults<Account> mAccounts;

    private volatile boolean mInitialized;

    @Override
    public void onStart() {
        mRealm = mDbManager.getRealm();
        mAccounts = AccountOperations.getAccounts(mRealm);
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
        CommonOperations.remove(mRealm, mAccounts.get(position));
    }

    @Override
    public void swapAccounts(int fromPosition, int toPosition) {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        AccountOperations.swapAccounts(mRealm, mAccounts.get(fromPosition), mAccounts.get(toPosition));
    }
}

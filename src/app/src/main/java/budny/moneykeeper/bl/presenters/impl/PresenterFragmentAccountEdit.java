package budny.moneykeeper.bl.presenters.impl;

import budny.moneykeeper.bl.presenters.IPresenterFragmentAccountEdit;
import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.operations.AccountOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.impl.DBManager;
import io.realm.Realm;
import io.realm.RealmResults;

public class PresenterFragmentAccountEdit implements IPresenterFragmentAccountEdit {
    private static final String TAG = PresenterFragmentAccountEdit.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";

    private final IDBManager mDbManager = DBManager.getInstance();

    private Realm mRealm;
    private RealmResults<Account> mAccounts;

    private volatile boolean mInitialized;

    @Override
    public void onStart() {
        mRealm = mDbManager.getRealm();
        mAccounts = AccountOperations.read(mRealm);
        mInitialized = true;
    }

    @Override
    public void onStop() {
        checkInitialized();
        mAccounts = null;
        mRealm.close();
        mInitialized = false;
    }

    @Override
    public Account getAccount(int index) {
        checkInitialized();
        return mAccounts.get(index);
    }

    @Override
    public void createAccount(Account account) {
        AccountOperations.create(mRealm, account);
    }

    /**
     * Updates specified account by replacing its contents.
     *
     * @param dstIndex   index of account to be replaced
     * @param srcAccount new account
     */
    @Override
    public void updateAccount(Account srcAccount, int dstIndex) {
        checkInitialized();
        AccountOperations.update(mRealm, srcAccount, mAccounts.get(dstIndex));
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

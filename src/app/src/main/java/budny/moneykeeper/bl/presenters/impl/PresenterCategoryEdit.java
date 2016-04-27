package budny.moneykeeper.bl.presenters.impl;

import budny.moneykeeper.bl.presenters.IPresenterCategoryEdit;
import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.db.operations.AccountOperations;
import budny.moneykeeper.db.operations.CategoryOperations;
import budny.moneykeeper.db.operations.CommonOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.impl.DBManager;
import io.realm.Realm;

public class PresenterCategoryEdit implements IPresenterCategoryEdit {
    private static final String TAG = PresenterCategoryEdit.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";

    private final IDBManager mDbManager = DBManager.getInstance();

    private Realm mRealm;

    private volatile boolean mInitialized;

    @Override
    public void onStart() {
        mRealm = mDbManager.getRealm();
        mInitialized = true;
    }

    @Override
    public void onStop() {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        mRealm.close();
        mInitialized = false;
    }

    @Override
    public void addCategory(Category category) {
        CommonOperations.add(mRealm, category);
    }
}

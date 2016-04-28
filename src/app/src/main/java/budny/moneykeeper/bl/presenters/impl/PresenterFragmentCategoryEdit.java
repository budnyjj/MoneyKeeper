package budny.moneykeeper.bl.presenters.impl;

import budny.moneykeeper.bl.presenters.IPresenterFragmentCategoryEdit;
import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.db.operations.CommonOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.impl.DBManager;
import io.realm.Realm;

public class PresenterFragmentCategoryEdit implements IPresenterFragmentCategoryEdit {
    private static final String TAG = PresenterFragmentCategoryEdit.class.getSimpleName();
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
        CommonOperations.addObject(mRealm, category);
    }
}

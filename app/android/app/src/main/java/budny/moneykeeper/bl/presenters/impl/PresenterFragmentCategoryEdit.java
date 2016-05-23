package budny.moneykeeper.bl.presenters.impl;

import budny.moneykeeper.bl.presenters.IPresenterFragmentCategoryEdit;
import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.db.operations.CategoryOperations;
import budny.moneykeeper.db.operations.CommonOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.impl.DBManager;
import budny.moneykeeper.ui.misc.IntentExtras;
import io.realm.Realm;
import io.realm.RealmResults;

public class PresenterFragmentCategoryEdit implements IPresenterFragmentCategoryEdit {
    private static final String TAG = PresenterFragmentCategoryEdit.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";
    private static final String MSG_INVALID_ACTION = "This method should not be invoked with this action: ";

    private final String mAction;
    private final int mCategoryIndex;
    private final IDBManager mDbManager = DBManager.getInstance();

    private Realm mRealm;
    private Category mCategory;

    private volatile boolean mInitialized;

    public PresenterFragmentCategoryEdit(String action, int categoryIndex) {
        mAction = action;
        mCategoryIndex = categoryIndex;
    }

    @Override
    public void onStart() {
        mRealm = mDbManager.getRealm();
        if (IntentExtras.ACTION_UPDATE.equals(mAction)) {
            mCategory = CategoryOperations.read(mRealm).get(mCategoryIndex);
        }
        mInitialized = true;
    }

    @Override
    public void onStop() {
        checkInitialized();
        mCategory = null;
        mRealm.close();
        mInitialized = false;
    }

    @Override
    public String getCategoryName() {
        checkInitialized();
        if (!IntentExtras.ACTION_UPDATE.equals(mAction)) {
            throw new IllegalStateException(MSG_INVALID_ACTION + mAction);
        }
        return mCategory.getName();
    }

    @Override
    public Category.Type getCategoryType() {
        checkInitialized();
        if (!IntentExtras.ACTION_UPDATE.equals(mAction)) {
            throw new IllegalStateException(MSG_INVALID_ACTION + mAction);
        }
        return mCategory.getType();
    }

    @Override
    public void createCategory(String name, Category.Type type) {
        checkInitialized();
        if (!IntentExtras.ACTION_CREATE.equals(mAction)) {
            throw new IllegalStateException(MSG_INVALID_ACTION + mAction);
        }
        CategoryOperations.create(mRealm, name, type);
    }

    @Override
    public void updateCategory(String name, Category.Type type) {
        checkInitialized();
        if (!IntentExtras.ACTION_UPDATE.equals(mAction)) {
            throw new IllegalStateException(MSG_INVALID_ACTION + mAction);
        }
        CategoryOperations.update(mRealm, mCategory, name, type);
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

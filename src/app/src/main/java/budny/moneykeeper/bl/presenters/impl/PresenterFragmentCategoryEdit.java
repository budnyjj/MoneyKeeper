package budny.moneykeeper.bl.presenters.impl;

import budny.moneykeeper.bl.presenters.IPresenterFragmentCategoryEdit;
import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.db.operations.CategoryOperations;
import budny.moneykeeper.db.operations.CommonOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.impl.DBManager;
import io.realm.Realm;
import io.realm.RealmResults;

public class PresenterFragmentCategoryEdit implements IPresenterFragmentCategoryEdit {
    private static final String TAG = PresenterFragmentCategoryEdit.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";

    private final IDBManager mDbManager = DBManager.getInstance();

    private Realm mRealm;
    private RealmResults<Category> mCategories;

    private volatile boolean mInitialized;

    @Override
    public void onStart() {
        mRealm = mDbManager.getRealm();
        mCategories = CategoryOperations.getCategories(mRealm);
        mInitialized = true;
    }

    @Override
    public void onStop() {
        checkInitialized();
        mCategories = null;
        mRealm.close();
        mInitialized = false;
    }

    @Override
    public Category getCategory(int index) {
        checkInitialized();
        return mCategories.get(index);
    }

    @Override
    public void createCategory(Category category) {
        checkInitialized();
        CommonOperations.createObject(mRealm, category);
    }

    /**
     * Updates specified category by replacing its contents.
     *
     * @param dstIndex index of category to be replaced
     * @param srcCategory new category
     */
    @Override
    public void updateCategory(Category srcCategory, int dstIndex) {
        checkInitialized();
        CategoryOperations.updateCategory(mRealm, srcCategory, mCategories.get(dstIndex));
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

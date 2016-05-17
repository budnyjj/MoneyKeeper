package budny.moneykeeper.bl.presenters.impl;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import budny.moneykeeper.bl.presenters.IPresenterFragmentCategoriesView;
import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.db.operations.CategoryOperations;
import budny.moneykeeper.db.operations.CommonOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.db.util.impl.DBManager;
import budny.moneykeeper.ui.activities.ActivityCategoryEdit;
import budny.moneykeeper.ui.misc.IntentExtras;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * The primary implementation of {@linkplain IPresenterFragmentCategoriesView}.
 *
 * It supports the actual list of categories, queried from Realm,
 * the list of category indexes, which are exposed to the client and
 * the stack of category indexes which are marked to be deleted.
 */
public class PresenterFragmentCategoriesView implements IPresenterFragmentCategoriesView {
    private static final String TAG = PresenterFragmentCategoriesView.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";

    private final IDBManager mDbManager = DBManager.getInstance();
    // array of indexes of categories which are exposed to client
    private final List<Integer> mExposedCategoryIndexes = new ArrayList<>();
    // stack of indexes of categories which were marked to be deleted
    private final List<Integer> mDeletedCategoryIndexes = new ArrayList<>();
    // preserves data change listeners from being garbage collected
    // and saves them from being removed during fragment lifecycle
    private final List<RealmChangeListener> mChangeListeners = new ArrayList<>();

    private Realm mRealm;
    private RealmResults<Category> mCategories;

    private volatile boolean mInitialized;

    @Override
    public void onStart() {
        mRealm = mDbManager.getRealm();
        mCategories = CategoryOperations.read(mRealm);
        // fill list of indexes of active categories
        for (int index = 0; index < mCategories.size(); index++) {
            mExposedCategoryIndexes.add(index);
        }
        // add pending listeners
        for (final RealmChangeListener listener : mChangeListeners) {
            mCategories.addChangeListener(listener);
        }
        mInitialized = true;
    }

    @Override
    public void onStop() {
        checkInitialized();
        deleteSelectedCategories();
        // clear indexes
        mExposedCategoryIndexes.clear();
        mCategories.removeChangeListeners();
        mCategories = null;
        mRealm.close();
        mInitialized = false;
    }

    @Override
    public int getNumCategories() {
        checkInitialized();
        return mExposedCategoryIndexes.size();
    }

    @Override
    public String getCategoryName(int position) {
        checkInitialized();
        int index = mExposedCategoryIndexes.get(position);
        return mCategories.get(index).getName();
    }

    /**
     * Starts {@linkplain budny.moneykeeper.ui.activities.ActivityCategoryEdit}
     * to update specified category.
     *
     * @param context activity context
     * @param position   index of category to edit
     * @throws NullPointerException if context is null
     */
    @Override
    public void updateCategory(Context context, int position) {
        if (context == null) {
            throw new NullPointerException("Context should not be null");
        }
        int index = mExposedCategoryIndexes.get(position);
        Intent intent = new Intent(context, ActivityCategoryEdit.class);
        intent.putExtra(IntentExtras.FIELD_ACTION, IntentExtras.ACTION_UPDATE);
        intent.putExtra(IntentExtras.FIELD_INDEX_CATEGORY, index);
        context.startActivity(intent);
    }

    @Override
    public boolean deleteCategory(int position) {
        checkInitialized();
        // remove category with specified position and add its index to the delete stack
        mDeletedCategoryIndexes.add(mExposedCategoryIndexes.remove(position));
        // notify clients about changes
        for (RealmChangeListener listener : mChangeListeners) {
            listener.onChange();
        }
        return true;
    }

    @Override
    public boolean unDeleteLastCategory(int position) {
        checkInitialized();
        int numDeletedCategories = mDeletedCategoryIndexes.size();
        if (numDeletedCategories == 0) {
            // there are no categories to delete
            return false;
        }
        // pop index of category from stack and add it to specified position
        mExposedCategoryIndexes.add(
                position, mDeletedCategoryIndexes.remove(numDeletedCategories - 1));
        // notify clients about changes
        for (RealmChangeListener listener : mChangeListeners) {
            listener.onChange();
        }
        return true;
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
            mCategories.addChangeListener(realmListener);
        }
    }

    /**
     * Performs actual deletion of selected categories.
     */
    private void deleteSelectedCategories() {
        while (!mDeletedCategoryIndexes.isEmpty()) {
            // pop first index
            int index = mDeletedCategoryIndexes.remove(0);
            CommonOperations.delete(mRealm, mCategories, index);
            updateDeletedCategoryIndexes(index);
        }
    }

    /**
     * Adjusts indexes of categories to delete after delete one of them.
     *
     * @param deletedIndex index of deleted category
     */
    private void updateDeletedCategoryIndexes(int deletedIndex) {
        for (int i = 0; i < mDeletedCategoryIndexes.size(); i++) {
            // decrease indexes, which were higher than deleted one
            int curIndex = mDeletedCategoryIndexes.get(i);
            if (curIndex > deletedIndex) {
                mDeletedCategoryIndexes.set(i, curIndex - 1);
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

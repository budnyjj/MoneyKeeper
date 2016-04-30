package budny.moneykeeper.bl.presenters.impl;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import budny.moneykeeper.bl.presenters.IPresenterFragmentCategories;
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

public class PresenterFragmentCategories implements IPresenterFragmentCategories {
    private static final String TAG = PresenterFragmentCategories.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";

    private final IDBManager mDbManager = DBManager.getInstance();
    // preserves data change listeners from being garbage collected
    // and saves them from being removed during fragment lifecycle
    private final List<RealmChangeListener> mChangeListeners = new ArrayList<>();

    private Realm mRealm;
    private RealmResults<Category> mCategories;

    private volatile boolean mInitialized;

    @Override
    public void onStart() {
        mRealm = mDbManager.getRealm();
        mCategories = CategoryOperations.getCategories(mRealm);
        // add pending listeners
        for (final RealmChangeListener listener : mChangeListeners) {
            mCategories.addChangeListener(listener);
        }
        mInitialized = true;
    }

    @Override
    public void onStop() {
        checkInitialized();
        mCategories.removeChangeListeners();
        mCategories = null;
        mRealm.close();
        mInitialized = false;
    }

    @Override
    public int getNumCategories() {
        checkInitialized();
        return mCategories.size();
    }

    @Override
    public Category getCategory(int position) {
        checkInitialized();
        return mCategories.get(position);
    }

    /**
     * Starts {@linkplain budny.moneykeeper.ui.activities.ActivityCategoryEdit}
     * to update specified category.
     *
     * @param context activity context
     * @param index   index of category to edit
     */
    @Override
    public void updateCategory(Context context, int index) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ActivityCategoryEdit.class);
        intent.putExtra(IntentExtras.FIELD_ACTION, IntentExtras.ACTION_UPDATE);
        intent.putExtra(IntentExtras.FIELD_INDEX, index);
        context.startActivity(intent);
    }

    @Override
    public void deleteCategory(int position) {
        checkInitialized();
        CommonOperations.deleteObject(mRealm, mCategories, position);
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
     * Checks, if presenter is in active state.
     */
    private void checkInitialized() {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
    }
}

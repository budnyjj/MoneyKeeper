package budny.moneykeeper.bl.presenters.impl;

import java.util.ArrayList;
import java.util.List;

import budny.moneykeeper.bl.presenters.IPresenterFragmentCategories;
import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.db.operations.CategoryOperations;
import budny.moneykeeper.db.operations.CommonOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.impl.DBManager;
import budny.moneykeeper.db.util.IDataChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class PresenterFragmentCategories implements IPresenterFragmentCategories {
    private static final String TAG = PresenterFragmentCategories.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";

    private final IDBManager mDbManager = DBManager.getInstance();
    private final List<IDataChangeListener> mDataChangeListeners = new ArrayList<>();

    private Realm mRealm;
    private RealmResults<Category> mCategories;

    private volatile boolean mInitialized;

    @Override
    public void onStart() {
        mRealm = mDbManager.getRealm();
        mCategories = CategoryOperations.getCategories(mRealm);
        for (final IDataChangeListener listener : mDataChangeListeners) {
            CommonOperations.addDataChangeListener(mRealm, listener);
        }
        mInitialized = true;
    }

    @Override
    public void onStop() {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        mCategories = null;
        mDataChangeListeners.clear();
        mRealm.close();
        mInitialized = false;
    }

    @Override
    public int getNumCategories() {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        return mCategories.size();
    }

    @Override
    public Category getCategory(int position) {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        return mCategories.get(position);
    }

    @Override
    public void removeCategory(int position) {
        if (!mInitialized) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        CommonOperations.deleteObject(mRealm, mCategories.get(position));
    }

    @Override
    public void addDataChangeListener(final IDataChangeListener listener) {
        // if already initialized, addObject change listener immediately
        if (mInitialized) {
            CommonOperations.addDataChangeListener(mRealm, listener);
        }
        // store listener in temporary list to be added during initialization
        mDataChangeListeners.add(listener);
    }
}

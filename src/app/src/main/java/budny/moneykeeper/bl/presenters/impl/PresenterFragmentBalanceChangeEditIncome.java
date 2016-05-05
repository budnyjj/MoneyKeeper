package budny.moneykeeper.bl.presenters.impl;

import java.util.HashSet;
import java.util.Set;

import budny.moneykeeper.bl.presenters.IPresenterFragmentBalanceChangeEditIncome;
import budny.moneykeeper.db.model.BalanceChange;
import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.db.operations.AccountOperations;
import budny.moneykeeper.db.operations.CategoryOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.impl.DBManager;
import budny.moneykeeper.ui.misc.IntentExtras;
import io.realm.Realm;
import io.realm.RealmResults;

public class PresenterFragmentBalanceChangeEditIncome
        implements IPresenterFragmentBalanceChangeEditIncome {
    private static final String TAG = PresenterFragmentCategoriesView.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";
    private static final String MSG_INVALID_ACTION = "This method should not be invoked with this action: ";

    private final String mAction;
    private final int mAccountIndex;
    private final int mBalanceChangeIndex;
    private final IDBManager mDbManager = DBManager.getInstance();
    private final Set<Integer> mSelectedCategoryIndexes = new HashSet<>();

    private Realm mRealm;
    private BalanceChange mBalanceChange;
    private RealmResults<Category> mAllCategories;

    private volatile boolean mInitialized;

    public PresenterFragmentBalanceChangeEditIncome(
            String action, int accountIndex, int balanceChangeIndex) {
        mAction = action;
        mAccountIndex = accountIndex;
        mBalanceChangeIndex = balanceChangeIndex;
    }

    @Override
    public void onStart() {
        mRealm = mDbManager.getRealm();
        mAllCategories = CategoryOperations.read(mRealm);
        if (IntentExtras.ACTION_UPDATE.equals(mAction)) {
            // read specified account and pick specified balance change from it
            mBalanceChange = AccountOperations
                    .read(mRealm)
                    .get(mAccountIndex)
                    .getBalanceChanges()
                    .get(mBalanceChangeIndex);
            mSelectedCategoryIndexes.add(mAllCategories.indexOf(mBalanceChange.getCategory()));
        }
        mInitialized = true;
    }

    @Override
    public void onStop() {
        checkInitialized();
        mAllCategories = null;
        mRealm.close();
        mInitialized = false;
    }

    @Override
    public long getAmount() {
        checkInitialized();
        if (!IntentExtras.ACTION_UPDATE.equals(mAction)) {
            throw new IllegalStateException(MSG_INVALID_ACTION + mAction);
        }
        return mBalanceChange.getAmount();
    }

    @Override
    public int getNumCategories() {
        checkInitialized();
        return mAllCategories.size();
    }

    @Override
    public String getCategoryName(int index) {
        checkInitialized();
        return mAllCategories.get(index).getName();
    }

    public boolean isSelectedCategory(int index) {
        return mSelectedCategoryIndexes.contains(index);
    }

    public void selectCategory(int index) {
        mSelectedCategoryIndexes.add(index);
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

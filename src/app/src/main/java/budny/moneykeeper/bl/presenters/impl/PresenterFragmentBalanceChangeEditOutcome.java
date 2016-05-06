package budny.moneykeeper.bl.presenters.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import budny.moneykeeper.bl.presenters.IPresenterFragmentBalanceChangeEditOutcome;
import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.model.BalanceChange;
import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.db.operations.AccountOperations;
import budny.moneykeeper.db.operations.BalanceChangeOperations;
import budny.moneykeeper.db.operations.CategoryOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.db.util.impl.DBManager;
import budny.moneykeeper.ui.misc.IntentExtras;
import io.realm.Realm;
import io.realm.RealmResults;

public class PresenterFragmentBalanceChangeEditOutcome
        implements IPresenterFragmentBalanceChangeEditOutcome {
    private static final String TAG = PresenterFragmentCategoriesView.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";
    private static final String MSG_INVALID_ACTION = "This method should not be invoked with this action: ";

    private final String mAction;
    private final int mAccountIndex;
    private final int mBalanceChangeIndex;
    private final IDBManager mDbManager = DBManager.getInstance();
    private final Set<Integer> mSelectedCategoryIndexes = new HashSet<>();
    private final Set<IDataChangeListener> mCategorySelectedListeners = new HashSet<>();

    private Realm mRealm;
    private Account mAccount;
    private BalanceChange mBalanceChange;
    private RealmResults<Category> mAllCategories;

    private volatile boolean mInitialized;

    public PresenterFragmentBalanceChangeEditOutcome(
            String action, int accountIndex, int balanceChangeIndex) {
        mAction = action;
        mAccountIndex = accountIndex;
        mBalanceChangeIndex = balanceChangeIndex;
    }

    @Override
    public void onStart() {
        mRealm = mDbManager.getRealm();
        mAccount = AccountOperations.read(mRealm).get(mAccountIndex);
        mAllCategories = CategoryOperations.readOutcome(mRealm);
        if (IntentExtras.ACTION_UPDATE.equals(mAction)) {
            // read specified account and pick specified balance change from it
            mBalanceChange = mAccount.getBalanceChanges().get(mBalanceChangeIndex);
            // initialize indexes of selected categories with outcome type only
            for (Category category : mBalanceChange.getCategories()) {
                if (Category.Type.OUTCOME.equals(category.getType())) {
                    mSelectedCategoryIndexes.add(mAllCategories.indexOf(category));
                }
            }
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
    public void createBalanceChange(long amount, Set<Category> categories) {
        checkInitialized();
        // TODO: use date from user input
        // create balance change
        BalanceChange change = BalanceChangeOperations.create(mRealm, amount, new Date());
        // add categories to balance change
        for (Category category : categories) {
            BalanceChangeOperations.addCategory(mRealm, change, category);
        }
        // add balance change to the account
        AccountOperations.addBalanceChange(mRealm, mAccount, change);
    }

    @Override
    public void updateBalanceChange(long amount, Set<Category> categories) {
        checkInitialized();
        // TODO: use date from user input
        BalanceChangeOperations.update(mRealm, mBalanceChange, amount, new Date());
        BalanceChangeOperations.clearCategories(mRealm, mBalanceChange);
        for (Category category : categories) {
            BalanceChangeOperations.addCategory(mRealm, mBalanceChange, category);
        }
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

    @Override
    public boolean isSelectedCategory(int index) {
        checkInitialized();
        return mSelectedCategoryIndexes.contains(index);
    }

    @Override
    public void toggleCategory(int index) {
        checkInitialized();
        if (isSelectedCategory(index)) {
            mSelectedCategoryIndexes.remove(index);
        } else {
            mSelectedCategoryIndexes.add(index);
        }
        // notify about changes of selected categories set
        for (IDataChangeListener listener : mCategorySelectedListeners) {
            listener.onChange();
        }
    }

    @Override
    public Set<Category> getSelectedCategories() {
        checkInitialized();
        Set<Category> selectedCategories = new HashSet<>();
        for (int categoryIndex : mSelectedCategoryIndexes) {
            selectedCategories.add(mAllCategories.get(categoryIndex));
        }
        return selectedCategories;
    }

    @Override
    public void addCategorySelectedListener(IDataChangeListener listener) {
        mCategorySelectedListeners.add(listener);
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

package budny.moneykeeper.bl.presenters.impl;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import budny.moneykeeper.bl.presenters.IPresenterFragmentAccountEdit;
import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.operations.AccountOperations;
import budny.moneykeeper.db.util.IDBManager;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.db.util.impl.DBManager;
import budny.moneykeeper.ui.misc.IntentExtras;
import io.realm.Realm;

public class PresenterFragmentAccountEdit implements IPresenterFragmentAccountEdit {
    private static final String TAG = PresenterFragmentAccountEdit.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized";
    private static final String MSG_INVALID_ACTION = "This method should not be invoked with this action: ";

    private final String mAction;
    private final int mAccountIndex;
    private final IDBManager mDbManager = DBManager.getInstance();
    private final List<Currency> mCurrencies;
    private final Set<IDataChangeListener> mCurrencySelectedListeners = new HashSet<>();

    private Realm mRealm;
    private Account mAccount;
    private int mSelectedCurrencyIndex;

    private volatile boolean mInitialized;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public PresenterFragmentAccountEdit(String action, int accountIndex) {
        mAction = action;
        mAccountIndex = accountIndex;
        mCurrencies = new ArrayList<>(Currency.getAvailableCurrencies());
    }

    @Override
    public void onStart() {
        mRealm = mDbManager.getRealm();
        if (IntentExtras.ACTION_UPDATE.equals(mAction)) {
            // read specified account and pick currency from it
            mAccount = AccountOperations.read(mRealm).get(mAccountIndex);
            mSelectedCurrencyIndex = mCurrencies.indexOf(Currency.getInstance(mAccount.getCurrencyCode()));
        } else {
            // set currency index based on default locale
            mSelectedCurrencyIndex = mCurrencies.indexOf(Currency.getInstance(Locale.getDefault()));
        }
        mInitialized = true;
    }

    @Override
    public void onStop() {
        checkInitialized();
        mAccount = null;
        mRealm.close();
        mInitialized = false;
    }

    @Override
    public String getAccountName() {
        checkInitialized();
        if (!IntentExtras.ACTION_UPDATE.equals(mAction)) {
            throw new IllegalStateException(MSG_INVALID_ACTION + mAction);
        }
        return mAccount.getName();
    }

    @Override
    public void createAccount(String name, String currencyCode) {
        checkInitialized();
        if (!IntentExtras.ACTION_CREATE.equals(mAction)) {
            throw new IllegalStateException(MSG_INVALID_ACTION + mAction);
        }
        AccountOperations.create(mRealm, name, currencyCode);
    }

    /**
     * Updates specified account by replacing its contents.
     */
    @Override
    public void updateAccount(String name, String currencyCode) {
        checkInitialized();
        if (!IntentExtras.ACTION_UPDATE.equals(mAction)) {
            throw new IllegalStateException(MSG_INVALID_ACTION + mAction);
        }
        AccountOperations.update(mRealm, mAccount, name, currencyCode);
    }

    @Override
    public int getNumCurrencies() {
        return mCurrencies.size();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public String getCurrencyName(int index) {
        return mCurrencies.get(index).getDisplayName();
    }

    @Override
    public void selectCurrency(int index) {
        mSelectedCurrencyIndex = index;
        for (IDataChangeListener listener : mCurrencySelectedListeners) {
            listener.onChange();
        }
    }

    @Override
    public boolean isSelectedCurrency(int index) {
        return mSelectedCurrencyIndex == index;
    }

    @Override
    public String getSelectedCurrencyCode() {
        return mCurrencies.get(mSelectedCurrencyIndex).getCurrencyCode();
    }

    @Override
    public void addCurrencySelectedListener(IDataChangeListener listener) {
        mCurrencySelectedListeners.add(listener);
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

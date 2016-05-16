package budny.moneykeeper.bl.presenters.impl;

import android.content.Context;
import android.os.Bundle;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterActivityBalanceChangeEdit;
import budny.moneykeeper.db.model.BalanceChange;
import budny.moneykeeper.db.operations.AccountOperations;
import budny.moneykeeper.db.util.impl.DBManager;
import budny.moneykeeper.ui.fragments.IFragmentEdit;
import budny.moneykeeper.ui.fragments.impl.FragmentBalanceChangeEditIncome;
import budny.moneykeeper.ui.fragments.impl.FragmentBalanceChangeEditOutcome;
import budny.moneykeeper.ui.misc.IntentExtras;
import io.realm.Realm;

public class PresenterActivityBalanceChangeEdit implements IPresenterActivityBalanceChangeEdit {
    private static final String TAG = PresenterActivityBalanceChangeEdit.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized with arguments bundle";
    private static final String MSG_NO_ARGS = "Unable to locate following arguments: ";
    private static final String MSG_UNKNOWN_TYPE = "Unknown input type: ";
    private static final int NUM_INPUT_TYPES = 2;
    private static final int INPUT_TYPE_OUTCOME = 0;
    private static final int INPUT_TYPE_INCOME = 1;

    private final Context mContext;
    private final Bundle mArgs;
    // action to perform with balance change (create or update)
    private final String mAction;
    // index of parent account
    private final int mAccountIndex;
    // index of balance change to edit
    private final int mBalanceChangeIndex;

    public PresenterActivityBalanceChangeEdit(Context context, Bundle args) {
        mContext = context;
        mArgs = args;
        // parse arguments
        if (mArgs == null) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        mAccountIndex = mArgs.getInt(IntentExtras.FIELD_INDEX_ACCOUNT, IntentExtras.INDEX_INVALID);
        if (mAccountIndex == IntentExtras.INDEX_INVALID) {
            throw new IllegalArgumentException(
                    MSG_NO_ARGS + IntentExtras.FIELD_INDEX_ACCOUNT);
        }
        String action = args.getString(IntentExtras.FIELD_ACTION);
        mAction = (action == null) ? IntentExtras.ACTION_INVALID : action;
        if (IntentExtras.ACTION_INVALID.equals(mAction)) {
            throw new IllegalArgumentException(
                    MSG_NO_ARGS + IntentExtras.FIELD_ACTION);
        }
        mBalanceChangeIndex =
                args.getInt(IntentExtras.FIELD_INDEX_BALANCE_CHANGE, IntentExtras.INDEX_INVALID);
        if (IntentExtras.ACTION_UPDATE.equals(mAction)) {
            if (mBalanceChangeIndex == IntentExtras.INDEX_INVALID) {
                throw new IllegalArgumentException(
                        MSG_NO_ARGS + IntentExtras.FIELD_INDEX_BALANCE_CHANGE);
            }
        }
    }

    public IFragmentEdit getFragmentEdit(int type) {
        switch (type) {
            case INPUT_TYPE_INCOME: {
                IFragmentEdit fragment = new FragmentBalanceChangeEditIncome();
                fragment.setArguments(mArgs);
                return fragment;
            }
            case INPUT_TYPE_OUTCOME: {
                IFragmentEdit fragment = new FragmentBalanceChangeEditOutcome();
                fragment.setArguments(mArgs);
                return fragment;
            }
            default:
                throw new IllegalArgumentException(MSG_UNKNOWN_TYPE + type);
        }
    }

    public String getFragmentEditName(int type) {
        switch (type) {
            case INPUT_TYPE_INCOME:
                return mContext.getString(R.string.title_income);
            case INPUT_TYPE_OUTCOME:
                return mContext.getString(R.string.title_outcome);
            default:
                throw new IllegalArgumentException(MSG_UNKNOWN_TYPE + type);
        }
    }

    public int getNumEditTypes() {
        return NUM_INPUT_TYPES;
    }

    @Override
    public int getSuggestedEditType() {
        int suggestedType = INPUT_TYPE_OUTCOME;
        if (IntentExtras.ACTION_UPDATE.equals(mAction)) {
            // we are modifying the existing balance change,
            // so try to suggest correct edit type based on the amount of the balance change
            DBManager manager = DBManager.getInstance();
            Realm realm = manager.getRealm();
            long amount = AccountOperations
                    .read(realm).get(mAccountIndex)
                    .getBalanceChanges().get(mBalanceChangeIndex)
                    .getAmount();
            realm.close();
            suggestedType = (amount >= 0) ? INPUT_TYPE_INCOME : INPUT_TYPE_OUTCOME;
        }
        return suggestedType;
    }
}

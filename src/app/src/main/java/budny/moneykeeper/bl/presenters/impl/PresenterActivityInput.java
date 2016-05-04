package budny.moneykeeper.bl.presenters.impl;

import android.content.Context;
import android.os.Bundle;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterActivityInput;
import budny.moneykeeper.ui.fragments.IFragmentEdit;
import budny.moneykeeper.ui.fragments.impl.FragmentBalanceChangeEditIncome;
import budny.moneykeeper.ui.fragments.impl.FragmentBalanceChangeEditOutcome;

public class PresenterActivityInput implements IPresenterActivityInput {
    private static final int NUM_INPUT_TYPES = 2;
    private static final String MSG_ERR_UNKNOWN_TYPE = "Unknown input type: ";

    private final Context mContext;
    private final Bundle mArgs;

    public PresenterActivityInput(Context context, Bundle args) {
        mContext = context;
        mArgs = args;
    }

    public IFragmentEdit getFragmentInput(int type) {
        switch (type) {
            case 0: {
                IFragmentEdit fragment = new FragmentBalanceChangeEditIncome();
                fragment.setArguments(mArgs);
                return fragment;
            }
            case 1: {
                IFragmentEdit fragment = new FragmentBalanceChangeEditOutcome();
                fragment.setArguments(mArgs);
                return fragment;
            }
            default:
                throw new IllegalArgumentException(MSG_ERR_UNKNOWN_TYPE + type);
        }
    }

    public String getFragmentInputName(int type) {
        switch (type) {
            case 0:
                return mContext.getString(R.string.title_income);
            case 1:
                return mContext.getString(R.string.title_outcome);
            default:
                throw new IllegalArgumentException(MSG_ERR_UNKNOWN_TYPE + type);
        }
    }

    public int getNumInputTypes() {
        return NUM_INPUT_TYPES;
    }
}

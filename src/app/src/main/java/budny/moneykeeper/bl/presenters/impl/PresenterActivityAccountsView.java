package budny.moneykeeper.bl.presenters.impl;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import budny.moneykeeper.bl.presenters.IPresenterActivityAccountsView;
import budny.moneykeeper.ui.activities.ActivityAccountEdit;
import budny.moneykeeper.ui.misc.IntentExtras;

public class PresenterActivityAccountsView implements IPresenterActivityAccountsView {
    private final Context mContext;

    public PresenterActivityAccountsView(@NonNull Context context) {
        mContext = context;
    }

    /**
     * Starts {@linkplain budny.moneykeeper.ui.activities.ActivityAccountEdit}
     * to create new category.
     */
    @Override
    public void createAccount() {
        Intent intent = new Intent(mContext, ActivityAccountEdit.class);
        intent.putExtra(IntentExtras.FIELD_ACTION, IntentExtras.ACTION_CREATE);
        mContext.startActivity(intent);
    }
}

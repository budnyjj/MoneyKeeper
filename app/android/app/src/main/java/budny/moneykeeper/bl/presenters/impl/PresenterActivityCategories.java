package budny.moneykeeper.bl.presenters.impl;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import budny.moneykeeper.bl.presenters.IPresenterActivityCategories;
import budny.moneykeeper.ui.activities.ActivityCategoryEdit;
import budny.moneykeeper.ui.misc.IntentExtras;

public class PresenterActivityCategories implements IPresenterActivityCategories {
    private final Context mContext;

    public PresenterActivityCategories(@NonNull Context context) {
        mContext = context;
    }

    /**
     * Starts {@linkplain budny.moneykeeper.ui.activities.ActivityCategoryEdit}
     * to create new category.
     */
    @Override
    public void createCategory() {
        Intent intent = new Intent(mContext, ActivityCategoryEdit.class);
        intent.putExtra(IntentExtras.FIELD_ACTION, IntentExtras.ACTION_CREATE);
        mContext.startActivity(intent);
    }
}

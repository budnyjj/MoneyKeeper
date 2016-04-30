package budny.moneykeeper.bl.presenters;

import android.content.Context;

import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterFragmentAccounts {
    void onStart();

    void onStop();

    void addDataChangeListener(IDataChangeListener listener);

    int getNumAccounts();

    Account getAccount(int position);

    /**
     * Update specified category.
     *
     * @param context activity context
     * @param index   index of category to update
     */
    void updateAccount(Context context, int index);

    void deleteAccount(int position);

    void swapAccounts(int fromPosition, int toPosition);
}

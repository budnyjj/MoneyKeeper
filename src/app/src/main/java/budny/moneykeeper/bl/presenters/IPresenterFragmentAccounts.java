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
     * @param index   index of account to update
     */
    void updateAccount(Context context, int index);

    /**
     * Deletes specified account, if possible.
     *
     * @param index index of account to delete
     * @return true, if deletion succeed, false otherwise
     */
    boolean deleteAccount(int index);

    void swapAccounts(int fromPosition, int toPosition);
}

package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.model.BalanceChange;
import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterFragmentAccountView {
    void onStart();

    void onStop();

    void addDataChangeListener(IDataChangeListener listener);

    String getCurrencyCode();

    long getTotalAmount();

    int getNumBalanceChanges();

    BalanceChange getBalanceChange(int position);

    void updateBalanceChange(int position);

    /**
     * Deletes specified balance change, if possible.
     *
     * @param position position of balance change to delete
     * @return true, if deletion succeed, false otherwise
     */
    boolean deleteBalanceChange(int position);

    /**
     * Undo last category delete balance change.
     *
     * @param position position in adapter to insert undo-deleted item
     * @return true, if undo succeed, false otherwise
     */
    boolean unDeleteLastBalanceChange(int position);
}
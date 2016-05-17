package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterFragmentAccountsView {
    void onStart();

    void onStop();

    void addDataChangeListener(IDataChangeListener listener);

    int getNumAccounts();

    String getAccountName(int position);

    /**
     * Update specified category.
     *
     * @param position position of account to update
     */
    void updateAccount(int position);

    /**
     * Deletes specified account, if possible.
     *
     * @param position position in adapter of item to delete
     * @return true, if deletion succeed, false otherwise
     */
    boolean deleteAccount(int position);

    /**
     * Undo last account delete operation.
     *
     * @param position position in adapter item undo-deleted item
     * @return true, if undo succeed, false otherwise
     */
    boolean unDeleteLastAccount(int position);

    void swapAccounts(int fromPosition, int toPosition);
}

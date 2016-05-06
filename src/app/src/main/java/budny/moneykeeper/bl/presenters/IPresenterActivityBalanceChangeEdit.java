package budny.moneykeeper.bl.presenters;


import budny.moneykeeper.ui.fragments.IFragmentEdit;

public interface IPresenterActivityBalanceChangeEdit {
    IFragmentEdit getFragmentEdit(int type);

    String getFragmentEditName(int type);

    int getNumEditTypes();

    /**
     * Provides the edit type, which is suitable for the balance change,
     * specified in arguments bundle.
     */
    int getSuggestedEditType();
}

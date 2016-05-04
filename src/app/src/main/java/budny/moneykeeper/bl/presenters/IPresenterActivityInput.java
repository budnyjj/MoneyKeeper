package budny.moneykeeper.bl.presenters;


import budny.moneykeeper.ui.fragments.IFragmentEdit;

public interface IPresenterActivityInput {
    IFragmentEdit getFragmentInput(int type);

    String getFragmentInputName(int type);

    int getNumInputTypes();
}

package budny.moneykeeper.bl.presenters;


import budny.moneykeeper.ui.fragments.IEditFragment;

public interface IPresenterActivityInput {
    IEditFragment getFragmentInput(int type);

    String getFragmentInputName(int type);

    int getNumInputTypes();
}

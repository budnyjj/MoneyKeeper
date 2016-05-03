package budny.moneykeeper.bl.presenters;


import android.support.v4.app.Fragment;

public interface IPresenterActivityInput {
    Fragment getFragmentInput(int type);

    String getFragmentInputName(int type);

    int getNumInputTypes();
}

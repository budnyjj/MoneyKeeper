package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterFragmentAccountView {
    void onStart();

    void onStop();

    void addDataChangeListener(IDataChangeListener listener);

    long getTotalAmount();
}

package budny.moneykeeper.bl.presenters;

import android.support.v4.app.Fragment;

import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterActivityBalanceChangesView {
    void onCreate();

    void onDestroy();

    void addDataChangeListener(IDataChangeListener listener);

    Fragment getFragmentAccountView(int index);

    CharSequence getFragmentAccountViewName(int index);

    int getNumAccounts();

    void createBalanceChange(int accountIndex);
}

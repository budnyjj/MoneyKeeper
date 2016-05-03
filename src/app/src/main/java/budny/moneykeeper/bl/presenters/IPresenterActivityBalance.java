package budny.moneykeeper.bl.presenters;

import android.support.v4.app.Fragment;

import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterActivityBalance {
    void onCreate();

    void onDestroy();

    void addDataChangeListener(IDataChangeListener listener);

    Fragment getFragmentAccountView(int position);

    CharSequence getFragmentAccountViewName(int position);

    int getNumAccounts();

    void createBalanceChange();
}

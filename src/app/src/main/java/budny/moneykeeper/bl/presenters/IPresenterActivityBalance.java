package budny.moneykeeper.bl.presenters;

import android.support.v4.app.Fragment;

import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterActivityBalance {
    void onCreate();

    void onDestroy();

    void addDataChangeListener(IDataChangeListener listener);

    Fragment getAccountFragment(int position);

    CharSequence getAccountName(int position);

    int getNumAccounts();
}

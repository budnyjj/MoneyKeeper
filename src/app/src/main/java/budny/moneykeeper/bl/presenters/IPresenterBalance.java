package budny.moneykeeper.bl.presenters;

import android.support.v4.app.Fragment;

public interface IPresenterBalance {
    void onCreate();

    void onDestroy();

    Fragment getAccountFragment(int position);

    CharSequence getAccountName(int position);

    int getNumAccounts();
}

package budny.moneykeeper;

import android.app.Application;

import budny.moneykeeper.db.DBManager;

public class MoneyKeeperApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DBManager.getInstance().init(this);
    }
}

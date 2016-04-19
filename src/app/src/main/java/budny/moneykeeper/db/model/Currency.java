package budny.moneykeeper.db.model;

import io.realm.RealmObject;

public class Currency extends RealmObject {
    private String mName;
    private String mConversionRate;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getConversionRate() {
        return mConversionRate;
    }

    public void setConversionRate(String rate) {
        mConversionRate = rate;
    }
}

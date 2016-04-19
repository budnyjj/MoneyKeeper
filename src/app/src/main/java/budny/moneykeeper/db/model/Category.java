package budny.moneykeeper.db.model;

import io.realm.RealmObject;

public class Category extends RealmObject {
    private String mName;
    private String mConversionRate;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    // TODO: use BigDecimal instead
    public String getConversionRate() {
        return mConversionRate;
    }

    // TODO: use BigDecimal instead
    public void setConversionRate(String rate) {
        mConversionRate = rate;
    }
}

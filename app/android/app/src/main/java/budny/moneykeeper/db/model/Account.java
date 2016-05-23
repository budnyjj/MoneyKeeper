package budny.moneykeeper.db.model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Account extends RealmObject {
    public static final String FIELD_INDEX = "mIndex";

    @SuppressWarnings("unused")
    private RealmList<BalanceChange> mBalanceChanges;
    private String mCurrencyCode;
    private int mIndex;
    private String mName;

    public String getCurrencyCode() {
        return mCurrencyCode;
    }

    public void setCurrencyCode(String currency) {
        mCurrencyCode = currency;
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public RealmList<BalanceChange> getBalanceChanges() {
        return mBalanceChanges;
    }
}

package budny.moneykeeper.db.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Account extends RealmObject {
    public static final String FIELD_INDEX = "mIndex";

    private RealmList<BalanceChange> mBalanceChanges;
    private String mCurrencyCode;
    private int mIndex;
    private String mName;
    private String mTotalAmount;

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

    // TODO: use lazy evaluation to provide total amount
    // TODO: try to return it as BigDecimal
    public String getTotalAmount() {
        return mTotalAmount;
    }

    // TODO: do not provide direct access to the list of balance changes
    public RealmList<BalanceChange> getBalanceChanges() {
        return mBalanceChanges;
    }

    public void setBalanceChanges(RealmList<BalanceChange> changes) {
        mBalanceChanges = changes;
    }
}

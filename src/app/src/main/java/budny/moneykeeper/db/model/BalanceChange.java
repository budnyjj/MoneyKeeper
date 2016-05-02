package budny.moneykeeper.db.model;

import java.util.Date;

import io.realm.RealmObject;

public class BalanceChange extends RealmObject {
    public static final String FIELD_AMOUNT = "mAmount";
    public static final String FIELD_DATE = "mDate";

    private long mAmount;
    private Category mCategory;
    private Date mDate;

    public long getAmount() {
        return mAmount;
    }

    public void setAmount(long amount) {
        mAmount = amount;
    }

    public Category getCategory() {
        return mCategory;
    }

    public void setCategory(Category category) {
        mCategory = category;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}

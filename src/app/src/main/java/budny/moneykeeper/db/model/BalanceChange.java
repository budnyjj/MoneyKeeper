package budny.moneykeeper.db.model;

import io.realm.RealmObject;

public class BalanceChange extends RealmObject {
    public static final String FIELD_AMOUNT = "mAmount";

    private long mAmount;
    private Category mCategory;
    private String mCurrency;

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

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
    }
}

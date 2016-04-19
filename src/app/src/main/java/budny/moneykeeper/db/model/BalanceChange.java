package budny.moneykeeper.db.model;

import io.realm.RealmObject;

public class BalanceChange extends RealmObject {
    private String mAmount;
    private Category mCategory;
    private String mCurrency;

    // TODO: use BigDecimal instead
    public String getAmount() {
        return mAmount;
    }

    // TODO: use BigDecimal instead
    public void setAmount(String amount) {
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

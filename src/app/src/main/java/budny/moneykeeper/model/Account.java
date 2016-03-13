package budny.moneykeeper.model;

import java.math.BigDecimal;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Account extends RealmObject {
    private String mName;
    private String mDefaultCurrency;
    private RealmList<BalanceChange> mBalanceChanges;
    private String mTotalAmount;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDefaultCurrency() {
        return mDefaultCurrency;
    }

    public void setDefaultCurrency(String currency) {
        mDefaultCurrency = currency;
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

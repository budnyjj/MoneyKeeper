package budny.moneykeeper.db.model;

import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class BalanceChange extends RealmObject {
    public static final String FIELD_AMOUNT = "mAmount";
    public static final String FIELD_DATE = "mDate";
    public static final long FRACTION_DIVISOR = 1000_000_000L;

    /**
     * Amount of change.
     * It is stored as long: 9 lower digits represent fraction part;
     *                       rest of upper digits are for the integer part
     */
    private long mAmount;
    @SuppressWarnings("unused")
    private RealmList<Category> mCategories;
    private Date mDate;

    public long getAmount() {
        return mAmount;
    }

    public void setAmount(long amount) {
        mAmount = amount;
    }

    public List<Category> getCategories() {
        return mCategories;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}

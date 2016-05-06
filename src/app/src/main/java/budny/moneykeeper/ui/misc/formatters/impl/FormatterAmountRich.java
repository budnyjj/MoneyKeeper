package budny.moneykeeper.ui.misc.formatters.impl;

import java.text.NumberFormat;
import java.util.Currency;

import budny.moneykeeper.db.model.BalanceChange;
import budny.moneykeeper.ui.misc.formatters.IFormatter;

public class FormatterAmountRich implements IFormatter<Long> {
    private static final String SIGN_MINUS = "-";
    private static final String SIGN_PLUS = "+";

    private final NumberFormat mCurrencyFormat;

    public FormatterAmountRich() {
        mCurrencyFormat = NumberFormat.getCurrencyInstance();
    }

    public void setCurrencyCode(String currencyCode) {
        mCurrencyFormat.setCurrency(Currency.getInstance(currencyCode));
    }

    @Override
    public String format(Long amount) {
        String formattedAmount =
                mCurrencyFormat.format((float) Math.abs(amount) / BalanceChange.FRACTION_DIVISOR);
        if (amount > 0) {
            return SIGN_PLUS + formattedAmount;
        } else if (amount < 0) {
            return SIGN_MINUS + formattedAmount;
        } else {
            return formattedAmount;
        }
    }
}

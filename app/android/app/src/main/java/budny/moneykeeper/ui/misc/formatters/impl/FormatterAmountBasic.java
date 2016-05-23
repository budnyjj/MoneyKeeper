package budny.moneykeeper.ui.misc.formatters.impl;

import java.text.NumberFormat;

import budny.moneykeeper.db.model.BalanceChange;
import budny.moneykeeper.ui.misc.formatters.IFormatter;

public class FormatterAmountBasic implements IFormatter<Long> {
    @Override
    public String format(Long amount) {
        return String.valueOf((float) Math.abs(amount) / BalanceChange.FRACTION_DIVISOR);
    }
}

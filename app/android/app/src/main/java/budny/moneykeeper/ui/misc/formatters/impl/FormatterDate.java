package budny.moneykeeper.ui.misc.formatters.impl;

import java.text.DateFormat;
import java.util.Date;

import budny.moneykeeper.ui.misc.formatters.IFormatter;

public class FormatterDate implements IFormatter<Date> {
    private final DateFormat mDateFormat = DateFormat.getDateInstance();

    @Override
    public String format(Date date) {
        return mDateFormat.format(date);
    }
}

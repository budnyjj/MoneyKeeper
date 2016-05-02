package budny.moneykeeper.ui.misc.formatters.impl;

import android.content.Context;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import budny.moneykeeper.ui.misc.formatters.IDateFormatter;

public class DateFormatter implements IDateFormatter {
    private final DateFormat mDateFormat = DateFormat.getDateInstance();
    private final TextView mViewDate;


    public DateFormatter(TextView viewDate) {
        mViewDate = viewDate;
    }

    @Override
    public void format(Date date) {
        mViewDate.setText(mDateFormat.format(date));
    }
}

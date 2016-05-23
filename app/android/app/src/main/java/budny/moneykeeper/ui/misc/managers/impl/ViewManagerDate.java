package budny.moneykeeper.ui.misc.managers.impl;

import android.widget.TextView;

import java.util.Date;

import budny.moneykeeper.ui.misc.formatters.IFormatter;
import budny.moneykeeper.ui.misc.formatters.impl.FormatterDate;
import budny.moneykeeper.ui.misc.managers.IViewManager;

public class ViewManagerDate implements IViewManager<Date> {
    private final IFormatter<Date> mDateFormatter = new FormatterDate();
    private final TextView mDateView;

    public ViewManagerDate(TextView dateView) {
        mDateView = dateView;
    }

    @Override
    public void setValue(Date date) {
        mDateView.setText(mDateFormatter.format(date));
    }
}

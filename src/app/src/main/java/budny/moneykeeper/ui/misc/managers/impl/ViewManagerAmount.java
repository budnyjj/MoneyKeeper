package budny.moneykeeper.ui.misc.managers.impl;

import android.content.Context;
import android.widget.TextView;

import budny.moneykeeper.R;
import budny.moneykeeper.ui.misc.formatters.impl.FormatterAmountRich;
import budny.moneykeeper.ui.misc.managers.IViewManagerAmount;

public class ViewManagerAmount implements IViewManagerAmount {
    private final Context mContext;
    private final TextView mAmountView;
    private final FormatterAmountRich mAmountFormatter;

    public ViewManagerAmount(Context context, TextView amountView) {
        mContext = context;
        mAmountView = amountView;
        mAmountFormatter = new FormatterAmountRich();
    }

    @Override
    public void setCurrencyCode(String currencyCode) {
        mAmountFormatter.setCurrencyCode(currencyCode);
    }

    @Override
    public void setValue(Long amount) {
        if (amount > 0) {
            mAmountView.setTextColor(mContext.getResources().getColor(R.color.text_green_dark));
        } else if (amount < 0) {
            mAmountView.setTextColor(mContext.getResources().getColor(R.color.text_red_dark));
        }
        mAmountView.setText(mAmountFormatter.format(amount));
    }
}
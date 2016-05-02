package budny.moneykeeper.ui.misc.formatters.impl;

import android.content.Context;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Currency;

import budny.moneykeeper.R;
import budny.moneykeeper.ui.misc.formatters.ICurrencyFormatter;

public class CurrencyFormatter implements ICurrencyFormatter {
    private static final String SIGN_MINUS = "-";
    private static final String SIGN_PLUS = "+";

    private final NumberFormat mCurrencyFormat;
    private final Context mContext;
    private final TextView mViewAmount;

    public CurrencyFormatter(Context context, TextView viewAmount) {
        mContext = context;
        mViewAmount = viewAmount;
        mCurrencyFormat = NumberFormat.getCurrencyInstance();
    }

    @SuppressWarnings("deprecation")
    public void format(long amount, String currencyCode) {
        mCurrencyFormat.setCurrency(Currency.getInstance(currencyCode));
        String amountStr = mCurrencyFormat.format(Math.abs(amount));
        if (amount > 0) {
            mViewAmount.setText(SIGN_PLUS + amountStr);
            mViewAmount.setTextColor(mContext.getResources().getColor(R.color.text_green_dark));
        } else if (amount < 0) {
            mViewAmount.setText(SIGN_MINUS + amountStr);
            mViewAmount.setTextColor(mContext.getResources().getColor(R.color.text_red_dark));
        } else {
            // amount == 0
            mViewAmount.setText(amountStr);
        }
    }
}

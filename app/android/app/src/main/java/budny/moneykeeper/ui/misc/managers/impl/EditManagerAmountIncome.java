package budny.moneykeeper.ui.misc.managers.impl;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import budny.moneykeeper.ui.misc.formatters.IFormatter;
import budny.moneykeeper.ui.misc.formatters.impl.FormatterAmountBasic;
import budny.moneykeeper.ui.misc.managers.IEditManagerAmount;
import budny.moneykeeper.ui.misc.validators.IValidator;
import budny.moneykeeper.ui.misc.validators.impl.ValidatorAmount;

public class EditManagerAmountIncome implements IEditManagerAmount {
    private final IFormatter<Long> mAmountFormatter = new FormatterAmountBasic();
    private final IValidator<Long> mAmountValidator = new ValidatorAmount();
    @SuppressWarnings("FieldCanBeLocal")
    private final EditText mEditText;
    private final TextInputLayout mInputLayout;
    private final String mErrorMsg;

    private Long mValue;

    @SuppressWarnings("deprecation")
    public EditManagerAmountIncome(EditText editText, TextInputLayout inputLayout,
                                   String errorMsg, boolean isInteractive) {
        mEditText = editText;
        mInputLayout = inputLayout;
        mErrorMsg = errorMsg;
        if (isInteractive) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        mValue = mAmountValidator.validate(s.toString());
                        mInputLayout.setErrorEnabled(false);
                    } catch (IllegalArgumentException e) {
                        mInputLayout.setError(mErrorMsg);
                        mEditText.requestFocus();
                    }
                }
            });
        }
    }

    @Override
    public Long getValue() {
        return Math.abs(mValue);
    }

    @Override
    public void setValue(Long value) {
        try {
            mEditText.setText(mAmountFormatter.format(value));
        } catch (IllegalArgumentException e) {
            mInputLayout.setError(mErrorMsg);
            mEditText.requestFocus();
        }
    }

    @Override
    public boolean isValid() {
        try {
            mValue = mAmountValidator.validate(mEditText.getText().toString());
            return true;
        } catch (IllegalArgumentException e) {
            mInputLayout.setError(mErrorMsg);
            return false;
        }
    }

    @Override
    public void setCurrencyCode(String currencyCode) {

    }
}

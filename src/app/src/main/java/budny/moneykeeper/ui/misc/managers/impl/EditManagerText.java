package budny.moneykeeper.ui.misc.managers.impl;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import budny.moneykeeper.ui.misc.formatters.IFormatter;
import budny.moneykeeper.ui.misc.formatters.impl.FormatterText;
import budny.moneykeeper.ui.misc.managers.IEditManager;
import budny.moneykeeper.ui.misc.validators.IValidator;
import budny.moneykeeper.ui.misc.validators.impl.ValidatorText;

public class EditManagerText implements IEditManager<String> {
    private final IFormatter<String> mTextFormatter = new FormatterText();
    private final IValidator<String> mTextValidator = new ValidatorText();
    private final EditText mEditText;
    private final TextInputLayout mInputLayout;
    private final String mErrorMsg;

    private String mValue;

    public EditManagerText(
            EditText editText, TextInputLayout inputLayout, String errorMsg, boolean isInteractive) {
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
                        mValue = mTextValidator.validate(s.toString());
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
    public String getValue() {
        return mValue;
    }

    @Override
    public void setValue(String value) {
        try {
            mEditText.setText(mTextFormatter.format(value));
        } catch (IllegalArgumentException e) {
            mInputLayout.setError(mErrorMsg);
            mEditText.requestFocus();
        }
    }

    @Override
    public boolean isValid() {
        try {
            mValue = mTextValidator.validate(mEditText.getText().toString());
            return true;
        } catch (IllegalArgumentException e) {
            mInputLayout.setError(mErrorMsg);
            return false;
        }
    }
}

package budny.moneykeeper.ui.misc;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import budny.moneykeeper.bl.validators.IContentValidator;

/**
 * Text watcher with validation function.
 */
public class ValidationTextWatcher implements TextWatcher {
    private final IContentValidator mValidator;
    private final TextInputLayout mInputContainer;
    private final EditText mInputField;
    private final String mErrorMsg;

    public ValidationTextWatcher(EditText inputField, TextInputLayout inputContainer,
                                 IContentValidator validator, String errorMsg) {
        mErrorMsg = errorMsg;
        mInputField = inputField;
        mInputContainer = inputContainer;
        mValidator = validator;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // nothing to do
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // nothing to do
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mValidator.validate(s.toString())) {
            mInputContainer.setErrorEnabled(false);
        } else {
            mInputContainer.setError(mErrorMsg);
            mInputField.requestFocus();
        }
    }
}

package budny.moneykeeper.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterFragmentAccountEdit;
import budny.moneykeeper.bl.presenters.impl.PresenterFragmentAccountEdit;
import budny.moneykeeper.bl.validators.IValidator;
import budny.moneykeeper.bl.validators.impl.TextValidator;
import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.ui.misc.ValidationTextWatcher;
import budny.moneykeeper.ui.misc.listeners.ISaveContentListener;

public class FragmentAccountEdit extends Fragment
        implements ISaveContentListener {
    private String mErrorMsgAccountName;

    private IPresenterFragmentAccountEdit mPresenter = new PresenterFragmentAccountEdit();
    private IValidator mTextValidator = new TextValidator();

    @SuppressWarnings("FieldCanBeLocal")
    private TextInputLayout mAccountNameContainer;
    private EditText mAccountNameField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_edit, container, false);

        mErrorMsgAccountName = getString(R.string.err_msg_account_name);
        mAccountNameContainer = (TextInputLayout) view.findViewById(R.id.input_container_edit_account_name);
        mAccountNameField = (EditText) view.findViewById(R.id.edit_account_name);
        mAccountNameField.addTextChangedListener(
                new ValidationTextWatcher(
                        mAccountNameField, mAccountNameContainer,
                        mTextValidator, mErrorMsgAccountName));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public boolean onSaveContent() {
        String accountName = mAccountNameField.getText().toString().trim();
        if (!mTextValidator.validate(accountName)) {
            mAccountNameContainer.setError(mErrorMsgAccountName);
            return false;
        }

        Account account = new Account();
        account.setName(accountName);
        mPresenter.addAccount(account);
        return true;
    }
}

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
import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.ui.misc.IntentExtras;
import budny.moneykeeper.ui.misc.ValidationTextWatcher;
import budny.moneykeeper.ui.misc.listeners.IContentEditListener;

public class FragmentAccountEdit extends Fragment implements IContentEditListener {
    // action to perform with account (create or update)
    private String mAction = IntentExtras.ACTION_INVALID;
    // index of account to edit
    private int mAccountIdx = IntentExtras.INDEX_INVALID;
    private IPresenterFragmentAccountEdit mPresenter = new PresenterFragmentAccountEdit();
    private IValidator mTextValidator = new TextValidator();
    private String mErrorMsgAccountName;

    @SuppressWarnings("FieldCanBeLocal")
    private TextInputLayout mAccountNameContainer;
    private EditText mAccountNameField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // parse arguments
        Bundle args = getArguments();
        if (args != null) {
            String action = args.getString(IntentExtras.FIELD_ACTION);
            mAction = (action == null) ? IntentExtras.ACTION_INVALID : action;
            mAccountIdx = args.getInt(IntentExtras.FIELD_INDEX, IntentExtras.INDEX_INVALID);
        }
        // setup owned views
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
        updateViews();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public boolean onEditContent() {
        String accountName = mAccountNameField.getText().toString().trim();
        if (!mTextValidator.validate(accountName)) {
            mAccountNameContainer.setError(mErrorMsgAccountName);
            return false;
        }

        Account account = new Account();
        account.setName(accountName);

        switch (mAction) {
            case IntentExtras.ACTION_CREATE:
                mPresenter.createAccount(account);
                break;
            case IntentExtras.ACTION_UPDATE:
                mPresenter.updateAccount(account, mAccountIdx);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Fills owned views with data.
     */
    private void updateViews() {
        if (IntentExtras.ACTION_UPDATE.equals(mAction)
                && mAccountIdx != IntentExtras.INDEX_INVALID) {
            Account account = mPresenter.getAccount(mAccountIdx);
            mAccountNameField.setText(account.getName());
        }
    }
}

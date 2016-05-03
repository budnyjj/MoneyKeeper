package budny.moneykeeper.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterFragmentAccountEdit;
import budny.moneykeeper.bl.presenters.impl.PresenterFragmentAccountEdit;
import budny.moneykeeper.bl.validators.IValidator;
import budny.moneykeeper.bl.validators.impl.TextValidator;
import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.ui.misc.IntentExtras;
import budny.moneykeeper.ui.misc.RVItemDividerDecoration;
import budny.moneykeeper.ui.misc.RVItemTouchListener;
import budny.moneykeeper.ui.misc.ValidationTextWatcher;
import budny.moneykeeper.ui.misc.listeners.IContentEditListener;
import budny.moneykeeper.ui.misc.listeners.IRVItemClickListener;

public class FragmentAccountEdit extends Fragment implements IContentEditListener {
    private static final String TAG = FragmentAccountEdit.class.getSimpleName();

    private final IValidator mTextValidator = new TextValidator();

    // action to perform with account (create or update)
    private String mAction = IntentExtras.ACTION_INVALID;
    // index of account to edit
    private int mAccountIdx = IntentExtras.INDEX_INVALID;
    private IPresenterFragmentAccountEdit mPresenter;
    private String mErrorMsgAccountName;

    @SuppressWarnings("FieldCanBeLocal")
    private TextInputLayout mAccountNameContainer;
    private EditText mAccountNameField;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView.LayoutManager mLayoutManager;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView mRecyclerView;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView.Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // parse arguments
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException(
                    TAG + " is not initialized with arguments bundle");
        }
        String action = args.getString(IntentExtras.FIELD_ACTION);
        mAction = (action == null) ? IntentExtras.ACTION_INVALID : action;
        if (IntentExtras.ACTION_INVALID.equals(mAction)) {
            throw new IllegalArgumentException(
                    "Unable to locate following arguments: " + IntentExtras.FIELD_ACTION);
        }
        if (IntentExtras.ACTION_UPDATE.equals(mAction)) {
            mAccountIdx = args.getInt(IntentExtras.FIELD_INDEX, IntentExtras.INDEX_INVALID);
            if (mAccountIdx == IntentExtras.INDEX_INVALID) {
                throw new IllegalArgumentException(
                        "Unable to locate following arguments: " + IntentExtras.FIELD_INDEX);
            }
        }
        mPresenter = new PresenterFragmentAccountEdit(mAction, mAccountIdx);
        // setup owned views
        View view = inflater.inflate(R.layout.fragment_account_edit, container, false);
        mErrorMsgAccountName = getString(R.string.err_msg_account_name);
        mAccountNameContainer = (TextInputLayout) view.findViewById(R.id.input_container_edit_account_name);
        mAccountNameField = (EditText) view.findViewById(R.id.edit_account_name);
        mAccountNameField.addTextChangedListener(
                new ValidationTextWatcher(
                        mAccountNameField, mAccountNameContainer,
                        mTextValidator, mErrorMsgAccountName));
        // setup recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_currencies);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RVCurrenciesAdapter(mPresenter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RVItemTouchListener(getActivity(), mRecyclerView, new RVItemClickListener(mPresenter)));
        mRecyclerView.addItemDecoration(new RVItemDividerDecoration(getContext()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
        updateFields();
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
        // TODO: setup account fields dynamically
        account.setCurrencyCode("EUR");

        switch (mAction) {
            case IntentExtras.ACTION_CREATE:
                mPresenter.createAccount(accountName, mPresenter.getSelectedCurrencyCode());
                break;
            case IntentExtras.ACTION_UPDATE:
                mPresenter.updateAccount(accountName, mPresenter.getSelectedCurrencyCode());
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Fills owned fields with data.
     */
    private void updateFields() {
        if (IntentExtras.ACTION_UPDATE.equals(mAction)
                && mAccountIdx != IntentExtras.INDEX_INVALID) {
            Account account = mPresenter.getAccount();
            mAccountNameField.setText(account.getName());
        }
    }

    private static class RVCurrenciesAdapter
            extends RecyclerView.Adapter<RVCurrenciesAdapter.ViewHolder> {
        private final IPresenterFragmentAccountEdit mPresenter;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView mCurrencyNameView;
            public final RadioButton mSelectedButton;

            public ViewHolder(View v) {
                super(v);
                mCurrencyNameView = (TextView) v.findViewById(R.id.text_view_currency_name);
                mSelectedButton = (RadioButton) v.findViewById(R.id.radio_button_selected);
            }
        }

        public RVCurrenciesAdapter(IPresenterFragmentAccountEdit presenter) {
            mPresenter = presenter;
            mPresenter.addCurrencySelectedListener(new IDataChangeListener() {
                @Override
                public void onChange() {
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public RVCurrenciesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_row_currency, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mCurrencyNameView.setText(mPresenter.getCurrencyName(position));
            holder.mSelectedButton.setChecked(mPresenter.isSelectedCurrency(position));
        }

        @Override
        public int getItemCount() {
            return mPresenter.getNumCurrencies();
        }
    }

    private class RVItemClickListener implements IRVItemClickListener {
        private final IPresenterFragmentAccountEdit mPresenter;

        public RVItemClickListener(IPresenterFragmentAccountEdit presenter) {
            mPresenter = presenter;
        }

        @Override
        public void onItemClick(View view, int position) {
            mPresenter.selectCurrency(position);
        }

        @Override
        public void onItemLongClick(View view, int position) {

        }
    }
}

package budny.moneykeeper.ui.fragments.impl;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
import budny.moneykeeper.bl.validators.IContentValidator;
import budny.moneykeeper.bl.validators.impl.TextValidator;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.ui.fragments.IFragmentEdit;
import budny.moneykeeper.ui.misc.IntentExtras;
import budny.moneykeeper.ui.misc.RVItemDividerDecoration;
import budny.moneykeeper.ui.misc.RVItemTouchListener;
import budny.moneykeeper.ui.misc.ValidationTextWatcher;
import budny.moneykeeper.ui.misc.listeners.IRVItemClickListener;

/**
 * A fragment used to edit specified account.
 * Concrete action (add/modify) as well as index of the
 * target account are specified by the arguments bundle.
 */
public class FragmentAccountEdit extends IFragmentEdit {
    private static final String TAG = FragmentAccountEdit.class.getSimpleName();

    private final IContentValidator mTextValidator = new TextValidator();

    private IPresenterFragmentAccountEdit mPresenter;
    // action to perform with account (create or update)
    private String mAction = IntentExtras.ACTION_INVALID;
    // index of account to edit
    private int mAccountIndex = IntentExtras.INDEX_INVALID;

    @SuppressWarnings("FieldCanBeLocal")
    private TextInputLayout mAccountNameLayout;
    @SuppressWarnings("FieldCanBeLocal")
    private EditText mAccountNameText;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView.LayoutManager mCurrenciesManager;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView mCurrenciesView;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView.Adapter mCurrenciesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parseArguments();
        mPresenter = new PresenterFragmentAccountEdit(mAction, mAccountIndex);
        View rootView = inflater.inflate(R.layout.fragment_account_edit, container, false);
        initViews(rootView);
        return rootView;
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
        String accountName = mAccountNameText.getText().toString().trim();
        if (!mTextValidator.validate(accountName)) {
            mAccountNameLayout.setError(getString(R.string.err_msg_account_name));
            return false;
        }

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

    private void parseArguments() {
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
            mAccountIndex = args.getInt(IntentExtras.FIELD_INDEX_ACCOUNT, IntentExtras.INDEX_INVALID);
            if (mAccountIndex == IntentExtras.INDEX_INVALID) {
                throw new IllegalArgumentException(
                        "Unable to locate following arguments: " + IntentExtras.FIELD_INDEX_ACCOUNT);
            }
        }
    }

    private void initViews(View rootView) {
        // account name text view
        mAccountNameLayout = (TextInputLayout) rootView.findViewById(
                R.id.fragment_account_edit_edit_text_container_account_name);
        mAccountNameText = (EditText) rootView.findViewById(
                R.id.fragment_account_edit_edit_text_account_name);
        mAccountNameText.addTextChangedListener(
                new ValidationTextWatcher(mAccountNameText, mAccountNameLayout,
                        mTextValidator, getString(R.string.err_msg_account_name)));
        // currencies recycler view
        mCurrenciesView = (RecyclerView) rootView.findViewById(
                R.id.fragment_account_edit_recycler_view_currencies);
        mCurrenciesManager = new LinearLayoutManager(getContext());
        mCurrenciesAdapter = new RVCurrenciesAdapter(mPresenter);
        mCurrenciesView.setLayoutManager(mCurrenciesManager);
        mCurrenciesView.setAdapter(mCurrenciesAdapter);
        mCurrenciesView.addOnItemTouchListener(new RVItemTouchListener(
                getActivity(), mCurrenciesView, new RVItemClickListener(mPresenter)));
        mCurrenciesView.addItemDecoration(new RVItemDividerDecoration(getContext()));
    }

    /**
     * Fills owned fields with data based on contents of the arguments bundle.
     */
    private void updateViews() {
        if (IntentExtras.ACTION_UPDATE.equals(mAction)
                && mAccountIndex != IntentExtras.INDEX_INVALID) {
            mAccountNameText.setText(mPresenter.getAccountName());
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
                mCurrencyNameView = (TextView) v.findViewById(
                        R.id.rv_row_currency_select_text_view_currency_name);
                mSelectedButton = (RadioButton) v.findViewById(
                        R.id.rv_row_currency_select_radio_button_selected);
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
                    .inflate(R.layout.rv_row_currency_select, parent, false);
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

    private static class RVItemClickListener implements IRVItemClickListener {
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

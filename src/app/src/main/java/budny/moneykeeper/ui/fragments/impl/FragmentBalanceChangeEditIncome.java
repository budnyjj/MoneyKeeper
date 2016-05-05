package budny.moneykeeper.ui.fragments.impl;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterFragmentBalanceChangeEditIncome;
import budny.moneykeeper.bl.presenters.impl.PresenterFragmentBalanceChangeEditIncome;
import budny.moneykeeper.bl.validators.IContentValidator;
import budny.moneykeeper.bl.validators.impl.CurrencyValidator;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.ui.fragments.IFragmentEdit;
import budny.moneykeeper.ui.misc.IntentExtras;
import budny.moneykeeper.ui.misc.RVItemDividerDecoration;
import budny.moneykeeper.ui.misc.RVItemTouchListener;
import budny.moneykeeper.ui.misc.ValidationTextWatcher;
import budny.moneykeeper.ui.misc.listeners.IRVItemClickListener;

public class FragmentBalanceChangeEditIncome extends IFragmentEdit {
    private static final String TAG = FragmentCategoryEdit.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized with arguments bundle";
    private static final String MSG_NO_ARGS = "Unable to locate following arguments: ";

    private final IContentValidator mCurrencyValidator = new CurrencyValidator();
    // action to perform with balance change (create or update)
    private String mAction = IntentExtras.ACTION_INVALID;
    // index of parent account
    private int mAccountIndex = IntentExtras.INDEX_INVALID;
    // index of balance change to edit
    private int mBalanceChangeIndex = IntentExtras.INDEX_INVALID;

    private IPresenterFragmentBalanceChangeEditIncome mPresenter;
    @SuppressWarnings("FieldCanBeLocal")
    private TextInputLayout mAmountLayout;
    @SuppressWarnings("FieldCanBeLocal")
    private EditText mAmountText;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView.LayoutManager mCategoriesManager;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView mCategoriesView;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView.Adapter mCategoriesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parseArguments();
        mPresenter = new PresenterFragmentBalanceChangeEditIncome(mAction, mAccountIndex, mBalanceChangeIndex);
        View rootView = inflater.inflate(R.layout.fragment_balance_change_edit_income, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
        updateViews();
        // update recycler view contents for those cases,
        // when user navigates back from corresponding edit activity
        mCategoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public boolean onEditContent() {
        // TODO: use custom formatter?
        String amountStr = mAmountText.getText().toString().trim();
        if (!mCurrencyValidator.validate(amountStr)) {
            mAmountLayout.setError(getString(R.string.err_msg_amount));
            return false;
        }
        long amount = Long.valueOf(amountStr);

        switch (mAction) {
            case IntentExtras.ACTION_CREATE:
                mPresenter.createBalanceChange(amount, mPresenter.getSelectedCategories());
                break;
            case IntentExtras.ACTION_UPDATE:
                mPresenter.updateBalanceChange(amount, mPresenter.getSelectedCategories());
                break;
            default:
                break;
        }
        return true;
    }


    private void parseArguments() {
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        mAccountIndex = args.getInt(IntentExtras.FIELD_INDEX_ACCOUNT, IntentExtras.INDEX_INVALID);
        if (mAccountIndex == IntentExtras.INDEX_INVALID) {
            throw new IllegalArgumentException(
                    MSG_NO_ARGS + IntentExtras.FIELD_INDEX_ACCOUNT);
        }
        String action = args.getString(IntentExtras.FIELD_ACTION);
        mAction = (action == null) ? IntentExtras.ACTION_INVALID : action;
        if (IntentExtras.ACTION_INVALID.equals(mAction)) {
            throw new IllegalArgumentException(
                    MSG_NO_ARGS + IntentExtras.FIELD_ACTION);
        }
        if (IntentExtras.ACTION_UPDATE.equals(mAction)) {
            mBalanceChangeIndex =
                    args.getInt(IntentExtras.FIELD_INDEX_BALANCE_CHANGE, IntentExtras.INDEX_INVALID);
            if (mBalanceChangeIndex == IntentExtras.INDEX_INVALID) {
                throw new IllegalArgumentException(
                        MSG_NO_ARGS + IntentExtras.FIELD_INDEX_BALANCE_CHANGE);
            }
        }
    }

    private void initViews(View rootView) {
        // amount edit text
        mAmountLayout = (TextInputLayout) rootView.findViewById(
                R.id.fragment_balance_change_edit_income_text_container_amount);
        mAmountText = (EditText) rootView.findViewById(
                R.id.fragment_balance_change_edit_income_edit_text_amount);
        mAmountText.addTextChangedListener(new ValidationTextWatcher(
                mAmountText, mAmountLayout, mCurrencyValidator, getString(R.string.err_msg_amount)));
        // categories recycler view
        mCategoriesView = (RecyclerView) rootView.findViewById(
                R.id.fragment_balance_change_edit_income_recycler_view_categories);
        mCategoriesManager = new LinearLayoutManager(getContext());
        mCategoriesAdapter = new RVCategoriesAdapter(mPresenter);
        mCategoriesView.setLayoutManager(mCategoriesManager);
        mCategoriesView.setAdapter(mCategoriesAdapter);
        mCategoriesView.addOnItemTouchListener(new RVItemTouchListener(
                getActivity(), mCategoriesView, new RVItemClickListener(mPresenter)));
        mCategoriesView.addItemDecoration(new RVItemDividerDecoration(getContext()));
    }

    /**
     * Fills owned views with updated data.
     */
    private void updateViews() {
        if (IntentExtras.ACTION_UPDATE.equals(mAction)
                && mBalanceChangeIndex != IntentExtras.INDEX_INVALID) {
            // TODO: use a formatter
            mAmountText.setText(String.valueOf(mPresenter.getAmount()));
        }
    }

    private static class RVCategoriesAdapter
            extends RecyclerView.Adapter<RVCategoriesAdapter.ViewHolder> {
        private final IPresenterFragmentBalanceChangeEditIncome mPresenter;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView mCategoryNameView;
            private final CheckBox mSelectedBox;

            public ViewHolder(View v) {
                super(v);
                mCategoryNameView = (TextView) v.findViewById(
                        R.id.rv_row_category_select_text_view_name);
                mSelectedBox = (CheckBox) v.findViewById(
                        R.id.rv_row_category_select_check_box_selected);
            }
        }

        public RVCategoriesAdapter(IPresenterFragmentBalanceChangeEditIncome presenter) {
            mPresenter = presenter;
            mPresenter.addCategorySelectedListener(new IDataChangeListener() {
                @Override
                public void onChange() {
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public RVCategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_row_category_select, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mCategoryNameView.setText(mPresenter.getCategoryName(position));
            holder.mSelectedBox.setChecked(mPresenter.isSelectedCategory(position));
        }

        @Override
        public int getItemCount() {
            return mPresenter.getNumCategories();
        }
    }

    private static class RVItemClickListener implements IRVItemClickListener {
        private final IPresenterFragmentBalanceChangeEditIncome mPresenter;

        public RVItemClickListener(IPresenterFragmentBalanceChangeEditIncome presenter) {
            mPresenter = presenter;
        }

        @Override
        public void onItemClick(View view, int position) {
            mPresenter.toggleCategory(position);
        }

        @Override
        public void onItemLongClick(View view, int position) {

        }
    }
}

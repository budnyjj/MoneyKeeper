package budny.moneykeeper.ui.fragments.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterFragmentAccountView;
import budny.moneykeeper.bl.presenters.impl.PresenterFragmentAccountView;
import budny.moneykeeper.db.model.BalanceChange;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.ui.misc.IntentExtras;
import budny.moneykeeper.ui.misc.RVItemDividerDecoration;
import budny.moneykeeper.ui.misc.RVItemTouchListener;
import budny.moneykeeper.ui.misc.formatters.ICurrencyFormatter;
import budny.moneykeeper.ui.misc.formatters.IDateFormatter;
import budny.moneykeeper.ui.misc.formatters.impl.CurrencyFormatter;
import budny.moneykeeper.ui.misc.formatters.impl.DateFormatter;
import budny.moneykeeper.ui.misc.listeners.IRVItemClickListener;

/**
 * A fragment used to show the details of specified account.
 * The index of the target account is specified by the arguments bundle.
 */
public class FragmentAccountView extends Fragment {
    private static final String TAG = FragmentAccountView.class.getSimpleName();

    private IPresenterFragmentAccountView mPresenter;
    @SuppressWarnings("FieldCanBeLocal")
    private int mAccountIndex = IntentExtras.INDEX_INVALID;

    private ICurrencyFormatter mTotalAmountFormatter;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView.LayoutManager mBalanceChangesManager;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView mBalanceChangesView;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView.Adapter mBalanceChangesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // parse arguments
        parseArguments();
        mPresenter = new PresenterFragmentAccountView(getContext(), mAccountIndex);
        mPresenter.addDataChangeListener(new IDataChangeListener() {
            @Override
            public void onChange() {
                updateViews();
            }
        });
        // setup owned views
        View rootView = inflater.inflate(R.layout.fragment_account_view, container, false);
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

    private void parseArguments() {
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException(
                    TAG + " is not initialized with arguments bundle");
        }
        mAccountIndex = args.getInt(IntentExtras.FIELD_INDEX, IntentExtras.INDEX_INVALID);
        if (mAccountIndex == IntentExtras.INDEX_INVALID) {
            throw new IllegalArgumentException(
                    "Unable to locate following arguments: " + IntentExtras.FIELD_INDEX);
        }
    }

    private void initViews(View rootView) {
        // amount text view
        mTotalAmountFormatter = new CurrencyFormatter(
                getContext(), (TextView) rootView.findViewById(R.id.fragment_account_view_text_view_total_amount));
        // setup recycler view
        mBalanceChangesView = (RecyclerView) rootView.findViewById(R.id.fragment_account_view_recycler_view_balance_changes);
        mBalanceChangesManager = new LinearLayoutManager(getContext());
        mBalanceChangesAdapter = new RVBalanceChangesAdapter(getContext(), mPresenter);
        mBalanceChangesView.setLayoutManager(mBalanceChangesManager);
        mBalanceChangesView.setAdapter(mBalanceChangesAdapter);
        mBalanceChangesView.addOnItemTouchListener(new RVItemTouchListener(getActivity(),
                mBalanceChangesView, new RVItemClickListener(mPresenter)));
        mBalanceChangesView.addItemDecoration(new RVItemDividerDecoration(getContext()));
    }

    /**
     * Fills owned fields with updated data.
     */
    private void updateViews() {
        mTotalAmountFormatter.format(mPresenter.getTotalAmount(), mPresenter.getCurrencyCode());
    }

    private static class RVBalanceChangesAdapter
            extends RecyclerView.Adapter<RVBalanceChangesAdapter.ViewHolder> {
        private final Context mContext;
        private final IPresenterFragmentAccountView mPresenter;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final ICurrencyFormatter mAmountFormatter;
            public final IDateFormatter mDateFormatter;
            public final TextView mCategoryView;

            public ViewHolder(Context context, View v) {
                super(v);
                mAmountFormatter = new CurrencyFormatter(
                        context, (TextView) v.findViewById(R.id.rv_row_balance_change_text_view_amount));
                mDateFormatter = new DateFormatter(
                        (TextView) v.findViewById(R.id.rv_row_balance_change_text_view_date));
                mCategoryView =
                        (TextView) v.findViewById(R.id.rv_row_balance_change_text_view_category);
            }
        }

        public RVBalanceChangesAdapter(Context context, IPresenterFragmentAccountView presenter) {
            mContext = context;
            mPresenter = presenter;
            mPresenter.addDataChangeListener(new IDataChangeListener() {
                @Override
                public void onChange() {
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public RVBalanceChangesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_row_balance_change, parent, false);
            return new ViewHolder(mContext, v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            BalanceChange change = mPresenter.getBalanceChange(position);
            holder.mAmountFormatter.format(change.getAmount(), mPresenter.getCurrencyCode());
            holder.mDateFormatter.format(change.getDate());
            holder.mCategoryView.setText(change.getCategory().getName());
        }

        @Override
        public int getItemCount() {
            return mPresenter.getNumBalanceChanges();
        }
    }

    private static class RVItemClickListener implements IRVItemClickListener {
        private final IPresenterFragmentAccountView mPresenter;

        public RVItemClickListener(IPresenterFragmentAccountView presenter) {
            mPresenter = presenter;
        }

        @Override
        public void onItemClick(View view, int position) {
            mPresenter.updateBalanceChange(position);
        }

        @Override
        public void onItemLongClick(View view, int position) {

        }
    }
}

package budny.moneykeeper.ui.fragments;

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

public class FragmentAccountView extends Fragment {
    private static final String TAG = FragmentAccountView.class.getSimpleName();

    @SuppressWarnings("FieldCanBeLocal")
    private int mAccountIdx = IntentExtras.INDEX_INVALID;
    private IPresenterFragmentAccountView mPresenter;

    private ICurrencyFormatter mTotalAmountFormatter;
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
        mAccountIdx = args.getInt(IntentExtras.FIELD_INDEX, IntentExtras.INDEX_INVALID);
        if (mAccountIdx == IntentExtras.INDEX_INVALID) {
            throw new IllegalArgumentException(
                    "Unable to locate following arguments: " + IntentExtras.FIELD_INDEX);
        }
        mPresenter = new PresenterFragmentAccountView(getContext(), mAccountIdx);
        // setup owned views
        View view = inflater.inflate(R.layout.fragment_account_view, container, false);
        mTotalAmountFormatter = new CurrencyFormatter(
                getContext(), (TextView) view.findViewById(R.id.text_view_amount_total));
        // setup recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_balance_changes);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RVBalanceChangesAdapter(getContext(), mPresenter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RVItemTouchListener(getActivity(),
                mRecyclerView, new RVItemClickListener(mPresenter)));
        mRecyclerView.addItemDecoration(new RVItemDividerDecoration(getContext()));
        // setup update listener
        mPresenter.addDataChangeListener(new IDataChangeListener() {
            @Override
            public void onChange() {
                updateFields();
            }
        });
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

    /**
     * Fills owned fields with data.
     */
    private void updateFields() {
        mTotalAmountFormatter.format(mPresenter.getTotalAmount(), mPresenter.getCurrencyCode());
    }

    private static class RVBalanceChangesAdapter
            extends RecyclerView.Adapter<RVBalanceChangesAdapter.ViewHolder> {
        private final Context mContext;
        private final IPresenterFragmentAccountView mPresenter;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final ICurrencyFormatter mAmountFormatter;
            public final IDateFormatter mDateFormatter;
            public final TextView mCategoryTextView;

            public ViewHolder(Context context, View v) {
                super(v);
                mAmountFormatter = new CurrencyFormatter(
                        context, (TextView) v.findViewById(R.id.text_view_amount));
                mDateFormatter = new DateFormatter((TextView) v.findViewById(R.id.text_view_date));
                mCategoryTextView = (TextView) v.findViewById(R.id.text_view_category);
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
                    .inflate(R.layout.rv_row_balance_change_stat, parent, false);
            return new ViewHolder(mContext, v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            BalanceChange change = mPresenter.getBalanceChange(position);
            holder.mAmountFormatter.format(change.getAmount(), mPresenter.getCurrencyCode());
            holder.mDateFormatter.format(change.getDate());
            holder.mCategoryTextView.setText(change.getCategory().getName());
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

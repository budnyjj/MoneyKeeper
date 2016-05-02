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
import budny.moneykeeper.bl.presenters.IPresenterFragmentCategories;
import budny.moneykeeper.bl.presenters.IPresenterFragmentTotal;
import budny.moneykeeper.bl.presenters.impl.PresenterFragmentTotal;
import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.ui.misc.RVItemDividerDecoration;
import budny.moneykeeper.ui.misc.RVItemTouchListener;
import budny.moneykeeper.ui.misc.formatters.ICurrencyFormatter;
import budny.moneykeeper.ui.misc.formatters.impl.CurrencyFormatter;
import budny.moneykeeper.ui.misc.listeners.IRVItemClickListener;

public class FragmentTotal extends Fragment {
    private final IPresenterFragmentTotal mPresenter = new PresenterFragmentTotal();

    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView.LayoutManager mLayoutManager;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView mRecyclerView;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView.Adapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // setup owned views
        View view = inflater.inflate(R.layout.fragment_total, container, false);
        // setup recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_accounts);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RVAccountsAdapter(getContext(), mPresenter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RVItemDividerDecoration(getContext()));
        mRecyclerView.addOnItemTouchListener(
                new RVItemTouchListener(getActivity(), mRecyclerView, new RVItemClickListener()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
        // update recycler view contents for those cases,
        // when user navigates back from corresponding edit activity
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    private static class RVAccountsAdapter
            extends RecyclerView.Adapter<RVAccountsAdapter.ViewHolder> {
        private final Context mContext;
        private final IPresenterFragmentTotal mPresenter;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final ICurrencyFormatter mAmountFormatter;
            public TextView mViewName;

            public ViewHolder(Context context, View v) {
                super(v);
                mAmountFormatter = new CurrencyFormatter(
                        context, (TextView) v.findViewById(R.id.text_view_amount));
                mViewName = (TextView) v.findViewById(R.id.text_view_name);
            }
        }

        public RVAccountsAdapter(Context context, IPresenterFragmentTotal presenter) {
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
        public RVAccountsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_row_account_stat, parent, false);
            return new ViewHolder(mContext, v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Account account = mPresenter.getAccount(position);
            holder.mAmountFormatter.format(account.getTotalAmount(), account.getCurrencyCode());
            holder.mViewName.setText(account.getName());
        }

        @Override
        public int getItemCount() {
            return mPresenter.getNumAccounts();
        }
    }

    private class RVItemClickListener implements IRVItemClickListener {
        @Override
        public void onItemClick(View view, int position) {

        }

        @Override
        public void onItemLongClick(View view, int position) {

        }
    }
}

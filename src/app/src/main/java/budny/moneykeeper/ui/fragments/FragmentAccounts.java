package budny.moneykeeper.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterFragmentAccounts;
import budny.moneykeeper.bl.presenters.impl.PresenterFragmentAccounts;
import budny.moneykeeper.db.model.Account;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.ui.misc.RVItemDividerDecoration;
import budny.moneykeeper.ui.misc.RVItemTouchListener;
import budny.moneykeeper.ui.misc.listeners.IRVItemClickListener;

public class FragmentAccounts extends Fragment {
    private IPresenterFragmentAccounts mPresenter;

    @SuppressWarnings("FieldCanBeLocal")
    private LayoutManager mLayoutManager;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView mRecyclerView;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView.Adapter mAdapter;
    @SuppressWarnings("FieldCanBeLocal")
    private ItemTouchHelper mTouchHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);
        mPresenter = new PresenterFragmentAccounts(getContext());
        // setup recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_accounts);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RVAccountsAdapter(mPresenter);
        mTouchHelper = new ItemTouchHelper(new RVTouchCallback(mPresenter));
        mTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.addOnItemTouchListener(new RVItemTouchListener(getActivity(),
                mRecyclerView, new RVItemClickListener(mPresenter)));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RVItemDividerDecoration(getContext()));
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
        private final IPresenterFragmentAccounts mPresenter;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;

            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.text_view_title);
            }
        }

        public RVAccountsAdapter(IPresenterFragmentAccounts presenter) {
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
                    .inflate(R.layout.rv_row_account, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Account account = mPresenter.getAccount(position);
            holder.mTextView.setText(account.getName());
        }

        @Override
        public int getItemCount() {
            return mPresenter.getNumAccounts();
        }
    }

    private class RVTouchCallback extends ItemTouchHelper.Callback {
        private final IPresenterFragmentAccounts mPresenter;

        public RVTouchCallback(IPresenterFragmentAccounts presenter) {
            mPresenter = presenter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            // allow swipe if there is more than one account only
            return mPresenter.getNumAccounts() > 1;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, ViewHolder source, ViewHolder target) {
            mPresenter.swapAccounts(source.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(ViewHolder holder, int direction) {
            mPresenter.deleteAccount(holder.getAdapterPosition());
        }
    }

    private static class RVItemClickListener implements IRVItemClickListener {
        private final IPresenterFragmentAccounts mPresenter;

        public RVItemClickListener(IPresenterFragmentAccounts presenter) {
            mPresenter = presenter;
        }

        @Override
        public void onItemClick(View view, int position) {
            mPresenter.updateAccount(position);
        }

        @Override
        public void onItemLongClick(View view, int position) {

        }
    }
}

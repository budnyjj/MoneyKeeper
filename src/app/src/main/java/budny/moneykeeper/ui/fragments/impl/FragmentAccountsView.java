package budny.moneykeeper.ui.fragments.impl;

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
import budny.moneykeeper.bl.presenters.IPresenterFragmentAccountsView;
import budny.moneykeeper.bl.presenters.impl.PresenterFragmentAccountsView;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.ui.misc.RVItemDividerDecoration;
import budny.moneykeeper.ui.misc.RVItemTouchListener;
import budny.moneykeeper.ui.misc.listeners.IRVItemClickListener;

/**
 * A fragment used to show all accounts.
 */
public class FragmentAccountsView extends Fragment {
    private IPresenterFragmentAccountsView mPresenter;

    @SuppressWarnings("FieldCanBeLocal")
    private LayoutManager mAccountsManager;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView mAccountsView;
    @SuppressWarnings("FieldCanBeLocal")
    private RecyclerView.Adapter mAccountsAdapter;
    @SuppressWarnings("FieldCanBeLocal")
    private ItemTouchHelper mAccountsTouchHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = new PresenterFragmentAccountsView(getContext());
        View rootView = inflater.inflate(R.layout.fragment_accounts_view, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
        // update recycler view contents for those cases,
        // when user navigates back from corresponding edit activity
        mAccountsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    private void initViews(View rootView) {
        // accounts recycler view
        mAccountsView = (RecyclerView) rootView.findViewById(R.id.fragment_accounts_view_recycler_view);
        mAccountsManager = new LinearLayoutManager(getContext());
        mAccountsAdapter = new RVAccountsAdapter(mPresenter);
        mAccountsTouchHelper = new ItemTouchHelper(new RVTouchCallback(mPresenter));
        mAccountsTouchHelper.attachToRecyclerView(mAccountsView);
        mAccountsView.addOnItemTouchListener(new RVItemTouchListener(getActivity(),
                mAccountsView, new RVItemClickListener(mPresenter)));
        mAccountsView.setLayoutManager(mAccountsManager);
        mAccountsView.setAdapter(mAccountsAdapter);
        mAccountsView.addItemDecoration(new RVItemDividerDecoration(getContext()));
    }

    private static class RVAccountsAdapter
            extends RecyclerView.Adapter<RVAccountsAdapter.ViewHolder> {
        private final IPresenterFragmentAccountsView mPresenter;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mNameView;

            public ViewHolder(View v) {
                super(v);
                mNameView = (TextView) v.findViewById(R.id.rv_row_account_text_view_name);
            }
        }

        public RVAccountsAdapter(IPresenterFragmentAccountsView presenter) {
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
            holder.mNameView.setText(mPresenter.getAccountName(position));
        }

        @Override
        public int getItemCount() {
            return mPresenter.getNumAccounts();
        }
    }

    private class RVTouchCallback extends ItemTouchHelper.Callback {
        private final IPresenterFragmentAccountsView mPresenter;

        public RVTouchCallback(IPresenterFragmentAccountsView presenter) {
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
        private final IPresenterFragmentAccountsView mPresenter;

        public RVItemClickListener(IPresenterFragmentAccountsView presenter) {
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

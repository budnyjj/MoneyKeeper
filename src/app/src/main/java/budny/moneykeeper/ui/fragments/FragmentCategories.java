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
import budny.moneykeeper.bl.presenters.IPresenterFragmentCategories;
import budny.moneykeeper.bl.presenters.impl.PresenterFragmentCategories;
import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.ui.misc.RVDividerItemDecoration;

public class FragmentCategories extends Fragment {
    private final IPresenterFragmentCategories mPresenter = new PresenterFragmentCategories();

    LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    RVAccountsAdapter mAdapter;
    ItemTouchHelper mTouchHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        // setup recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_categories);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RVAccountsAdapter(mPresenter);
        mTouchHelper = new ItemTouchHelper(new RVTouchCallback(mAdapter));
        mTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RVDividerItemDecoration(getContext()));

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

    private static class RVAccountsAdapter extends RecyclerView.Adapter<RVAccountsAdapter.ViewHolder> {
        private final IPresenterFragmentCategories mPresenter;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;

            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.item_title);
            }
        }

        public RVAccountsAdapter(IPresenterFragmentCategories presenter) {
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
                    .inflate(R.layout.rv_row_categories, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Category category = mPresenter.getCategory(position);
            holder.mTextView.setText(category.getName());
        }

        @Override
        public int getItemCount() {
            return mPresenter.getNumCategories();
        }

        public void onItemDismiss(int position) {
            mPresenter.removeCategory(position);
            notifyItemRemoved(position);
        }
    }

    private class RVTouchCallback extends ItemTouchHelper.Callback {
        private final RVAccountsAdapter mAdapter;

        public RVTouchCallback(RVAccountsAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(0, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, ViewHolder source, ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(ViewHolder viewHolder, int direction) {
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        }
    }
}

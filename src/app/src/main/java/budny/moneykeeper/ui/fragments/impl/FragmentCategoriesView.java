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
import budny.moneykeeper.bl.presenters.IPresenterFragmentCategoriesView;
import budny.moneykeeper.bl.presenters.impl.PresenterFragmentCategoriesView;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.ui.misc.RVItemDividerDecoration;
import budny.moneykeeper.ui.misc.RVItemTouchListener;
import budny.moneykeeper.ui.misc.listeners.IRVItemClickListener;

public class FragmentCategoriesView extends Fragment {
    private final IPresenterFragmentCategoriesView mPresenter = new PresenterFragmentCategoriesView();

    LayoutManager mCategoriesManager;
    RecyclerView mCategoriesView;
    RVAccountsAdapter mCategoriesAdapter;
    ItemTouchHelper mCategoriesTouchHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories_view, container, false);
        // setup recycler view
        mCategoriesView = (RecyclerView) view.findViewById(R.id.fragment_categories_view_recycler_view);
        mCategoriesManager = new LinearLayoutManager(getContext());
        mCategoriesAdapter = new RVAccountsAdapter(mPresenter);
        mCategoriesTouchHelper = new ItemTouchHelper(new RVTouchCallback(mPresenter));
        mCategoriesTouchHelper.attachToRecyclerView(mCategoriesView);
        mCategoriesView.addOnItemTouchListener(
                new RVItemTouchListener(getActivity(), mCategoriesView, new RVItemClickListener(mPresenter)));
        mCategoriesView.setLayoutManager(mCategoriesManager);
        mCategoriesView.setAdapter(mCategoriesAdapter);
        mCategoriesView.addItemDecoration(new RVItemDividerDecoration(getContext()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
        // update recycler view contents for those cases,
        // when user navigates back from corresponding edit activity
        mCategoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    private static class RVAccountsAdapter
            extends RecyclerView.Adapter<RVAccountsAdapter.ViewHolder> {
        private final IPresenterFragmentCategoriesView mPresenter;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mTextView;

            public ViewHolder(View v) {
                super(v);
                mTextView = (TextView) v.findViewById(R.id.rv_row_category_view_text_view_name);
            }
        }

        public RVAccountsAdapter(IPresenterFragmentCategoriesView presenter) {
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
            View v = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.rv_row_category_view, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mPresenter.getCategoryName(position));
        }

        @Override
        public int getItemCount() {
            return mPresenter.getNumCategories();
        }
    }

    private static class RVTouchCallback extends ItemTouchHelper.Callback {
        private final IPresenterFragmentCategoriesView mPresenter;

        public RVTouchCallback(IPresenterFragmentCategoriesView presenter) {
            mPresenter = presenter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            // allow swipe if there is more than one category only
            return mPresenter.getNumCategories() > 1;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(0, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, ViewHolder source, ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(ViewHolder viewHolder, int direction) {
            mPresenter.deleteCategory(viewHolder.getAdapterPosition());
        }
    }

    private class RVItemClickListener implements IRVItemClickListener {
        private final IPresenterFragmentCategoriesView mPresenter;

        public RVItemClickListener(IPresenterFragmentCategoriesView presenter) {
            mPresenter = presenter;
        }

        @Override
        public void onItemClick(View view, int position) {
            mPresenter.updateCategory(getActivity(), position);
        }

        @Override
        public void onItemLongClick(View view, int position) {

        }
    }
}

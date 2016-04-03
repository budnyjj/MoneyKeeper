package budny.moneykeeper.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import budny.moneykeeper.R;
import budny.moneykeeper.view.activities.ActivityCamera;
import budny.moneykeeper.view.activities.ActivityReport;
import budny.moneykeeper.view.activities.ActivityBalance;
import budny.moneykeeper.view.activities.ActivitySettings;

public class FragmentNavDrawer extends Fragment {
    private static final String TAG = FragmentNavDrawer.class.getSimpleName();
    private final List<RecyclerViewItem> mItems = new ArrayList<>();

    private Activity mParentActivity;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItems.add(new RecyclerViewItem(getResources().getString(R.string.nav_item_balance), ActivityBalance.class));
        mItems.add(new RecyclerViewItem(getResources().getString(R.string.nav_item_report), ActivityReport.class));
        mItems.add(new RecyclerViewItem(getResources().getString(R.string.nav_item_settings), ActivitySettings.class));
        mItems.add(new RecyclerViewItem(getResources().getString(R.string.nav_item_camera), ActivityCamera.class));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        mParentActivity = getActivity();
        if (!(mParentActivity instanceof ActivityContract)) {
            throw new IllegalArgumentException("Parent activity should implement the contract");
        }

        final View layout = inflater.inflate(R.layout.fragment_nav_drawer, container, false);
        mRecyclerViewAdapter = new RecyclerViewAdapter(mParentActivity, mItems);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.nav_drawer_items);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mParentActivity));
        mRecyclerView.addOnItemTouchListener(
                new RVTouchListener(mParentActivity, mRecyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        onDrawerItemSelected(view, position);
                        ((ActivityContract) mParentActivity).closeNavigationDrawer(layout);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                })
        );
        return layout;
    }

    private void onDrawerItemSelected(@NonNull View view, int position) {
        // start requested activity only if it's different from the current one
        Class<? extends Activity> reqActivityCls = mItems.get(position).getActivityClass();
        if (!reqActivityCls.equals(mParentActivity.getClass())) {
            Intent intent = new Intent(mParentActivity, reqActivityCls);
            startActivity(intent);
        }
    }

    private static class RecyclerViewItem {
        private final String mTitle;
        private final Class<? extends Activity> mActivityCls;

        public RecyclerViewItem(@NonNull String title, @NonNull Class<? extends Activity> activityCls) {
            mTitle = title;
            mActivityCls = activityCls;
        }

        public String getTitle() {
            return mTitle;
        }

        public Class<? extends Activity> getActivityClass() {
            return mActivityCls;
        }
    }

    private static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private final LayoutInflater mInflater;
        private final List<RecyclerViewItem> mItems;

        public RecyclerViewAdapter(Context context, List<RecyclerViewItem> items) {
            mInflater = LayoutInflater.from(context);
            mItems = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.nav_drawer_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            RecyclerViewItem item = mItems.get(position);
            holder.setTitle(item.getTitle());
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mTitle;

            private ViewHolder(View v) {
                super(v);
                mTitle = (TextView) v.findViewById(R.id.nav_drawer_row);
            }

            private void setTitle(String title) {
                mTitle.setText(title);
            }
        }
    }

    private static class RVTouchListener implements RecyclerView.OnItemTouchListener {
        private final GestureDetector mGestureDetector;
        private final ClickListener mClickListener;

        public RVTouchListener(@NonNull Context context,
                               @NonNull final RecyclerView recyclerView,
                               @NonNull final ClickListener clickListener) {
            mClickListener = clickListener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && mGestureDetector.onTouchEvent(e)) {
                mClickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ActivityContract {
        void closeNavigationDrawer(View drawerView);
    }

    private interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }
}
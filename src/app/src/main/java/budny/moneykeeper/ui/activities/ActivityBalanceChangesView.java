package budny.moneykeeper.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterActivityBalanceChangesView;
import budny.moneykeeper.bl.presenters.impl.PresenterActivityBalanceChangesView;
import budny.moneykeeper.db.util.IDataChangeListener;

/**
 * An activity used to view the list of balance changes of all accounts.
 */
public class ActivityBalanceChangesView extends AppCompatActivity {
    private final IPresenterActivityBalanceChangesView mPresenter = new PresenterActivityBalanceChangesView(this);

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    @SuppressWarnings("FieldCanBeLocal")
    private FloatingActionButton mFab;
    @SuppressWarnings("FieldCanBeLocal")
    private TabLayout mTabs;
    private Toolbar mToolbar;
    @SuppressWarnings("FieldCanBeLocal")
    private ViewPager mPagerView;
    @SuppressWarnings("FieldCanBeLocal")
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate();
        setContentView(R.layout.activity_balance_changes_view);
        initViews();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.balance, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_balance_item_group_by_categories) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        // toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDefaultDisplayHomeAsUpEnabled(true);
        }
        // view pager
        mTabs = (TabLayout) findViewById(R.id.activity_balance_changes_view_tabs);
        mPagerView = (ViewPager) findViewById(R.id.activity_balance_changes_view_view_pager);
        mPagerAdapter = new AdapterViewPager(getSupportFragmentManager(), mTabs, mPresenter);
        mPagerView.setAdapter(mPagerAdapter);
        mTabs.setupWithViewPager(mPagerView);
        // floating action button
        mFab = (FloatingActionButton) findViewById(R.id.activity_balance_changes_view_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.createBalanceChange();
            }
        });
        // navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_balance_changes_view_nav_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mToolbar.setAlpha(1 - slideOffset / 2);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mNavigationView = (NavigationView) findViewById(R.id.activity_balance_changes_view_navigation_view);
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        Class<? extends Activity> selectedActivityCls = null;
                        switch (item.getItemId()) {
                            case R.id.menu_nav_drawer_item_accounts:
                                selectedActivityCls = ActivityAccountsView.class;
                                break;
                            case R.id.menu_nav_drawer_item_categories:
                                selectedActivityCls = ActivityCategoriesView.class;
                                break;
                            case R.id.menu_nav_drawer_item_settings:
                                selectedActivityCls = ActivityPreferences.class;
                                break;
                            case R.id.menu_nav_drawer_item_camera:
                                selectedActivityCls = ActivityCamera.class;
                                break;
                        }
                        mDrawerLayout.closeDrawer(mNavigationView);
                        if (selectedActivityCls != null) {
                            Intent intent = new Intent(getBaseContext(), selectedActivityCls);
                            startActivity(intent);
                        }
                        return true;
                    }
                });
    }

    private static class AdapterViewPager extends FragmentStatePagerAdapter {
        private final IPresenterActivityBalanceChangesView mPresenter;

        private final TabLayout mLayout;

        public AdapterViewPager(@NonNull FragmentManager manager,
                                TabLayout layout, IPresenterActivityBalanceChangesView presenter) {
            super(manager);
            mLayout = layout;
            mPresenter = presenter;
            mPresenter.addDataChangeListener(new IDataChangeListener() {
                @Override
                public void onChange() {
                    if (mPresenter.getNumAccounts() > 1) {
                        mLayout.setVisibility(View.VISIBLE);
                    } else {
                        mLayout.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemPosition(Object object) {
            // this is needed to perform full redraw when the dataset is updated
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return mPresenter.getFragmentAccountView(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPresenter.getFragmentAccountViewName(position);
        }

        @Override
        public int getCount() {
            return mPresenter.getNumAccounts();
        }
    }
}
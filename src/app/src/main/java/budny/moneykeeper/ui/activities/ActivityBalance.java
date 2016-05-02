package budny.moneykeeper.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import budny.moneykeeper.bl.presenters.IPresenterActivityBalance;
import budny.moneykeeper.bl.presenters.impl.PresenterActivityBalance;
import budny.moneykeeper.db.util.IDataChangeListener;

public class ActivityBalance extends AppCompatActivity {
    private final IPresenterActivityBalance mPresenter = new PresenterActivityBalance(this);

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    @SuppressWarnings("FieldCanBeLocal")
    private FloatingActionButton mFab;
    @SuppressWarnings("FieldCanBeLocal")
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    @SuppressWarnings("FieldCanBeLocal")
    private ViewPager mPagerView;
    @SuppressWarnings("FieldCanBeLocal")
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        // initialize presenter
        mPresenter.onCreate();

        // setup views
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setupActionBar(getSupportActionBar());

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mPagerView = (ViewPager) findViewById(R.id.viewpager);
        mPagerAdapter = new AdapterViewPager(getSupportFragmentManager(), mTabLayout, mPresenter);
        mPagerView.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mPagerView);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityBalance.this, ActivityInput.class);
                startActivity(intent);
            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.fragment_container_balance_nav_drawer);
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

        mNavigationView = (NavigationView) findViewById(R.id.balance_navigation);
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        Class<? extends Activity> selectedActivityCls = null;
                        switch (item.getItemId()) {
                            case R.id.menu_nav_drawer_item_accounts:
                                selectedActivityCls = ActivityAccounts.class;
                                break;
                            case R.id.menu_nav_drawer_item_categories:
                                selectedActivityCls = ActivityCategories.class;
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
        if (id == R.id.menu_balance_item_group_categories) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar(@Nullable ActionBar bar) {
        if (bar != null) {
            bar.setDefaultDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * An adapter for view pager.
     *
     * If there is only one account, it provides a fragment for it.
     * It there is more than one account, it provides a fragment with total values
     * and number of fragments per each account.
     */
    private static class AdapterViewPager extends FragmentStatePagerAdapter {
        private final IPresenterActivityBalance mPresenter;

        private final TabLayout mTabLayout;

        public AdapterViewPager(@NonNull FragmentManager manager,
                                TabLayout layout, IPresenterActivityBalance presenter) {
            super(manager);
            mTabLayout = layout;
            mPresenter = presenter;
            mPresenter.addDataChangeListener(new IDataChangeListener() {
                @Override
                public void onChange() {
                    if (mPresenter.getNumAccounts() > 1) {
                        mTabLayout.setVisibility(View.VISIBLE);
                    } else {
                        mTabLayout.setVisibility(View.GONE);
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
            int numAccounts = mPresenter.getNumAccounts();
            if (numAccounts > 1) {
                if (position > 0) {
                    return mPresenter.getFragmentAccountView(position - 1);
                }
                return mPresenter.getFragmentTotal();
            } else {
                return mPresenter.getFragmentAccountView(position);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int numAccounts = mPresenter.getNumAccounts();
            if (numAccounts > 1) {
                if (position > 0) {
                    return mPresenter.getFragmentAccountViewName(position - 1);
                }
                return mPresenter.getFragmentTotalName();
            } else {
                return mPresenter.getFragmentAccountViewName(position);
            }
        }

        @Override
        public int getCount() {
            int numAccounts = mPresenter.getNumAccounts();
            if (numAccounts > 1) {
                return numAccounts + 1;
            }
            return numAccounts;
        }
    }
}
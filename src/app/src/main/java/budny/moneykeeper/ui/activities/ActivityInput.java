package budny.moneykeeper.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterActivityBalance;
import budny.moneykeeper.bl.presenters.IPresenterActivityInput;
import budny.moneykeeper.bl.presenters.impl.PresenterActivityInput;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.ui.fragments.FragmentAccountEdit;
import budny.moneykeeper.ui.misc.listeners.IContentEditListener;

public class ActivityInput extends AppCompatActivity {
    private IContentEditListener mEditListener;

    private IPresenterActivityInput mPresenter;

    @SuppressWarnings("FieldCanBeLocal")
    private Toolbar mToolbar;
    @SuppressWarnings("FieldCanBeLocal")
    private TabLayout mTabLayout;
    @SuppressWarnings("FieldCanBeLocal")
    private ViewPager mPagerView;
    @SuppressWarnings("FieldCanBeLocal")
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        // pass argument bundle to presenter
        mPresenter = new PresenterActivityInput(this, getIntent().getExtras());
        // setup owned views
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setupActionBar(getSupportActionBar());
        // setup view pager
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mPagerView = (ViewPager) findViewById(R.id.viewpager);
        mPagerAdapter = new AdapterViewPager(getSupportFragmentManager(), mPresenter);
        mPagerView.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mPagerView);
//        // pass activity's arguments to the fragment
//        FragmentAccountEdit fragment = new FragmentAccountEdit();
//        fragment.setArguments(getIntent().getExtras());
//        mEditListener = fragment;
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.fragment_container_account_edit_content, fragment)
//                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_input_item_save: {
                if (mEditListener.onEditContent()) {
                    onBackPressed();
                }
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void setupActionBar(@Nullable ActionBar bar) {
        if (bar != null) {
            bar.setDefaultDisplayHomeAsUpEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private static class AdapterViewPager extends FragmentStatePagerAdapter {
        private final IPresenterActivityInput mPresenter;

        public AdapterViewPager(@NonNull FragmentManager manager, IPresenterActivityInput presenter) {
            super(manager);
            mPresenter = presenter;
        }

        @Override
        public int getItemPosition(Object object) {
            // this is needed to perform full redraw when the dataset is updated
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return mPresenter.getFragmentInput(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPresenter.getFragmentInputName(position);
        }

        @Override
        public int getCount() {
            return mPresenter.getNumInputTypes();
        }
    }
}

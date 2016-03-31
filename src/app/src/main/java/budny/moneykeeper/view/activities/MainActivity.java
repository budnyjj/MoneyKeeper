package budny.moneykeeper.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import budny.moneykeeper.R;
import budny.moneykeeper.view.fragments.AccountFragment;
import budny.moneykeeper.view.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

//    /**
//     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
//     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
//     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
//     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
//     */
//    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    //    /**
//     * The {@link ViewPager} that will display the three primary sections of the app, one at a
//     * time.
//     */
    Toolbar mToolBar;
    ViewPager mViewPager;
    TabLayout mTabLayout;
    FloatingActionButton mFab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InputActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupViewPager(ViewPager pager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), "Main");
        adapter.addFragment(new AccountFragment(), "Account 1");
        adapter.addFragment(new AccountFragment(), "Account 2");
        adapter.addFragment(new AccountFragment(), "Account 3");
        adapter.addFragment(new AccountFragment(), "Account 4");
        adapter.addFragment(new AccountFragment(), "Account 5");
        adapter.addFragment(new AccountFragment(), "Account 6");
        adapter.addFragment(new AccountFragment(), "Account 7");
        adapter.addFragment(new AccountFragment(), "Account 8");
        adapter.addFragment(new AccountFragment(), "Account 9");
        adapter.addFragment(new AccountFragment(), "Account 10");
        mViewPager.setAdapter(adapter);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }
}

//        // Create the adapter that will return a fragment for each of the three primary sections
//        // of the app.
//        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
//
//        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
//        // parent.
//        getSupportActionBar().setHomeButtonEnabled(false);
//
//        // Specify that we will be displaying tabs in the action bar.
//        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//
//        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
//        // user swipes between sections.
//        mViewPager = (ViewPager) findViewById(R.id.pager);
//        mViewPager.setAdapter(mAppSectionsPagerAdapter);
//        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                // When swiping between different app sections, select the corresponding tab.
//                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
//                // Tab.
//                getSupportActionBar().setSelectedNavigationItem(position);
//            }
//        });
//
//        // For each of the sections in the app, add a tab to the action bar.
//        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
//            // Create a tab with text corresponding to the page title defined by the adapter.
//            // Also specify this Activity object, which implements the TabListener interface, as the
//            // listener for when this tab is selected.
//            getSupportActionBar().addTab(
//                    getSupportActionBar().newTab()
//                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
//                            .setTabListener(this));
//        }
//    }

//    @Override
//    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//    }
//
//    @Override
//    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//        // When the given tab is selected, switch to the corresponding page in the ViewPager.
//        mViewPager.setCurrentItem(tab.getPosition());
//    }
//
//    @Override
//    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//    }
//
//    /**
//     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
//     * sections of the app.
//     */
//    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
//
//        public AppSectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int i) {
//            switch (i) {
//                case 0:
//                    // The first section of the app is the most interesting -- it offers
//                    // a launchpad into the other demonstrations in this example application.
//                    return new LaunchpadSectionFragment();
//
//                default:
//                    // The other sections of the app are dummy placeholders.
//                    Fragment fragment = new DummySectionFragment();
//                    Bundle args = new Bundle();
//                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
//                    fragment.setArguments(args);
//                    return fragment;
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return "Section " + (position + 1);
//        }
//    }
//
//    /**
//     * A fragment that launches other parts of the demo application.
//     */
//    public static class LaunchpadSectionFragment extends Fragment {
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
//
//            // Demonstration of a collection-browsing activity.
//            rootView.findViewById(R.id.demo_collection_button)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent intent = new Intent(getActivity(), CollectionDemoActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//
//            // Demonstration of navigating to external activities.
//            rootView.findViewById(R.id.demo_external_activity)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            // Create an intent that asks the user to pick a photo, but using
//                            // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
//                            // the application from the device home screen does not return
//                            // to the external activity.
//                            Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
//                            externalActivityIntent.setType("image/*");
//                            externalActivityIntent.addFlags(
//                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                            startActivity(externalActivityIntent);
//                        }
//                    });
//
//            return rootView;
//        }
//    }
//
//    /**
//     * A dummy fragment representing a section of the app, but that simply displays dummy text.
//     */
//    public static class DummySectionFragment extends Fragment {
//
//        public static final String ARG_SECTION_NUMBER = "section_number";
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
//            Bundle args = getArguments();
//            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
//                    getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }

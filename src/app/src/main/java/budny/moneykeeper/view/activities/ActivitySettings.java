package budny.moneykeeper.view.activities;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import budny.moneykeeper.R;
import budny.moneykeeper.presenter.PresenterMainPreferences;
import budny.moneykeeper.view.fragments.FragmentNavDrawer;

public class ActivitySettings extends AppCompatActivity
        implements FragmentNavDrawer.ActivityContract {
    private static final String TAG = ActivitySettings.class.getSimpleName();

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.fragment_container_settings_nav_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.nav_drawer_open, R.string.nav_drawer_close) {
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

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_settings, new FragmentMainPreferences())
                .commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void closeNavigationDrawer(View drawerView) {
        mDrawerLayout.closeDrawer(drawerView);
    }

    public static class FragmentMainPreferences extends PreferenceFragment {
        private PresenterMainPreferences mPresenter;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_main);

            mPresenter = PresenterMainPreferences.getInstance();

            final String defaultCurrencyKey =
                    getResources().getString(R.string.pref_key_default_currency);
            final ListPreference defaultCurrencyPreference =
                    (ListPreference) findPreference(defaultCurrencyKey);
            defaultCurrencyPreference.setEntries(mPresenter.getCurrencyNames());
            defaultCurrencyPreference.setEntryValues(mPresenter.getCurrencyCodes());
            defaultCurrencyPreference.setDefaultValue(mPresenter.getDefaultCurrencyCode());
        }
    }

//    public static class Prefs2Fragment extends PreferenceFragment {
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            addPreferencesFromResource(R.xml.preferences_dependencies);
//        }
//    }
}

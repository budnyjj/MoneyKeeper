package budny.moneykeeper.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterActivityAccounts;
import budny.moneykeeper.bl.presenters.impl.PresenterActivityAccounts;
import budny.moneykeeper.ui.fragments.FragmentAccounts;

public class ActivityAccounts extends AppCompatActivity {
    @SuppressWarnings("unused")
    private static final String TAG = ActivityAccounts.class.getSimpleName();

    private final IPresenterActivityAccounts mPresenter = new PresenterActivityAccounts(this);

    @SuppressWarnings("FieldCanBeLocal")
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setupActionBar(getSupportActionBar());

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_accounts_content, new FragmentAccounts())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.accounts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_accounts_item_add: {
                mPresenter.createAccount();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar(@Nullable ActionBar bar) {
        if (bar != null) {
            bar.setDefaultDisplayHomeAsUpEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }
}

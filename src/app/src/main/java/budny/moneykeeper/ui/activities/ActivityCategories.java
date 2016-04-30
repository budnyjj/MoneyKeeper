package budny.moneykeeper.ui.activities;

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
import budny.moneykeeper.bl.presenters.IPresenterActivityCategories;
import budny.moneykeeper.bl.presenters.impl.PresenterActivityCategories;
import budny.moneykeeper.ui.fragments.FragmentCategories;

public class ActivityCategories extends AppCompatActivity {
    @SuppressWarnings("unused")
    private static final String TAG = ActivityCategories.class.getSimpleName();

    private final IPresenterActivityCategories mPresenter = new PresenterActivityCategories(this);

    @SuppressWarnings("FieldCanBeLocal")
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

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
                .add(R.id.fragment_container_categories_content, new FragmentCategories())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_categories_item_add) {
            mPresenter.createCategory();
            return true;
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

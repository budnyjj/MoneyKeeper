package budny.moneykeeper.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterActivityCategories;
import budny.moneykeeper.bl.presenters.impl.PresenterActivityCategories;
import budny.moneykeeper.ui.fragments.impl.FragmentCategoriesView;

/**
 * An activity used to view the list of categories.
 */
public class ActivityCategoriesView extends AppCompatActivity {
    @SuppressWarnings("unused")
    private static final String TAG = ActivityCategoriesView.class.getSimpleName();

    private final IPresenterActivityCategories mPresenter = new PresenterActivityCategories(this);

    @SuppressWarnings("FieldCanBeLocal")
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_view);
        setupViews();
        setupFragments();
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

    private void setupViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDefaultDisplayHomeAsUpEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupFragments() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_categories_view_fragment_container, new FragmentCategoriesView())
                .commit();
    }
}

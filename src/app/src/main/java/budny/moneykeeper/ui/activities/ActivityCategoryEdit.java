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
import budny.moneykeeper.ui.fragments.impl.FragmentCategoryEdit;
import budny.moneykeeper.ui.misc.listeners.IContentEditListener;

/**
 * An activity used to edit specified category.
 * Edit action type as well as index of account are specified by arguments bundle.
 */
public class ActivityCategoryEdit extends AppCompatActivity {
    private IContentEditListener mEditListener;

    @SuppressWarnings("FieldCanBeLocal")
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_edit);
        initViews();
        initFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.category_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_category_edit_item_save:
                if (mEditListener.onEditContent()) {
                    onBackPressed();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        // toolbar
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

    private void initFragments() {
        // pass activity's arguments to the fragment
        FragmentCategoryEdit fragment = new FragmentCategoryEdit();
        fragment.setArguments(getIntent().getExtras());
        mEditListener = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_category_edit_fragment_container, fragment)
                .commit();
    }
}

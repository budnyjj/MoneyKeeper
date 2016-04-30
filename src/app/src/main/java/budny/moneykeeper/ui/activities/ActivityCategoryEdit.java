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
import budny.moneykeeper.ui.fragments.FragmentCategoryEdit;
import budny.moneykeeper.ui.misc.listeners.IContentEditListener;

public class ActivityCategoryEdit extends AppCompatActivity {
    private IContentEditListener mEditListener;

    @SuppressWarnings("FieldCanBeLocal")
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_edit);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setupActionBar(getSupportActionBar());

        FragmentCategoryEdit fragment = new FragmentCategoryEdit();
        // pass activity's arguments to the fragment
        fragment.setArguments(getIntent().getExtras());
        mEditListener = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_category_edit_content, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.category_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_category_edit_item_save: {
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
}

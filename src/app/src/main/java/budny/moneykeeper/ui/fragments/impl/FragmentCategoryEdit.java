package budny.moneykeeper.ui.fragments.impl;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterFragmentCategoryEdit;
import budny.moneykeeper.bl.presenters.impl.PresenterFragmentCategoryEdit;
import budny.moneykeeper.bl.validators.IValidator;
import budny.moneykeeper.bl.validators.impl.TextValidator;
import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.ui.fragments.IFragmentEdit;
import budny.moneykeeper.ui.misc.IntentExtras;
import budny.moneykeeper.ui.misc.ValidationTextWatcher;

/**
 * A fragment used to edit specified category.
 * Concrete action (add/modify) as well as index of the
 * target category are specified by the arguments bundle.
 */
public class FragmentCategoryEdit extends IFragmentEdit {
    private static final String TAG = FragmentCategoryEdit.class.getSimpleName();

    private final IValidator mTextValidator = new TextValidator();

    private IPresenterFragmentCategoryEdit mPresenter;
    // action to perform with category (create or update)
    private String mAction = IntentExtras.ACTION_INVALID;
    // index of category to edit
    private int mCategoryIndex = IntentExtras.INDEX_INVALID;

    @SuppressWarnings("FieldCanBeLocal")
    private TextInputLayout mCategoryNameLayout;
    private EditText mCategoryNameText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parseArguments();
        mPresenter = new PresenterFragmentCategoryEdit(mAction, mCategoryIndex);
        // setup owned views
        View rootView = inflater.inflate(R.layout.fragment_category_edit, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
        updateViews();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public boolean onEditContent() {
        String categoryName = mCategoryNameText.getText().toString().trim();
        if (!mTextValidator.validate(categoryName)) {
            mCategoryNameLayout.setError(getString(R.string.err_msg_category_name));
            return false;
        }

        switch (mAction) {
            case IntentExtras.ACTION_CREATE:
                mPresenter.createCategory(categoryName);
                break;
            case IntentExtras.ACTION_UPDATE:
                mPresenter.updateCategory(categoryName);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Fills owned views with data.
     */
    private void updateViews() {
        if (IntentExtras.ACTION_UPDATE.equals(mAction)
                && mCategoryIndex != IntentExtras.INDEX_INVALID) {
            Category category = mPresenter.getCategory();
            mCategoryNameText.setText(category.getName());
        }
    }

    private void parseArguments() {
        // parse arguments
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException(
                    TAG + " is not initialized with arguments bundle");
        }
        String action = args.getString(IntentExtras.FIELD_ACTION);
        mAction = (action == null) ? IntentExtras.ACTION_INVALID : action;
        if (IntentExtras.ACTION_INVALID.equals(mAction)) {
            throw new IllegalArgumentException(
                    "Unable to locate following arguments: " + IntentExtras.FIELD_ACTION);
        }
        if (IntentExtras.ACTION_UPDATE.equals(mAction)) {
            mCategoryIndex = args.getInt(IntentExtras.FIELD_INDEX, IntentExtras.INDEX_INVALID);
            if (mCategoryIndex == IntentExtras.INDEX_INVALID) {
                throw new IllegalArgumentException(
                        "Unable to locate following arguments: " + IntentExtras.FIELD_INDEX);
            }
        }
    }

    private void initViews(View rootView) {
        mCategoryNameLayout = (TextInputLayout) rootView.findViewById(
                R.id.fragment_category_edit_edit_text_container_name);
        mCategoryNameText = (EditText) rootView.findViewById(
                R.id.fragment_category_edit_edit_text_name);
        mCategoryNameText.addTextChangedListener(
                new ValidationTextWatcher(
                        mCategoryNameText, mCategoryNameLayout,
                        mTextValidator, getString(R.string.err_msg_category_name)));
    }
}

package budny.moneykeeper.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
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
import budny.moneykeeper.ui.misc.IntentExtras;
import budny.moneykeeper.ui.misc.ValidationTextWatcher;
import budny.moneykeeper.ui.misc.listeners.IContentEditListener;

public class FragmentCategoryEdit extends Fragment
        implements IContentEditListener {
    // action to perform with category (create or update)
    private String mAction = IntentExtras.ACTION_INVALID;
    // index of category to edit
    private int mCategoryIdx = IntentExtras.INDEX_INVALID;
    private IPresenterFragmentCategoryEdit mPresenter = new PresenterFragmentCategoryEdit();
    private IValidator mTextValidator = new TextValidator();
    private String mErrorMsgCategoryName;


    @SuppressWarnings("FieldCanBeLocal")
    private TextInputLayout mCategoryNameContainer;
    private EditText mCategoryNameField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // parse arguments
        Bundle args = getArguments();
        if (args != null) {
            String action = args.getString(IntentExtras.FIELD_ACTION);
            mAction = (action == null) ? IntentExtras.ACTION_INVALID : action;
            mCategoryIdx = args.getInt(IntentExtras.FIELD_INDEX, IntentExtras.INDEX_INVALID);
        }
        // setup owned views
        View view = inflater.inflate(R.layout.fragment_category_edit, container, false);
        mErrorMsgCategoryName = getString(R.string.err_msg_category_name);
        mCategoryNameContainer = (TextInputLayout) view.findViewById(R.id.input_container_category_edit_name);
        mCategoryNameField = (EditText) view.findViewById(R.id.category_edit_name);
        mCategoryNameField.addTextChangedListener(
                new ValidationTextWatcher(
                        mCategoryNameField, mCategoryNameContainer,
                        mTextValidator, mErrorMsgCategoryName));
        return view;
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
        String categoryName = mCategoryNameField.getText().toString().trim();
        if (!mTextValidator.validate(categoryName)) {
            mCategoryNameContainer.setError(mErrorMsgCategoryName);
            return false;
        }

        Category category = new Category();
        category.setName(categoryName);

        switch (mAction) {
            case IntentExtras.ACTION_CREATE:
                mPresenter.createCategory(category);
                break;
            case IntentExtras.ACTION_UPDATE:
                mPresenter.updateCategory(category, mCategoryIdx);
                break;
            default:
        }
        return true;
    }

    /**
     * Fills owned views with data.
     */
    private void updateViews() {
        if (IntentExtras.ACTION_UPDATE.equals(mAction)
                && mCategoryIdx != IntentExtras.INDEX_INVALID) {
            Category category = mPresenter.getCategory(mCategoryIdx);
            mCategoryNameField.setText(category.getName());
        }
    }
}

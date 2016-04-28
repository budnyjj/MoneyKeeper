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
import budny.moneykeeper.ui.misc.ValidationTextWatcher;
import budny.moneykeeper.ui.misc.listeners.ISaveContentListener;

public class FragmentCategoryEdit extends Fragment
        implements ISaveContentListener {
    private String mErrorMsgCategoryName;

    private IPresenterFragmentCategoryEdit mPresenter = new PresenterFragmentCategoryEdit();
    private IValidator mTextValidator = new TextValidator();

    @SuppressWarnings("FieldCanBeLocal")
    private TextInputLayout mCategoryNameContainer;
    private EditText mCategoryNameField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public boolean onSaveContent() {
        String categoryName = mCategoryNameField.getText().toString().trim();
        if (!mTextValidator.validate(categoryName)) {
            mCategoryNameContainer.setError(mErrorMsgCategoryName);
            return false;
        }

        Category category = new Category();
        category.setName(categoryName);
        mPresenter.addCategory(category);
        return true;
    }
}

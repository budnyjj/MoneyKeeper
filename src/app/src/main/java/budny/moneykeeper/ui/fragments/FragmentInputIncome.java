package budny.moneykeeper.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import budny.moneykeeper.R;
import budny.moneykeeper.ui.misc.IntentExtras;
import budny.moneykeeper.ui.misc.listeners.IContentEditListener;

public class FragmentInputIncome extends Fragment implements IContentEditListener {
    private static final String TAG = FragmentCategoryEdit.class.getSimpleName();

    //    private final IPresenterFragmentCategoryEdit mPresenter = new PresenterFragmentCategoryEdit();
//    private final IValidator mTextValidator = new TextValidator();
//
    // action to perform with category (create or update)
    private String mAction = IntentExtras.ACTION_INVALID;
    // index of category to edit
    private int mBalanceChangeIdx = IntentExtras.INDEX_INVALID;
//    private String mErrorMsgCategoryName;
//
//    @SuppressWarnings("FieldCanBeLocal")
//    private TextInputLayout mCategoryNameContainer;
//    private EditText mCategoryNameField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
            mBalanceChangeIdx = args.getInt(IntentExtras.FIELD_INDEX, IntentExtras.INDEX_INVALID);
            if (mBalanceChangeIdx == IntentExtras.INDEX_INVALID) {
                throw new IllegalArgumentException(
                        "Unable to locate following arguments: " + IntentExtras.FIELD_INDEX);
            }
        }
        // setup owned views
        View view = inflater.inflate(R.layout.fragment_input_income, container, false);
//        mErrorMsgCategoryName = getString(R.string.err_msg_category_name);
//        mCategoryNameContainer = (TextInputLayout) view.findViewById(R.id.input_container_category_edit_name);
//        mCategoryNameField = (EditText) view.findViewById(R.id.category_edit_name);
//        mCategoryNameField.addTextChangedListener(
//                new ValidationTextWatcher(
//                        mCategoryNameField, mCategoryNameContainer,
//                        mTextValidator, mErrorMsgCategoryName));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        mPresenter.onStart();
        updateViews();
    }

    @Override
    public void onStop() {
        super.onStop();
//        mPresenter.onStop();
    }

    @Override
    public boolean onEditContent() {
//        String categoryName = mCategoryNameField.getText().toString().trim();
//        if (!mTextValidator.validate(categoryName)) {
//            mCategoryNameContainer.setError(mErrorMsgCategoryName);
//            return false;
//        }

//        Category category = new Category();
//        category.setName(categoryName);

//        switch (mAction) {
//            case IntentExtras.ACTION_CREATE:
//                mPresenter.createCategory(category);
//                break;
//            case IntentExtras.ACTION_UPDATE:
//                mPresenter.updateCategory(category, mBalanceChangeIdx);
//                break;
//            default:
//                break;
//        }
        return true;
    }

    /**
     * Fills owned views with data.
     */
    private void updateViews() {
//        if (IntentExtras.ACTION_UPDATE.equals(mAction)
//                && mBalanceChangeIdx != IntentExtras.INDEX_INVALID) {
//            Category category = mPresenter.getCategory(mBalanceChangeIdx);
//            mCategoryNameField.setText(category.getName());
//        }
    }
}

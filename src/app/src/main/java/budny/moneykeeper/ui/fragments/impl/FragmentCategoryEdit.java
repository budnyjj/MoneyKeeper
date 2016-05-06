package budny.moneykeeper.ui.fragments.impl;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Map;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterFragmentCategoryEdit;
import budny.moneykeeper.bl.presenters.impl.PresenterFragmentCategoryEdit;
import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.ui.fragments.IFragmentEdit;
import budny.moneykeeper.ui.misc.IntentExtras;
import budny.moneykeeper.ui.misc.managers.IEditManager;
import budny.moneykeeper.ui.misc.managers.impl.EditManagerRadioGroup;
import budny.moneykeeper.ui.misc.managers.impl.EditManagerText;

/**
 * A fragment used to edit specified category.
 * Concrete action (add/modify) as well as index of the
 * target category are specified by the arguments bundle.
 */
public class FragmentCategoryEdit extends IFragmentEdit {
    private static final String TAG = FragmentCategoryEdit.class.getSimpleName();
    private static final String MSG_NOT_INITIALIZED = TAG + " is not initialized with arguments bundle";
    private static final String MSG_NO_ARGS = "Unable to locate following arguments: ";

    private IPresenterFragmentCategoryEdit mPresenter;
    // action to perform with category (create or update)
    private String mAction = IntentExtras.ACTION_INVALID;
    // index of category to edit
    private int mCategoryIndex = IntentExtras.INDEX_INVALID;

    private IEditManager<String> mCategoryNameEditManager;
    private IEditManager<Category.Type> mCategoryTypeEditManager;

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
        if (!mCategoryNameEditManager.isValid()
                || !mCategoryTypeEditManager.isValid()) {
            return false;
        }
        switch (mAction) {
            case IntentExtras.ACTION_CREATE:
                mPresenter.createCategory(
                        mCategoryNameEditManager.getValue(), mCategoryTypeEditManager.getValue());
                break;
            case IntentExtras.ACTION_UPDATE:
                mPresenter.updateCategory(
                        mCategoryNameEditManager.getValue(), mCategoryTypeEditManager.getValue());
                break;
            default:
                break;
        }
        return true;
    }

    private void parseArguments() {
        // parse arguments
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException(MSG_NOT_INITIALIZED);
        }
        String action = args.getString(IntentExtras.FIELD_ACTION);
        mAction = (action == null) ? IntentExtras.ACTION_INVALID : action;
        if (IntentExtras.ACTION_INVALID.equals(mAction)) {
            throw new IllegalArgumentException(MSG_NO_ARGS + IntentExtras.FIELD_ACTION);
        }
        if (IntentExtras.ACTION_UPDATE.equals(mAction)) {
            mCategoryIndex = args.getInt(IntentExtras.FIELD_INDEX_CATEGORY, IntentExtras.INDEX_INVALID);
            if (mCategoryIndex == IntentExtras.INDEX_INVALID) {
                throw new IllegalArgumentException(MSG_NO_ARGS + IntentExtras.FIELD_INDEX_CATEGORY);
            }
        }
    }

    private void initViews(View rootView) {
        mCategoryNameEditManager = new EditManagerText(
                (EditText) rootView.findViewById(R.id.fragment_category_edit_edit_text_name),
                (TextInputLayout) rootView.findViewById(R.id.fragment_category_edit_edit_text_container_name),
                getString(R.string.err_msg_category_name), true);
        // setup category type radio group
        Map<Integer, Category.Type> uiTypeMap = new HashMap<>();
        uiTypeMap.put(R.id.fragment_category_edit_radio_button_income, Category.Type.INCOME);
        uiTypeMap.put(R.id.fragment_category_edit_radio_button_outcome, Category.Type.OUTCOME);
        mCategoryTypeEditManager = new EditManagerRadioGroup<>(
                (RadioGroup) rootView.findViewById(R.id.fragment_category_edit_radio_group_type),
                uiTypeMap);
    }

    /**
     * Fills owned views with data.
     */
    private void updateViews() {
        if (IntentExtras.ACTION_UPDATE.equals(mAction)
                && mCategoryIndex != IntentExtras.INDEX_INVALID) {
            mCategoryNameEditManager.setValue(mPresenter.getCategoryName());
            mCategoryTypeEditManager.setValue(mPresenter.getCategoryType());
        }
    }
}

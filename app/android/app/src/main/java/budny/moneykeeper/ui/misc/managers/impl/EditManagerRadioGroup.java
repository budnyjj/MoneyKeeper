package budny.moneykeeper.ui.misc.managers.impl;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Map;

import budny.moneykeeper.ui.misc.managers.IEditManager;

public class EditManagerRadioGroup<T extends Enum> implements IEditManager<T> {
    @SuppressWarnings("FieldCanBeLocal")
    private final RadioGroup mCategoryTypeGroup;
    private final Map<Integer, T> mUiTypeMap = new HashMap<>();
    private final Map<T, Integer> mUiTypeMapInv = new HashMap<>();

    private T mSelectedValue;

    public EditManagerRadioGroup(
            RadioGroup categoryTypeGroup, Map<Integer, T> uiTypeMap) {
        mCategoryTypeGroup = categoryTypeGroup;
        // add straight relations
        for (Map.Entry<Integer, T> entry : uiTypeMap.entrySet()) {
            mUiTypeMap.put(entry.getKey(), entry.getValue());
        }
        // add inversed relations
        for (Map.Entry<Integer, T> entry : uiTypeMap.entrySet()) {
            mUiTypeMapInv.put(entry.getValue(), entry.getKey());
        }
        // setup listener
        mCategoryTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mSelectedValue = mUiTypeMap.get(checkedId);
            }
        });
    }

    @Override
    public T getValue() {
        return mSelectedValue;
    }

    @Override
    public void setValue(T type) {
        mSelectedValue = type;
        int typeButtonId = mUiTypeMapInv.get(mSelectedValue);
        RadioButton typeButton = (RadioButton) mCategoryTypeGroup.findViewById(typeButtonId);
        typeButton.setChecked(true);
    }

    @Override
    public boolean isValid() {
        return mSelectedValue != null;
    }
}

package budny.moneykeeper.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import budny.moneykeeper.R;

public class FragmentAccountEdit extends Fragment {
    private TextInputLayout mAccountNameContainer;
    private EditText mAccountName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_edit, container, false);

        mAccountNameContainer = (TextInputLayout) view.findViewById(R.id.input_container_edit_account_name);
        mAccountName = (EditText) view.findViewById(R.id.edit_account_name);
        return view;
    }
}

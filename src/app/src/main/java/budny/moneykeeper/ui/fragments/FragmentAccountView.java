package budny.moneykeeper.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterFragmentAccountView;
import budny.moneykeeper.bl.presenters.impl.PresenterFragmentAccountView;
import budny.moneykeeper.db.util.IDataChangeListener;
import budny.moneykeeper.ui.misc.IntentExtras;

public class FragmentAccountView extends Fragment {
    private static final String TAG = FragmentAccountView.class.getSimpleName();

    @SuppressWarnings("FieldCanBeLocal")
    private int mAccountIdx = IntentExtras.INDEX_INVALID;
    private IPresenterFragmentAccountView mPresenter;

    private TextView mTotalAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // parse arguments
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalArgumentException(
                    TAG + " is not initialized with arguments bundle");
        }
        mAccountIdx = args.getInt(IntentExtras.FIELD_INDEX, IntentExtras.INDEX_INVALID);
        if (mAccountIdx == IntentExtras.INDEX_INVALID) {
            throw new IllegalArgumentException(
                    "Unable to locate following arguments: " + IntentExtras.FIELD_INDEX);
        }
        mPresenter = new PresenterFragmentAccountView(mAccountIdx);
        // setup owned views
        View view = inflater.inflate(R.layout.fragment_account_view, container, false);
        mTotalAmount = (TextView) view.findViewById(R.id.view_amount_total);
        // setup update listener
        mPresenter.addDataChangeListener(new IDataChangeListener() {
            @Override
            public void onChange() {
                updateFields();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
        updateFields();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    /**
     * Fills owned fields with data.
     */
    private void updateFields() {
        mTotalAmount.setText(String.valueOf(mPresenter.getTotalAmount()));
    }
}

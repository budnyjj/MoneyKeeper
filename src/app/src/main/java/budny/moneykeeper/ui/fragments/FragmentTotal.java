package budny.moneykeeper.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import budny.moneykeeper.R;
import budny.moneykeeper.bl.presenters.IPresenterFragmentTotal;
import budny.moneykeeper.bl.presenters.impl.PresenterFragmentTotal;
import budny.moneykeeper.db.util.IDataChangeListener;

public class FragmentTotal extends Fragment {
    private final IPresenterFragmentTotal mPresenter = new PresenterFragmentTotal();

    private TextView mTotalAmount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // setup owned views
        View view = inflater.inflate(R.layout.fragment_total, container, false);
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

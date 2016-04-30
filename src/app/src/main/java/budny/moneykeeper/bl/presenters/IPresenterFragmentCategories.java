package budny.moneykeeper.bl.presenters;

import android.content.Context;

import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterFragmentCategories {
    void onStart();

    void onStop();

    void addDataChangeListener(IDataChangeListener listener);

    int getNumCategories();

    Category getCategory(int position);

    /**
     * Update specified category.
     *
     * @param context activity context
     * @param index   index of category to update
     */
    void updateCategory(Context context, int index);

    void deleteCategory(int position);
}
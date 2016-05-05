package budny.moneykeeper.bl.presenters;

import android.content.Context;

import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterFragmentCategoriesView {
    void onStart();

    void onStop();

    void addDataChangeListener(IDataChangeListener listener);

    int getNumCategories();

    String getCategoryName(int index);

    /**
     * Update specified category.
     *
     * @param context activity context
     * @param index   index of category to update
     */
    void updateCategory(Context context, int index);

    /**
     * Deletes specified category, if possible.
     *
     * @param index index of category to delete
     * @return true, if deletion succeed, false otherwise
     */
    boolean deleteCategory(int index);
}
package budny.moneykeeper.bl.presenters;

import android.content.Context;

import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterFragmentCategoriesView {
    void onStart();

    void onStop();

    void addDataChangeListener(IDataChangeListener listener);

    int getNumCategories();

    String getCategoryName(int position);

    /**
     * Update specified category.
     *
     * @param context activity context
     * @param position   index of category to update
     */
    void updateCategory(Context context, int position);

    /**
     * Deletes specified category, if possible.
     *
     * @param position index of category to delete
     * @return true, if deletion succeed, false otherwise
     */
    boolean deleteCategory(int position);

    /**
     * Undo last category delete operation.
     *
     * @param position position in adapter to insert undo-deleted item
     * @return true, if undo succeed, false otherwise
     */
    boolean unDeleteLastCategory(int position);
}
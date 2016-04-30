package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.model.Category;

public interface IPresenterFragmentCategoryEdit {
    void onStart();

    void onStop();

    void createCategory(Category category);

    void updateCategory(Category srcCategory, int dstIndex);

    Category getCategory(int index);
}

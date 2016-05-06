package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.model.Category;

public interface IPresenterFragmentCategoryEdit {
    void onStart();

    void onStop();

    void createCategory(String name, Category.Type type);

    void updateCategory(String name, Category.Type type);

    String getCategoryName();

    Category.Type getCategoryType();
}

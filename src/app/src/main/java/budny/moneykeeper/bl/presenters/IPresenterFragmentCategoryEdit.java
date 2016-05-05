package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.model.Category;

public interface IPresenterFragmentCategoryEdit {
    void onStart();

    void onStop();

    void createCategory(String name);

    void updateCategory(String name);

    String getCategoryName();
}

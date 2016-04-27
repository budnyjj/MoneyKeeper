package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.model.Category;

public interface IPresenterCategoryEdit {
    void onStart();

    void onStop();

    void addCategory(Category category);
}

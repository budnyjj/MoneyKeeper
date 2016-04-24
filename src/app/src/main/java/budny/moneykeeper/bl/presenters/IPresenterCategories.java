package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.model.Category;

public interface IPresenterCategories {
    void onStart();

    void onStop();

    int getNumCategories();

    Category getCategory(int position);

    void removeCategory(int position);
}
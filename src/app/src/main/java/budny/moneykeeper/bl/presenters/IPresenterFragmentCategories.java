package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterFragmentCategories {
    void onStart();

    void onStop();

    void addDataChangeListener(IDataChangeListener listener);

    int getNumCategories();

    Category getCategory(int position);

    void removeCategory(int position);
}
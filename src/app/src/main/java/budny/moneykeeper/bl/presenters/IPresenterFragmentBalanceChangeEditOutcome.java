package budny.moneykeeper.bl.presenters;

import java.util.Set;

import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterFragmentBalanceChangeEditOutcome {
    void onStart();

    void onStop();

    void createBalanceChange(long amount, Set<Category> categories);

    void updateBalanceChange(long amount, Set<Category> categories);

    long getAmount();

    String getCategoryName(int index);

    int getNumCategories();

    boolean isSelectedCategory(int index);

    /**
     * Adds specified category to the balance change,
     * if it doesn't contain it yet, or removes it otherwise.
     */
    void toggleCategory(int index);

    Set<Category> getSelectedCategories();

    void addCategorySelectedListener(IDataChangeListener listener);
}

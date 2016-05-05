package budny.moneykeeper.bl.presenters;

import budny.moneykeeper.db.util.IDataChangeListener;

public interface IPresenterFragmentBalanceChangeEditIncome {
    void onStart();

    void onStop();

    long getAmount();

    String getCategoryName(int index);

    int getNumCategories();

    boolean isSelectedCategory(int index);

    void selectCategory(int index);
}

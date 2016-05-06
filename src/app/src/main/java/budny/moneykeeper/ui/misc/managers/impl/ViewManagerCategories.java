package budny.moneykeeper.ui.misc.managers.impl;

import android.widget.TextView;

import java.util.List;

import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.ui.misc.formatters.IFormatter;
import budny.moneykeeper.ui.misc.formatters.impl.FormatterCategories;
import budny.moneykeeper.ui.misc.managers.IViewManager;

public class ViewManagerCategories implements IViewManager<List<Category>> {
    private final IFormatter<List<Category>> mCategoriesFormatter = new FormatterCategories();
    private final TextView mCategoriesView;

    public ViewManagerCategories(TextView categoriesView) {
        mCategoriesView = categoriesView;
    }

    @Override
    public void setValue(List<Category> categories) {
        mCategoriesView.setText(mCategoriesFormatter.format(categories));
    }
}

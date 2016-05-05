package budny.moneykeeper.ui.misc.formatters.impl;

import android.content.Context;
import android.widget.TextView;

import java.util.List;

import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.ui.misc.formatters.ICategoriesFormatter;

public class CategoriesFormatter implements ICategoriesFormatter {
    private final TextView mCategoriesView;

    public CategoriesFormatter(TextView categoriesView) {
        mCategoriesView = categoriesView;
    }

    @Override
    public void format(List<Category> categories) {
        // TODO: rework formating
        String categoriesStr = "";
        for (Category category : categories) {
            categoriesStr += category.getName() + " ";
        }
        mCategoriesView.setText(categoriesStr);
    }
}

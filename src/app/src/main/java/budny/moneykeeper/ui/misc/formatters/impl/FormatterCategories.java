package budny.moneykeeper.ui.misc.formatters.impl;

import java.util.List;

import budny.moneykeeper.db.model.Category;
import budny.moneykeeper.ui.misc.formatters.IFormatter;

public class FormatterCategories implements IFormatter<List<Category>> {
    @Override
    public String format(List<Category> categories) {
        // TODO: rework formating
        String categoriesStr = "";
        for (Category category : categories) {
            categoriesStr += category.getName() + " ";
        }
        return categoriesStr;
    }
}

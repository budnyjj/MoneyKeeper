package budny.moneykeeper.ui.misc.formatters;

import java.util.List;

import budny.moneykeeper.db.model.Category;

/**
 * Formats contents of TextView based on provided categories.
 */
public interface ICategoriesFormatter {
    void format(List<Category> categories);
}

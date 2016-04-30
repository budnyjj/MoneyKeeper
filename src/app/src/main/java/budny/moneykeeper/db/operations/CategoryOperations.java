package budny.moneykeeper.db.operations;

import budny.moneykeeper.db.model.Category;
import io.realm.Realm;
import io.realm.RealmResults;

public class CategoryOperations {
    /**
     * Stores category in database.
     * Initializes category's fields from method parameters.
     *
     * @return managed {@linkplain Category} instance
     */
    public static Category createCategory(Realm realm, String name, String currencyCode) {
        Category category = new Category();
        category.setName(name);
        return CommonOperations.createObject(realm, category);
    }

    /**
     * Retrieves categories from database.
     */
    public static RealmResults<Category> getCategories(Realm realm) {
        return realm.where(Category.class).findAllSorted(Category.FIELD_NAME);
    }

    /**
     * Updates contents of specified category.
     *
     * @param realm Realm instance
     * @param srcCategory source category with new values
     * @param dstCategory category to be updated
     */
    public static void updateCategory(Realm realm, final Category srcCategory, final Category dstCategory) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                dstCategory.setName(srcCategory.getName());
            }
        });
    }
}

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
    public static Category create(Realm realm, String name, Category.Type type) {
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        return create(realm, category);
    }

    /**
     * Stores category in database.
     * Initializes category's fields with values from prototype.
     *
     * @return managed {@linkplain Category} instance
     */
    public static Category create(Realm realm, Category category) {
        // store in database
        realm.beginTransaction();
        Category managedCategory = realm.copyToRealm(category);
        realm.commitTransaction();
        return managedCategory;
    }

    /**
     * Retrieves all categories from database.
     */
    public static RealmResults<Category> read(Realm realm) {
        return realm.where(Category.class).findAllSorted(Category.FIELD_NAME);
    }

    /**
     * Retrieves all categories from database.
     */
    public static RealmResults<Category> readIncome(Realm realm) {
        return realm
                .where(Category.class)
                .contains(Category.FIELD_TYPE, Category.Type.INCOME.toString())
                .findAllSorted(Category.FIELD_NAME);
    }

    /**
     * Retrieves all categories from database.
     */
    public static RealmResults<Category> readOutcome(Realm realm) {
        return realm
                .where(Category.class)
                .contains(Category.FIELD_TYPE, Category.Type.OUTCOME.toString())
                .findAllSorted(Category.FIELD_NAME);
    }

    /**
     * Updates contents of specified category.
     *
     * @param realm Realm instance
     * @param category category to be updated
     */
    public static void update(Realm realm, final Category category,
                              final String name, final Category.Type type) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                category.setName(name);
                category.setType(type);
            }
        });
    }
}

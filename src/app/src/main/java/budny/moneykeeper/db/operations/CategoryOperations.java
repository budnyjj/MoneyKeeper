package budny.moneykeeper.db.operations;

import budny.moneykeeper.db.model.Category;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by budnyjj on 4/24/16.
 */
public class CategoryOperations {
    /**
     * Retrieves categories from database.
     */
    public static RealmResults<Category> getCategories(Realm realm) {
        RealmResults<Category> categories = realm.where(Category.class).findAll();
        categories.sort(Category.FIELD_NAME);
        return categories;
    }
}

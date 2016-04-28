package budny.moneykeeper.db.util;

/**
 * An interface to notify UI layer about data changes.
 *
 * The purpose of this method is to avoid exposing storage-related
 * details (Realm) to the UI level.
 */
public interface IDataChangeListener {
    void onChange();
}

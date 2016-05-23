package budny.moneykeeper.ui.misc.managers;

/**
 * An interface that is used as proxy for various views.
 * Provides set API for values of specified type and
 * encapsulates formatting functionality.
 */
public interface IViewManager<T> {
    /**
     * Updates content of managed input field with formatted value.
     *
     * @param value value to set
     */
    void setValue(T value);
}

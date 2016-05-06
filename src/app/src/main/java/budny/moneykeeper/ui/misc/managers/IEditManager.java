package budny.moneykeeper.ui.misc.managers;

/**
 * An interface that is used as proxy for various input fields.
 * Provides get/set API for values of specified type and
 * encapsulates formatting and validation functionality.
 */
public interface IEditManager<T> {
    /**
     * Provides content of managed input field.
     *
     * @return last validated content of input field.
     */
    T getValue();

    /**
     * Updates content of managed input field with formatted value.
     *
     * @param value value to set
     */
    void setValue(T value);

    /**
     * Checks, if managed input field contains valid value.
     *
     * @return true, if the user input is valid, false otherwise
     */
    boolean isValid();
}

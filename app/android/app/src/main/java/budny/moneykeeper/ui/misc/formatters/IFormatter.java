package budny.moneykeeper.ui.misc.formatters;

import java.util.IllegalFormatConversionException;

/**
 * An interface that provides custom formatting functionality.
 * It is typically used to convert values from the machine-readable
 * format to the their representation, suitable for showing them in UI fields.
 */
public interface IFormatter<T> {
    /**
     * Formats input value.
     *
     * @param value value to be formatted
     * @return string representation of the value, which is ready to be showed in UI
     * @throws IllegalFormatConversionException if it is unable to format value
     */
    String format(T value);
}

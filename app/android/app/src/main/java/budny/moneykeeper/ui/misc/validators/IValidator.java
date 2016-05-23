package budny.moneykeeper.ui.misc.validators;

import java.util.IllegalFormatConversionException;

/**
 * An interface that provides custom validation functionality.
 * It is typically used to convert values from their UI fields
 * representation to the machine-readable format.
 */
public interface IValidator<T> {
    T validate(String value) throws IllegalArgumentException;
}

package budny.moneykeeper.ui.misc.validators.impl;

import budny.moneykeeper.db.model.BalanceChange;
import budny.moneykeeper.ui.misc.validators.IValidator;

public class ValidatorAmount implements IValidator<Long> {
    @Override
    public Long validate(String value) throws IllegalArgumentException {
        try {
            return (long) (Double.parseDouble(value.trim()) * BalanceChange.FRACTION_DIVISOR);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

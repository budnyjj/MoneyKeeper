package budny.moneykeeper.ui.misc.validators.impl;

import budny.moneykeeper.ui.misc.validators.IValidator;

public class ValidatorText implements IValidator<String> {
    private static final String MSG_VALUE_EMPTY = "Input value is empty";

    @Override
    public String validate(String value) {
        String validatedValue = value.trim();
        if (validatedValue.isEmpty()) {
            throw new IllegalArgumentException(MSG_VALUE_EMPTY);
        }
        return validatedValue;
    }
}

package budny.moneykeeper.bl.validators.impl;

import budny.moneykeeper.bl.validators.IValidator;

public class TextValidator implements IValidator {
    @Override
    public boolean validate(String value) {
        return !value.trim().isEmpty();
    }
}

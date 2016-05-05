package budny.moneykeeper.bl.validators.impl;

import budny.moneykeeper.bl.validators.IContentValidator;

public class TextValidator implements IContentValidator {
    @Override
    public boolean validate(String value) {
        return !value.trim().isEmpty();
    }
}

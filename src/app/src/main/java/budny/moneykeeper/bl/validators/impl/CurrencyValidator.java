package budny.moneykeeper.bl.validators.impl;

import budny.moneykeeper.bl.validators.IContentValidator;

public class CurrencyValidator implements IContentValidator {
    @Override
    public boolean validate(String value) {
        try {
            Long.parseLong(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

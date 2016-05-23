package budny.moneykeeper.ui.misc.formatters.impl;

import budny.moneykeeper.ui.misc.formatters.IFormatter;

public class FormatterText implements IFormatter<String> {
    @Override
    public String format(String value) {
        return value.trim();
    }
}

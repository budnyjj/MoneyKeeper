package budny.moneykeeper.ui.misc.formatters;

/**
 * Formats contents of TextView based on provided amount value.
 */
public interface ICurrencyFormatter {
    void format(long amount, String currencyCode);
}

package budny.moneykeeper.ui.misc.listeners;

/**
 * Listener for communication between fragment and parent activity,
 * when user wants to create or update content.
 */
public interface IContentEditListener {
    /**
     * Action, which should be performed to create or update content.
     *
     * @return true on success, false otherwise.
     */
    boolean onEditContent();
}

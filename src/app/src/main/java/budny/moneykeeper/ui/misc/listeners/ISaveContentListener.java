package budny.moneykeeper.ui.misc.listeners;

/**
 * Listener for communication between fragment and parent activity,
 * when user wants to save content.
 */
public interface ISaveContentListener {
    /**
     * Action, which should be performed to save content.
     *
     * @return true on success, false otherwise.
     */
    boolean onSaveContent();
}

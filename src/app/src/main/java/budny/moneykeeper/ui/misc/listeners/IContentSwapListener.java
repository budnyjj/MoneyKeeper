package budny.moneykeeper.ui.misc.listeners;

/**
 * Listener for communication between touch callback and fragment,
 * when user wants to swap content.
 */
public interface IContentSwapListener {
    /**
     * Action, which should be performed to swap content.
     *
     * @param fromPosition index of source content in dataset
     * @param toPosition index of destination content in dataset
     */
    void onSwapContent(int fromPosition, int toPosition);
}

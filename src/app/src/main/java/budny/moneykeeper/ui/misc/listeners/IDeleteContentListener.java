package budny.moneykeeper.ui.misc.listeners;

/**
 * Listener for communication between touch callback and fragment,
 * when user wants to delete content by swipe.
 */
public interface IDeleteContentListener {
    /**
     * Action, which should be performed to delete content.
     *
     * @param position index of content in dataset
     */
    void onDeleteContent(int position);
}

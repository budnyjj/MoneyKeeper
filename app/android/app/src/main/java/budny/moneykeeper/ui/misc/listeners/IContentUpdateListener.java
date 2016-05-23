package budny.moneykeeper.ui.misc.listeners;

public interface IContentUpdateListener {
    /**
     * Action, which should be performed to update content.
     *
     * @param position index of source content in dataset
     */
    void onUpdateContent(int position);
}

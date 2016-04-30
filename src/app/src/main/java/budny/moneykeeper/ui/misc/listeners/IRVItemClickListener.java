package budny.moneykeeper.ui.misc.listeners;

import android.view.View;

/**
 * Handles click events on {@linkplain android.support.v7.widget.RecyclerView} items.
 */
public interface IRVItemClickListener {
    void onItemClick(View view, int position);

    void onItemLongClick(View view, int position);
}

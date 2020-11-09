package fr.r_thd.player.adapter;

/**
 * Listener des adapter
 */
public interface AdapterListener {
    /**
     * On long click
     */
    void onLongClick();

    /**
     * On click
     *
     * @param pos Position de l'adapter
     */
    void onClick(int pos);

    /**
     * On edit button
     *
     * @param pos Position de l'adapter
     */
    void onEditButtonClick(int pos);

    /**
     * On delete button
     *
     * @param pos Position de l'adapter
     */
    void onDeleteButtonClick(int pos);
}

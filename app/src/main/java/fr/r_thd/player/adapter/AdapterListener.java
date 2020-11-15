package fr.r_thd.player.adapter;

import android.graphics.Bitmap;

/**
 * Listener des adapter, permet de gérer chaque holder individuellement,
 * et de séparer les onclick entre les différents boutons
 *
 * @param <T> Type
 */
public interface AdapterListener<T> {
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

    /**
     * @param elem Élément
     * @param filter Filtre
     * @return Si l'élément est reconnu par le filtre ou non
     */
    boolean containsFilter(T elem, String filter);

    /**
     * @param elem Élément
     * @return Titre
     */
    String getTitle(T elem);

    /**
     * @param elem Élément
     * @return Preview
     */
    Bitmap getPreview(T elem);
}

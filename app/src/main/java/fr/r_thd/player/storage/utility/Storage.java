package fr.r_thd.player.storage.utility;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Storage
 *
 * @param <T> Contenu
 */
public interface Storage<T> {
    /**
     * Get un élément
     *
     * @param id Id
     *
     * @return Élément
     */
    @Nullable
    T find(int id);

    /**
     * Trouve tous les éléments
     *
     * @return Tous les éléments
     */
    @NonNull
    List<T> findAll();

    /**
     * @return Nombre d'éléments
     */
    int size();

    /**
     * Insère un élément
     *
     * @param elem Élément
     *
     * @return Id de l'élément
     */
    int insert(@NonNull T elem);

    /**
     * Update un élément
     *
     * @param id Id
     * @param elem Élément
     *
     * @return Updated ou non
     */
    boolean update(int id, @NonNull T elem);

    /**
     * Supprime un élément
     *
     * @param id Id
     *
     * @return Supprimé ou non
     */
    boolean delete(int id);
}

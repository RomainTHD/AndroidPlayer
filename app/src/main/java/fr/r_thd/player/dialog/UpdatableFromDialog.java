package fr.r_thd.player.dialog;

import androidx.annotation.NonNull;

/**
 * Updatable from dialog
 */
public interface UpdatableFromDialog {
    /**
     * Type d'update
     */
    enum UpdateType {
        /**
         * Édition
         */
        EDIT,

        /**
         * Suppression
         */
        DELETE
    }

    /**
     * Update from dialog
     *
     * @param pos Pos
     * @param type Type d'update
     */
    void updateFromDialog(int pos, @NonNull UpdateType type);
}

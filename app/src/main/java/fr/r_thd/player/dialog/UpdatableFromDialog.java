package fr.r_thd.player.dialog;

public interface UpdatableFromDialog {
    enum UpdateType {
        EDIT, DELETE
    }

    void updateFromDialog(int pos, UpdateType type);
}

package fr.r_thd.player.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import fr.r_thd.player.model.Music;
import fr.r_thd.player.model.Playlist;
import fr.r_thd.player.storage.MusicDatabaseStorage;

public class MusicDeleteDialog extends DialogFragment {
    private final UpdatableFromDialog updatable;
    private final int id;
    private final Playlist playlist;

    public MusicDeleteDialog(UpdatableFromDialog updatable, Playlist playlist, int id) {
        this.updatable = updatable;
        this.id = id;
        this.playlist = playlist;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Suppression")
                .setMessage("Voulez-vous vraiment supprimer '" + playlist.get(id).getTitle() + "' de la playlist ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MusicDatabaseStorage.get(getContext()).delete(playlist.get(id).getId());
                        playlist.remove(id);
                        updatable.updateFromDialog();
                    }
                })
                .setNegativeButton("Non", null)
                .create();
    }
}

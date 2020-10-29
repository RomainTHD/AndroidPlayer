package fr.r_thd.player.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import fr.r_thd.player.objects.Playlist;
import fr.r_thd.player.storage.MusicDatabaseStorage;

public class MusicDeleteDialog extends DialogFragment {
    private final UpdatableFromDialog updatable;
    private final int pos;
    private final Playlist playlist;

    public MusicDeleteDialog(UpdatableFromDialog updatable, Playlist playlist, int pos) {
        this.updatable = updatable;
        this.pos = pos;
        this.playlist = playlist;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Suppression")
                .setMessage("Voulez-vous vraiment supprimer '" + playlist.get(pos).getTitle() + "' de la playlist ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MusicDatabaseStorage.get(getContext()).delete(playlist.get(pos).getId());
                        playlist.remove(pos);
                        updatable.updateFromDialog(pos, UpdatableFromDialog.UpdateType.DELETE);
                    }
                })
                .setNegativeButton("Non", null)
                .create();
    }
}

package fr.r_thd.player.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import fr.r_thd.player.adapter.MusicAdapter;
import fr.r_thd.player.objects.Playlist;
import fr.r_thd.player.storage.MusicDatabaseStorage;

public class MusicDeleteDialog extends DialogFragment {
    private final UpdatableFromDialog updatable;
    private final Playlist playlist;
    private final MusicAdapter adapter;
    private final int pos;

    public MusicDeleteDialog(UpdatableFromDialog updatable, Playlist playlist, MusicAdapter adapter, int pos) {
        this.updatable = updatable;
        this.playlist = playlist;
        this.adapter = adapter;
        this.pos = pos;
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
                        adapter.remove(pos);
                        playlist.remove(pos);
                        updatable.updateFromDialog(pos, UpdatableFromDialog.UpdateType.DELETE);
                    }
                })
                .setNegativeButton("Non", null)
                .create();
    }
}

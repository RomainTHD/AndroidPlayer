package fr.r_thd.player.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.List;

import fr.r_thd.player.adapter.PlaylistAdapter;
import fr.r_thd.player.objects.Music;
import fr.r_thd.player.objects.Playlist;
import fr.r_thd.player.storage.MusicDatabaseStorage;
import fr.r_thd.player.storage.PlaylistDatabaseStorage;

public class PlaylistDeleteDialog extends DialogFragment {
    private final UpdatableFromDialog updatable;
    private final List<Playlist> playlistList;
    private final PlaylistAdapter adapter;
    private final int pos;

    public PlaylistDeleteDialog(UpdatableFromDialog updatable, List<Playlist> playlistList, PlaylistAdapter adapter, int pos) {
        this.updatable = updatable;
        this.playlistList = playlistList;
        this.adapter = adapter;
        this.pos = pos;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Suppression")
                .setMessage("Voulez-vous vraiment supprimer la playlist '" + playlistList.get(pos).getName() + "' ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Playlist playlist = playlistList.get(pos);
                        PlaylistDatabaseStorage.get(getContext()).delete(playlist.getId());
                        List<Music> list = MusicDatabaseStorage.get(getContext()).findAll(playlist.getId());

                        for (Music music : list) {
                            MusicDatabaseStorage.get(getContext()).delete(music.getId());
                        }

                        adapter.remove(pos);
                        playlistList.remove(pos);
                        updatable.updateFromDialog(pos, UpdatableFromDialog.UpdateType.DELETE);
                    }
                })
                .setNegativeButton("Non", null)
                .create();
    }
}

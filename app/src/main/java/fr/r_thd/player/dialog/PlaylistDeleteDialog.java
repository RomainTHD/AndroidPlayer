package fr.r_thd.player.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.List;

import fr.r_thd.player.R;
import fr.r_thd.player.adapter.PlaylistAdapter;
import fr.r_thd.player.objects.Music;
import fr.r_thd.player.objects.Playlist;
import fr.r_thd.player.storage.MusicDatabaseStorage;
import fr.r_thd.player.storage.PlaylistDatabaseStorage;

/**
 * Dialog de suppression de playlist
 */
public class PlaylistDeleteDialog extends DialogFragment {
    /**
     * Updatable
     */
    @NonNull
    private final UpdatableFromDialog updatable;

    /**
     * Liste de playlist
     */
    @NonNull
    private final List<Playlist> playlistList;

    /**
     * Adapter
     */
    @NonNull
    private final PlaylistAdapter adapter;

    /**
     * Position
     */
    private final int pos;

    /**
     * Constructeur
     *
     * @param updatable Updatable
     * @param playlistList Liste de playlist
     * @param adapter Adapter
     * @param pos Position
     */
    public PlaylistDeleteDialog(@NonNull UpdatableFromDialog updatable, @NonNull List<Playlist> playlistList, @NonNull PlaylistAdapter adapter, int pos) {
        this.updatable = updatable;
        this.playlistList = playlistList;
        this.adapter = adapter;
        this.pos = pos;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.dialog_playlist_delete_name))
                .setMessage(getString(R.string.dialog_playlist_confirm, playlistList.get(pos).getName()))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Playlist playlist = playlistList.get(pos);
                        PlaylistDatabaseStorage.get(getContext()).delete(playlist.getId());
                        List<Music> list = MusicDatabaseStorage.get(getContext()).findAll(playlist.getId());

                        for (Music music : list) {
                            MusicDatabaseStorage.get(getContext()).delete(music.getId());
                        }

                        adapter.remove(pos);
                        updatable.updateFromDialog(pos, UpdatableFromDialog.UpdateType.DELETE);
                    }
                })
                .setNegativeButton(getString(R.string.no), null)
                .create();
    }
}

package fr.r_thd.player.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import fr.r_thd.player.R;
import fr.r_thd.player.adapter.MusicAdapter;
import fr.r_thd.player.objects.Playlist;
import fr.r_thd.player.storage.MusicDatabaseStorage;

/**
 * Dialog de suppression de musique
 */
public class MusicDeleteDialog extends DialogFragment {
    /**
     * Updatable
     */
    @NonNull
    private final UpdatableFromDialog updatable;

    /**
     * Playlist
     */
    @NonNull
    private final Playlist playlist;

    /**
     * Adapter
     */
    @NonNull
    private final MusicAdapter adapter;

    /**
     * Position
     */
    private final int pos;

    /**
     * Constructeur
     *
     * @param updatable Updatable
     * @param playlist Playlist
     * @param adapter Adapter
     * @param pos Position
     */
    public MusicDeleteDialog(@NonNull UpdatableFromDialog updatable, @NonNull Playlist playlist, @NonNull MusicAdapter adapter, int pos) {
        this.updatable = updatable;
        this.playlist = playlist;
        this.adapter = adapter;
        this.pos = pos;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.dialog_music_delete_name))
                .setMessage(getString(R.string.dialog_music_confirm, playlist.get(pos).getTitle()))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MusicDatabaseStorage.get(getContext()).delete(playlist.get(pos).getId());
                        adapter.remove(pos);
                        updatable.updateFromDialog(pos, UpdatableFromDialog.UpdateType.DELETE);
                    }
                })
                .setNegativeButton(getString(R.string.no), null)
                .create();
    }
}

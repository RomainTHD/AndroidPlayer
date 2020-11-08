package fr.r_thd.player.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.List;

import fr.r_thd.player.R;
import fr.r_thd.player.objects.Playlist;
import fr.r_thd.player.storage.PlaylistDatabaseStorage;

/**
 * Dialog d'édition de titre de playlist
 */
public class PlaylistEditDialog extends DialogFragment {
    /**
     * Updatable
     */
    @NonNull
    private final UpdatableFromDialog updatable;

    /**
     * Position
     */
    private final int pos;

    /**
     * Playlist modifiée
     */
    @NonNull
    private final Playlist edited;

    /**
     * Constructeur
     *
     * @param updatable Updatable
     * @param playlistList Liste de playlist
     * @param pos Position
     */
    public PlaylistEditDialog(@NonNull UpdatableFromDialog updatable, @NonNull List<Playlist> playlistList, int pos) {
        this.updatable = updatable;
        this.pos = pos;
        this.edited = playlistList.get(pos);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_edit_music, null);
        setMusicToView(view);
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.dialog_playlist_edit_name))
                .setView(view)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Playlist playlist = getMusicFromView(view);
                        PlaylistDatabaseStorage.get(getContext()).update(playlist.getId(), playlist);
                        updatable.updateFromDialog(pos, UpdatableFromDialog.UpdateType.EDIT);
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .create();
    }

    private void setMusicToView(View view) {
        ((EditText) view.findViewById(R.id.music_title)).setText(edited.getName());
    }

    private Playlist getMusicFromView(View view) {
        edited.setName(((EditText) view.findViewById(R.id.music_title)).getText().toString());
        return edited;
    }
}

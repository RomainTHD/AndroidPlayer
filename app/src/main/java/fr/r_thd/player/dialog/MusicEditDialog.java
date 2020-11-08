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

import fr.r_thd.player.R;
import fr.r_thd.player.objects.Music;
import fr.r_thd.player.objects.Playlist;
import fr.r_thd.player.storage.MusicDatabaseStorage;

/**
 * Dialog d'édition de titre de musique
 */
public class MusicEditDialog extends DialogFragment {
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
     * Musique modifiée
     */
    @NonNull
    private final Music edited;

    /**
     * Constructeur
     *
     * @param updatable Updatable
     * @param playlist Playlist
     * @param pos Position
     */
    public MusicEditDialog(@NonNull UpdatableFromDialog updatable, @NonNull Playlist playlist, int pos) {
        this.updatable = updatable;
        this.pos = pos;
        this.edited = playlist.get(pos);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_edit_music, null);
        setMusicToView(view);
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.dialog_music_edit_name))
                .setView(view)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Music music = getMusicFromView(view);
                        MusicDatabaseStorage.get(getContext()).update(music.getId(), music);
                        updatable.updateFromDialog(pos, UpdatableFromDialog.UpdateType.EDIT);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private void setMusicToView(View view) {
        ((EditText) view.findViewById(R.id.music_title)).setText(edited.getTitle());
    }

    private Music getMusicFromView(View view) {
        edited.setTitle(((EditText) view.findViewById(R.id.music_title)).getText().toString());
        return edited;
    }
}

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

public class PlaylistEditDialog extends DialogFragment {
    private final UpdatableFromDialog updatable;
    private final int pos;
    private Playlist edited;
    private View view;

    public PlaylistEditDialog(UpdatableFromDialog updatable, List<Playlist> playlistList, int pos) {
        this.updatable = updatable;
        this.pos = pos;
        this.edited = playlistList.get(pos);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_edit_music, null);
        setMusicToView();
        return new AlertDialog.Builder(getActivity())
                .setTitle("Modifier une musique")
                .setView(view)
                .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Playlist playlist = getMusicFromView();
                        PlaylistDatabaseStorage.get(getContext()).update(playlist.getId(), playlist);
                        updatable.updateFromDialog(pos, UpdatableFromDialog.UpdateType.EDIT);
                    }
                })
                .setNegativeButton("Annuler", null)
                .create();
    }

    private void setMusicToView() {
        ((EditText) view.findViewById(R.id.music_title)).setText(edited.getName());
    }

    private Playlist getMusicFromView() {
        edited.setName(((EditText) view.findViewById(R.id.music_title)).getText().toString());
        return edited;
    }
}

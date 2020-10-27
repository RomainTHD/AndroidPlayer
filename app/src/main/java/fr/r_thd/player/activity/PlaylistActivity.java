package fr.r_thd.player.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.r_thd.player.R;
import fr.r_thd.player.adapter.MusicAdapter;
import fr.r_thd.player.adapter.MusicAdapterListener;
import fr.r_thd.player.dialog.MusicDeleteDialog;
import fr.r_thd.player.dialog.MusicEditDialog;
import fr.r_thd.player.dialog.UpdatableFromDialog;
import fr.r_thd.player.model.Music;
import fr.r_thd.player.model.Playlist;
import fr.r_thd.player.service.MusicPlayerService;
import fr.r_thd.player.storage.MusicDatabaseStorage;
import fr.r_thd.player.storage.PlaylistDatabaseStorage;
import fr.r_thd.player.util.UriUtility;

public class PlaylistActivity extends AppCompatActivity implements UpdatableFromDialog {
    private static final int REQUEST_GET_FILE = 0;

    private static Playlist playlist;
    private MusicAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        int id = getIntent().getIntExtra(HomeActivity.EXTRA_DETAILS_PLAYLIST_ID, -1);

        if (id == -1) {
            Toast.makeText(getApplicationContext(), "ID nul", Toast.LENGTH_LONG).show();
            return;
        }

        playlist = PlaylistDatabaseStorage.get(getApplicationContext()).find(id);

        if (playlist == null) {
            Toast.makeText(getApplicationContext(), "Playlist nulle", Toast.LENGTH_LONG).show();
            return;
        }

        ////////////////////////////////////////////////////////////////////////////////////////////

        final RecyclerView list = findViewById(R.id.playlist);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listAdapter = new MusicAdapter(playlist, new MusicAdapterListener() {
            @Override
            public void onLongClick() {
                Toast.makeText(getApplicationContext(), "Détails d'un élément", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClick(int pos) {
                Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
                intent.putExtra(MusicPlayerActivity.EXTRA_CURRENT_PLAYLIST_ID, playlist.getId());
                intent.putExtra(MusicPlayerActivity.EXTRA_SELECTED_MUSIC_INDEX, pos);
                startActivity(intent);
            }

            @Override
            public void onEditButtonClick(int pos) {
                new MusicEditDialog(PlaylistActivity.this, playlist.get(pos)).show(getSupportFragmentManager(), "");
            }

            @Override
            public void onDeleteButtonClick(int pos) {
                new MusicDeleteDialog(PlaylistActivity.this, playlist, pos).show(getSupportFragmentManager(), "");
            }
        });
        list.setAdapter(listAdapter);

        findViewById(R.id.button_shuffle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playlist.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Playlist vide", Toast.LENGTH_SHORT).show();
                }
                else {
                    playlist.shuffle();
                    playlist.setCurrentIndex(0);
                    Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
                    intent.putExtra(MusicPlayerActivity.EXTRA_CURRENT_PLAYLIST_ID, playlist.getId());
                    intent.putExtra(MusicPlayerActivity.EXTRA_SELECTED_MUSIC_INDEX, playlist.getRandomIndex());
                    intent.putExtra(MusicPlayerActivity.EXTRA_SHOULD_PLAY, Boolean.valueOf(MusicPlayerService.isPlaying()));
                    startActivity(intent);
                }
            }
        });

        findViewById(R.id.button_shuffle).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "Sélectionne une musique aléatoire", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Ajout d'une musique"), REQUEST_GET_FILE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GET_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();

                if (fileUri != null) {
                    String name = UriUtility.getFileName(fileUri, getContentResolver());
                    String path = UriUtility.getPath(fileUri);

                    Music music = new Music(playlist.getId(), name, path);
                    int id = MusicDatabaseStorage.get(getApplicationContext()).insert(music);

                    if (id == -1) {
                        Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        music.setId(id);
                        playlist.add(music);
                        listAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    public void updateFromDialog() {
        listAdapter.notifyDataSetChanged();
    }
}

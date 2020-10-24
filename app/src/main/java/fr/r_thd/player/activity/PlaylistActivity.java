package fr.r_thd.player.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.r_thd.player.R;
import fr.r_thd.player.adapter.MusicAdapter;
import fr.r_thd.player.model.Music;
import fr.r_thd.player.model.Playlist;
import fr.r_thd.player.service.MusicPlayerService;
import fr.r_thd.player.storage.MusicDatabaseStorage;
import fr.r_thd.player.storage.PlaylistDatabaseStorage;
import fr.r_thd.player.util.UriUtility;

public class PlaylistActivity extends AppCompatActivity {
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
        listAdapter = new MusicAdapter(playlist) {
            @Override
            public void onItemClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
                intent.putExtra(MusicPlayerActivity.EXTRA_CURRENT_PLAYLIST_ID, playlist.getId());
                intent.putExtra(MusicPlayerActivity.EXTRA_SELECTED_MUSIC_INDEX, list.getChildViewHolder(v).getAdapterPosition());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PlaylistActivity.this);
                builder.setTitle("Modifier le titre");

                final EditText input = new EditText(PlaylistActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                input.setHint("Titre de la musique");
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = input.getText().toString().trim();

                        if (!title.isEmpty()) {
                            playlist.get(list.getChildViewHolder(v).getAdapterPosition()).setTitle(title);
                            notifyDataSetChanged();
                        }
                    }
                });

                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                return true;
            }
        };
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
}

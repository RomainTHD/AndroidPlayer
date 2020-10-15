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
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import fr.r_thd.player.R;
import fr.r_thd.player.adapter.MusicAdapter;
import fr.r_thd.player.model.Music;
import fr.r_thd.player.model.Playlist;
import fr.r_thd.player.util.UriUtility;

public class PlaylistActivity extends AppCompatActivity {
    private static final int REQUEST_GET_FILE = 0;

    private Playlist playlist;
    private MusicAdapter listAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        playlist = (Playlist) getIntent().getSerializableExtra(HomeActivity.EXTRA_DETAILS_PLAYLIST);

        if (playlist == null) {
            Toast.makeText(getApplicationContext(), "Playlist nulle", Toast.LENGTH_LONG).show();
            return;
        }

        final RecyclerView list = findViewById(R.id.playlist);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listAdaper = new MusicAdapter(playlist) {
            @Override
            public void onItemClick(View v) {
                playlist.setCurrentIndex(list.getChildViewHolder(v).getAdapterPosition());
                Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
                intent.putExtra(MusicPlayerActivity.EXTRA_CURRENT_PLAYLIST, playlist);
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
        list.setAdapter(listAdaper);

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
                    intent.putExtra(MusicPlayerActivity.EXTRA_CURRENT_PLAYLIST, playlist);
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
                    playlist.add(new Music(name, fileUri.toString()));
                    listAdaper.notifyDataSetChanged();
                }
            }
        }
    }
}

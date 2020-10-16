package fr.r_thd.player.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.r_thd.player.R;
import fr.r_thd.player.adapter.PlaylistAdapter;
import fr.r_thd.player.model.Music;
import fr.r_thd.player.model.Playlist;
import fr.r_thd.player.service.MusicPlayerService;

public class HomeActivity extends AppCompatActivity {
    private static List<Playlist> playlistList = new ArrayList<>();

    static {
        Playlist p1 = new Playlist("Ma playlist");
        p1.add(new Music("test", null));
        p1.add(new Music("test2", null));

        Playlist p2 = new Playlist("Autre playlist");
        p2.add(new Music("test3", null));
        p2.add(new Music("test4", null));
        p2.add(new Music("test5", null));

        Playlist p3 = new Playlist("Playlist 3");

        playlistList.add(p1);
        playlistList.add(p2);
        playlistList.add(p3);
    }

    public static final String EXTRA_DETAILS_PLAYLIST = "EXTRA_DETAILS_PLAYLIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!MusicPlayerService.isRunning()) {
            Intent intent = new Intent(
                    getApplicationContext(),
                    MusicPlayerService.class
            );

            startService(intent);
        }

        setContentView(R.layout.activity_home);

        final RecyclerView list = findViewById(R.id.playlist_list);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        final PlaylistAdapter playlistAdapter = new PlaylistAdapter(playlistList) {
            @Override
            public void onItemClick(View v) {
                Playlist playlist = playlistList.get(list.getChildViewHolder(v).getAdapterPosition());
                Intent intent = new Intent(getApplicationContext(), PlaylistActivity.class);
                intent.putExtra(EXTRA_DETAILS_PLAYLIST, playlist);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Modifier le nom de la playlist");

                final EditText input = new EditText(HomeActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                input.setText(playlistList.get(list.getChildViewHolder(v).getAdapterPosition()).getName());
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString().trim();

                        if (!name.isEmpty()) {
                            playlistList.get(list.getChildViewHolder(v).getAdapterPosition()).setName(name);
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

        list.setAdapter(playlistAdapter);

        findViewById(R.id.button_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Créer une playlist");

                final EditText input = new EditText(HomeActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                input.setHint("Nom de la playlist");
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString().trim();

                        if (!name.isEmpty()) {
                            playlistList.add(new Playlist(name));
                            playlistAdapter.notifyDataSetChanged();
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
            }
        });

        findViewById(R.id.button_create).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "Créer une playlist", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    public void update() {

    }
}

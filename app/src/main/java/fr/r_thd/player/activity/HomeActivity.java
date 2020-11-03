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
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import fr.r_thd.player.R;
import fr.r_thd.player.adapter.AdapterListener;
import fr.r_thd.player.adapter.PlaylistAdapter;
import fr.r_thd.player.dialog.PlaylistDeleteDialog;
import fr.r_thd.player.dialog.PlaylistEditDialog;
import fr.r_thd.player.dialog.UpdatableFromDialog;
import fr.r_thd.player.objects.Playlist;
import fr.r_thd.player.service.MusicPlayerService;
import fr.r_thd.player.storage.PlaylistDatabaseStorage;

public class HomeActivity extends AppCompatActivity implements UpdatableFromDialog {
    public static final String EXTRA_DETAILS_PLAYLIST_ID = "EXTRA_DETAILS_PLAYLIST_ID";

    private PlaylistAdapter playlistAdapter;

    private List<Playlist> playlistList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ////////////////////////////////////////////////////////////////////////////////////////////

        if (!MusicPlayerService.isRunning()) {
            Intent intent = new Intent(
                    getApplicationContext(),
                    MusicPlayerService.class
            );

            startService(intent);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////

        playlistList = PlaylistDatabaseStorage.get(getApplicationContext()).findAll();

        setContentView(R.layout.activity_home);

        final RecyclerView list = findViewById(R.id.playlist_list);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        playlistAdapter = new PlaylistAdapter(playlistList, new AdapterListener() {
            @Override
            public void onLongClick() {
                Toast.makeText(getApplicationContext(), "Détails d'une playlist", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClick(int pos) {
                Playlist playlist = playlistList.get(pos);
                Intent intent = new Intent(getApplicationContext(), PlaylistActivity.class);
                intent.putExtra(EXTRA_DETAILS_PLAYLIST_ID, playlist.getId());
                startActivity(intent);
            }

            @Override
            public void onEditButtonClick(int pos) {
                new PlaylistEditDialog(HomeActivity.this, playlistList, pos).show(getSupportFragmentManager(), "");
            }

            @Override
            public void onDeleteButtonClick(int pos) {
                new PlaylistDeleteDialog(HomeActivity.this, playlistList, playlistAdapter, pos).show(getSupportFragmentManager(), "");
            }
        });

        list.setAdapter(playlistAdapter);

        ////////////////////////////////////////////////////////////////////////////////////////////

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
                            Playlist playlist = new Playlist(name);
                            int id = PlaylistDatabaseStorage.get(getApplicationContext()).insert(playlist);

                            if (id != -1) {
                                playlist.setId(id);
                                playlistList.add(playlist);
                                playlistAdapter.add(playlist);
                                playlistAdapter.notifyDataSetChanged();
                            }
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

        ////////////////////////////////////////////////////////////////////////////////////////////

        SearchView searchView = findViewById(R.id.search_bar_playlist);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                playlistAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void updateFromDialog(int pos, UpdatableFromDialog.UpdateType type) {
        if (type == UpdateType.EDIT) {
            playlistAdapter.notifyItemChanged(pos);
        }
        else if (type == UpdateType.DELETE) {
            playlistAdapter.notifyItemRemoved(pos);
        }
    }
}

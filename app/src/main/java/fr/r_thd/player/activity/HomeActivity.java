package fr.r_thd.player.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.LinearLayout;
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

/**
 * Activité d'accueil
 */
public class HomeActivity extends AppCompatActivity implements UpdatableFromDialog {
    /**
     * Code pour les détails d'une playlist
     */
    public static final String EXTRA_DETAILS_PLAYLIST_ID = "EXTRA_DETAILS_PLAYLIST_ID";

    /**
     * Adapter de la liste des playlist
     */
    private PlaylistAdapter playlistAdapter;

    /**
     * Liste des playlist
     */
    private List<Playlist> playlistList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playlistList = PlaylistDatabaseStorage.get(getApplicationContext()).findAll();

        // Lancement du service de musique
        if (!MusicPlayerService.isRunning()) {
            Intent intent = new Intent(
                    getApplicationContext(),
                    MusicPlayerService.class
            );

            startService(intent);
        }

        buildRecyclerView();
        bindButtons();

        SearchView searchView = findViewById(R.id.search_bar_playlist);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                playlistAdapter.getFilter().filter(newText);
                return true;
            }
        });

        playlistAdapter.notifyDataSetChanged();
    }

    private void buildRecyclerView() {
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

        playlistAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                onChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                onChanged();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                onChanged();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                onChanged();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                onChanged();
            }

            @Override
            public void onChanged() {
                super.onChanged();
                View noResultView = findViewById(R.id.no_result);
                if (playlistAdapter.getItemCount() == 0) {
                    noResultView.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
                }
                else {
                    noResultView.getLayoutParams().height = 0;
                }
                noResultView.requestLayout();
            }
        });

        list.setAdapter(playlistAdapter);
    }

    private void bindButtons() {
        findViewById(R.id.button_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle(getString(R.string.dialog_playlist_title));

                final EditText input = new EditText(HomeActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                input.setHint(getString(R.string.dialog_playlist_hint));
                builder.setView(input);

                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString().trim();

                        if (!name.isEmpty()) {
                            Playlist playlist = new Playlist(name);
                            int id = PlaylistDatabaseStorage.get(getApplicationContext()).insert(playlist);

                            if (id != -1) {
                                playlist.setId(id);
                                playlistAdapter.add(playlist);
                                playlistAdapter.notifyItemInserted(playlistList.size() - 1);
                            }
                        }
                    }
                });

                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
                Toast.makeText(getApplicationContext(), getString(R.string.hint_create_playlist), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void updateFromDialog(int pos, @NonNull UpdatableFromDialog.UpdateType type) {
        if (type == UpdateType.EDIT) {
            playlistAdapter.notifyItemChanged(pos);
        }
        else if (type == UpdateType.DELETE) {
            playlistAdapter.notifyItemRemoved(pos);
        }
    }
}

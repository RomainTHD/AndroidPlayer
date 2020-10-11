package fr.r_thd.player.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import fr.r_thd.player.R;
import fr.r_thd.player.adapter.MusicAdapter;
import fr.r_thd.player.model.Music;
import fr.r_thd.player.model.Playlist;

public class PlaylistActivity extends AppCompatActivity {
    public static final String EXTRA_DETAILS_MUSIC = "EXTRA_DETAILS_MUSIC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        final Playlist playlist = (Playlist) getIntent().getSerializableExtra(HomeActivity.EXTRA_DETAILS_PLAYLIST);

        if (playlist == null) {
            Toast.makeText(getApplicationContext(), "Playlist nulle", Toast.LENGTH_LONG).show();
            return;
        }

        final RecyclerView list = findViewById(R.id.playlist);
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list.setAdapter(new MusicAdapter(playlist) {
            @Override
            public void onItemClick(View v) {
                Music music = playlist.get(list.getChildViewHolder(v).getAdapterPosition());
                Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
                intent.putExtra(EXTRA_DETAILS_MUSIC, music);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View v) {
                Toast.makeText(getApplicationContext(), "Détails d'une musique", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}

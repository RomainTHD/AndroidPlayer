package fr.r_thd.player.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fr.r_thd.player.R;
import fr.r_thd.player.model.Music;
import fr.r_thd.player.model.Playlist;
import fr.r_thd.player.service.MusicPlayerService;

public class MusicPlayerActivity extends AppCompatActivity {
    public static final String EXTRA_URI = "EXTRA_URI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        final Playlist playlist = (Playlist) getIntent().getSerializableExtra(PlaylistActivity.EXTRA_CURRENT_PLAYLIST);

        if (playlist == null) {
            Toast.makeText(getApplicationContext(), "Playlist nulle", Toast.LENGTH_LONG).show();
            return;
        }

        final Music music = playlist.get();

        FloatingActionButton playPauseButton = findViewById(R.id.button_play_pause);

        final TextView text = findViewById(R.id.text);

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicPlayerActivity.this, MusicPlayerService.class);

                if (isMyServiceRunning(MusicPlayerService.class)) {
                    // playPauseButton.setImageDrawable();
                    text.setText("Stoped");
                    stopService(intent);
                } else {
                    text.setText("Started");
                    intent.putExtra(EXTRA_URI, music.getUri());
                    startService(intent);
                }
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

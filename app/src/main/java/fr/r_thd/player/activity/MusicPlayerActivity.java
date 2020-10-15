package fr.r_thd.player.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fr.r_thd.player.R;
import fr.r_thd.player.model.Music;
import fr.r_thd.player.model.Playlist;
import fr.r_thd.player.service.MusicPlayerService;

public class MusicPlayerActivity extends AppCompatActivity {
    public static final String EXTRA_URI = "EXTRA_URI";
    public static final String EXTRA_CURRENT_PLAYLIST = "EXTRA_CURRENT_PLAYLIST";
    public static final String EXTRA_IS_MUSIC_PLAYING = "EXTRA_IS_MUSIC_PLAYING";

    private Playlist playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        playlist = (Playlist) getIntent()
                .getSerializableExtra(EXTRA_CURRENT_PLAYLIST);

        if (playlist == null) {
            Toast.makeText(getApplicationContext(), "Playlist nulle", Toast.LENGTH_LONG).show();
            return;
        }

        final Music music = playlist.get();

        ////////////////////////////////////////////////////////////////////////////////////////////

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        SeekBar volumeSeekBar = findViewById(R.id.volume_seek_bar);
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////

        ((TextView) findViewById(R.id.music_title)).setText(music.getTitle());

        ////////////////////////////////////////////////////////////////////////////////////////////

        final FloatingActionButton playPauseButton = findViewById(R.id.button_play_pause);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMusicServicePlaying()) {
                    playPauseButton.setImageResource(R.drawable.ic_play);
                    stopMusicPlayerService();
                }
                else {
                    playPauseButton.setImageResource(R.drawable.ic_pause);
                    startMusicPlayerService(music.getUri());
                }
            }
        });

        if (isMusicServicePlaying()) {
            playPauseButton.setImageResource(R.drawable.ic_pause);
        }
        else {
            playPauseButton.setImageResource(R.drawable.ic_play);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////

        findViewById(R.id.button_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPreviousMusic();
            }
        });

        findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextMusic();
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////

        Toast.makeText(getApplicationContext(), String.valueOf(isMusicServicePlaying()), Toast.LENGTH_SHORT).show();

        Object isMusicPlayingObj = getIntent()
                .getSerializableExtra(EXTRA_IS_MUSIC_PLAYING);

        boolean isMusicPlaying = false;

        if (isMusicPlayingObj != null) {
            isMusicPlaying = (Boolean) isMusicPlayingObj;
        }

        if (isMusicPlaying) {
            // startMusicPlayerService(music.getUri());
            // TODO: Marche po
        }

        // Toast.makeText(getApplicationContext(), String.valueOf(isMusicServicePlaying()) + String.valueOf(isMusicPlaying), Toast.LENGTH_SHORT).show();
    }

    private void startPreviousMusic() {
        playlist.previous();
        Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
        intent.putExtra(EXTRA_CURRENT_PLAYLIST, playlist);
        intent.putExtra(EXTRA_IS_MUSIC_PLAYING, Boolean.valueOf(isMusicServicePlaying()));
        stopMusicPlayerService();
        startActivity(intent);
        finish();
    }

    private void startNextMusic() {
        playlist.next();
        Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
        intent.putExtra(EXTRA_CURRENT_PLAYLIST, playlist);
        intent.putExtra(EXTRA_IS_MUSIC_PLAYING, Boolean.valueOf(isMusicServicePlaying()));
        stopMusicPlayerService();
        startActivity(intent);
        finish();
    }

    private void startMusicPlayerService(String uri) {
        MusicPlayerService.setCaller(this);

        Intent intent = new Intent(
                getApplicationContext(),
                MusicPlayerService.class
        );

        intent.putExtra(EXTRA_URI, uri);
        startService(intent);
    }

    private void stopMusicPlayerService() {
        Intent intent = new Intent(
                getApplicationContext(),
                MusicPlayerService.class
        );

        stopService(intent);
    }

    public void onMusicFinished() {
        startNextMusic();
    }

    private boolean isMusicServicePlaying() {
        return isServicePlaying(MusicPlayerService.class);
    }

    private boolean isServicePlaying(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(
                MusicPlayerActivity.this,
                MusicPlayerService.class
        );
        stopService(intent);

        super.onDestroy();
    }
}

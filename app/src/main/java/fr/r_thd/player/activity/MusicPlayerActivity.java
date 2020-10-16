package fr.r_thd.player.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
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
    public static final String EXTRA_CURRENT_PLAYLIST = "EXTRA_CURRENT_PLAYLIST";
    public static final String EXTRA_SHOULD_PLAY =  "EXTRA_SHOULD_PLAY";

    private Playlist playlist;
    private Music music;

    private MusicPlayerService musicPlayerService;

    private ServiceConnection connection;

    private boolean shouldPlay;

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

        music = playlist.get();

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
                if (MusicPlayerService.isPlaying()) {
                    playPauseButton.setImageResource(R.drawable.ic_play);
                    musicPlayerService.pause();
                }
                else {
                    playPauseButton.setImageResource(R.drawable.ic_pause);
                    musicPlayerService.play();
                }
            }
        });

        if (MusicPlayerService.isPlaying()) {
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

        Object shouldPlayObj = getIntent()
                .getSerializableExtra(EXTRA_SHOULD_PLAY);

        shouldPlay = false;

        if (shouldPlayObj != null) {
            shouldPlay = (Boolean) shouldPlayObj;
        }
    }

    private void startPreviousMusic() {
        playlist.previous();
        Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
        intent.putExtra(EXTRA_CURRENT_PLAYLIST, playlist);
        intent.putExtra(EXTRA_SHOULD_PLAY, Boolean.valueOf(MusicPlayerService.isPlaying()));
        startActivity(intent);
        finish();
    }

    private void startNextMusic() {
        playlist.next();
        Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
        intent.putExtra(EXTRA_CURRENT_PLAYLIST, playlist);
        intent.putExtra(EXTRA_SHOULD_PLAY, Boolean.valueOf(MusicPlayerService.isPlaying()));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicPlayerService.MusicPlayerServiceBinder binder = (MusicPlayerService.MusicPlayerServiceBinder) service;
                musicPlayerService = binder.getService();
                musicPlayerService.setCaller(MusicPlayerActivity.this);
                musicPlayerService.setMusic(music.getUri(), shouldPlay);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent intent = new Intent(this, MusicPlayerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        Toast.makeText(getApplicationContext(), String.valueOf(shouldPlay), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        unbindService(connection);
        Toast.makeText(getApplicationContext(), "stop", Toast.LENGTH_SHORT).show();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(getApplicationContext(), "destroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    public void onMusicFinished() {
        startNextMusic();
    }
}

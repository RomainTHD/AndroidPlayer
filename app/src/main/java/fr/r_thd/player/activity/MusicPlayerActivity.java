package fr.r_thd.player.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fr.r_thd.player.R;
import fr.r_thd.player.objects.Music;
import fr.r_thd.player.objects.Playlist;
import fr.r_thd.player.service.MusicPlayerService;
import fr.r_thd.player.storage.PlaylistDatabaseStorage;

/**
 * Activité de player de musique
 */
public class MusicPlayerActivity extends AppCompatActivity {
    public static final String EXTRA_CURRENT_PLAYLIST_ID = "EXTRA_CURRENT_PLAYLIST";
    public static final String EXTRA_SELECTED_MUSIC_INDEX = "EXTRA_SELECTED_MUSIC_INDEX";
    public static final String EXTRA_SHOULD_PLAY =  "EXTRA_SHOULD_PLAY";

    public static final int REQUEST_EXTERNAL_STORAGE = 12345;

    /**
     * Playlist
     */
    private Playlist playlist;

    /**
     * Musique courante
     */
    private Music music;

    /**
     * Service de lecture de musique
     */
    private MusicPlayerService musicPlayerService;

    /**
     * Connexion au service
     */
    private ServiceConnection connection;

    /**
     * Observer du volume
     */
    private ContentObserver volumeObserver;

    /**
     * Doit jouer directement ou non
     */
    private boolean shouldPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        int id = getIntent().getIntExtra(MusicPlayerActivity.EXTRA_CURRENT_PLAYLIST_ID, -1);

        int musicIndex = getIntent().getIntExtra(MusicPlayerActivity.EXTRA_SELECTED_MUSIC_INDEX, -1);

        if (id == -1 || musicIndex == -1) {
            Toast.makeText(getApplicationContext(), getString(R.string.err_null_id), Toast.LENGTH_LONG).show();
            return;
        }

        playlist = PlaylistDatabaseStorage.get(getApplicationContext()).find(id);

        if (playlist == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.err_null_playlist), Toast.LENGTH_LONG).show();
            return;
        }

        playlist.setCurrentIndex(musicIndex);

        music = playlist.get();

        setVolumeBar();

        ((TextView) findViewById(R.id.music_title)).setText(music.getTitle());

        ((TextView) findViewById(R.id.title_next)).setText(playlist.getNext().getTitle());

        ((TextView) findViewById(R.id.title_previous)).setText(playlist.getPrevious().getTitle());

        Bitmap bitmap = music.getPicture();
        if (bitmap != null) {
            ((ImageView) findViewById(R.id.music_picture_id)).setImageBitmap(bitmap);
        }

        bindButtons();

        Object shouldPlayObj = getIntent()
                .getSerializableExtra(EXTRA_SHOULD_PLAY);

        shouldPlay = false;

        if (shouldPlayObj != null) {
            shouldPlay = (Boolean) shouldPlayObj;
        }
    }

    private void startPreviousMusic() {
        Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
        intent.putExtra(EXTRA_CURRENT_PLAYLIST_ID, playlist.getId());
        intent.putExtra(EXTRA_SELECTED_MUSIC_INDEX, playlist.getPreviousIndex());
        intent.putExtra(EXTRA_SHOULD_PLAY, Boolean.valueOf(MusicPlayerService.isPlaying()));
        startActivity(intent);
        finish();
    }

    private void startNextMusic() {
        Intent intent = new Intent(getApplicationContext(), MusicPlayerActivity.class);
        intent.putExtra(EXTRA_CURRENT_PLAYLIST_ID, playlist.getId());
        intent.putExtra(EXTRA_SELECTED_MUSIC_INDEX, playlist.getNextIndex());
        intent.putExtra(EXTRA_SHOULD_PLAY, Boolean.valueOf(MusicPlayerService.isPlaying()));
        startActivity(intent);
        finish();
    }

    private void setVolumeBar() {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        final SeekBar volumeSeekBar = findViewById(R.id.volume_seek_bar);
        final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeSeekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        volumeObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                volumeSeekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            }
        };

        getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, volumeObserver);
    }

    private void bindButtons() {
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
    }

    @Override
    protected void onStop() {
        unbindService(connection);
        getApplicationContext().getContentResolver().unregisterContentObserver(volumeObserver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onMusicFinished() {
        startNextMusic();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getApplicationContext(), getString(R.string.warn_no_permissions), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

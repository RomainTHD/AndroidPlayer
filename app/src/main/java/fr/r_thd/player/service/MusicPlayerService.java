package fr.r_thd.player.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import fr.r_thd.player.activity.MusicPlayerActivity;

/**
 * Service de lecture de musique
 */
public class MusicPlayerService extends Service implements
        MediaPlayer.OnCompletionListener {

    private static boolean isRunning = false;

    private static boolean isPlaying = false;

    public static boolean isRunning() {
        return isRunning;
    }

    public static boolean isPlaying() {
        return isPlaying;
    }

    private final IBinder binder = new MusicPlayerServiceBinder();

    private MusicPlayerActivity caller;

    private int pos = 0;

    /**
     * Music player
     */
    private MediaPlayer musicPlayer;

    public class MusicPlayerServiceBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    public void setCaller(MusicPlayerActivity activity) {
        caller = activity;
    }

    /**
     * Service lié
     */
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void pause() {
        musicPlayer.pause();
        pos = musicPlayer.getCurrentPosition();
        isPlaying = false;
    }

    public void play() {
        musicPlayer.seekTo(pos);
        musicPlayer.start();
        isPlaying = true;
    }

    /**
     * Service créé
     */
    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        isPlaying = false;

        musicPlayer = new MediaPlayer();
        musicPlayer.setLooping(false);
        musicPlayer.setOnCompletionListener(this);

        /*
        musicPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });*/
    }

    /**
     * Service lancé
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void setMusic(String path, final Boolean shouldPlay) {
        musicPlayer.reset();
        pos = 0;

        try {
            musicPlayer.setDataSource(path);
            musicPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (shouldPlay) {
                        isPlaying = true;
                        mp.start();
                    }
                }
            });
            musicPlayer.prepareAsync();
            // musicPlayer.setOnCompletionListener(this);
        }
        catch (IOException e) {
            Toast.makeText(this, "Erreur lors du chargement de la musique", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Service détruit
     */
    @Override
    public void onDestroy() {
        if (musicPlayer.isPlaying()) {
            musicPlayer.stop();
        }

        isRunning = false;
        super.onDestroy();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        caller.onMusicFinished();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // super.onTaskRemoved(rootIntent);
        stopSelf();
    }
}

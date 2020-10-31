package fr.r_thd.player.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import fr.r_thd.player.activity.MusicPlayerActivity;

/**
 * Service de lecture de musique
 */
public class MusicPlayerService extends Service implements
        MediaPlayer.OnCompletionListener {

    /**
     * En train de tourner ou non
     */
    private static boolean isRunning = false;

    /**
     * En train de jouer un son ou non
     */
    private static boolean isPlaying = false;

    /**
     * Path de la musique actuelle
     */
    private static String currentPath;

    /**
     * Getter
     *
     * @return En train de tourner ou non
     */
    public static boolean isRunning() {
        return isRunning;
    }

    /**
     * Getter
     *
     * @return En train de jouer un son ou non
     */
    public static boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Binder
     */
    private final IBinder binder = new MusicPlayerServiceBinder();

    /**
     * Caller
     */
    private MusicPlayerActivity caller;

    /**
     * Timecode dans la musique
     */
    private int pos = 0;

    /**
     * Music player
     */
    private MediaPlayer musicPlayer;

    /**
     * Binder
     */
    public class MusicPlayerServiceBinder extends Binder {
        /**
         * Récupération du service
         *
         * @return Service
         */
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    /**
     * Set caller
     *
     * @param activity Activity
     */
    public void setCaller(MusicPlayerActivity activity) {
        caller = activity;
    }

    /**
     * Service lié
     */
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * Pause
     */
    public void pause() {
        musicPlayer.pause();
        pos = musicPlayer.getCurrentPosition();
        isPlaying = false;
    }

    /**
     * Play / unpause
     */
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
        currentPath = null;

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

    /**
     * Set la musique
     *
     * @param uriStr URI
     * @param shouldPlay Doit jouer ou non
     */
    public void setMusic(String uriStr, final Boolean shouldPlay) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            caller.requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MusicPlayerActivity.REQUEST_EXTERNAL_STORAGE);
        }

        String externalDirectory = Environment.getExternalStorageDirectory().getPath();
        String path = uriStr;

        if (!uriStr.startsWith(externalDirectory)) {
            path = externalDirectory + "/" + uriStr;
        }

        if (path.equals(currentPath)) {
            return;
        }

        currentPath = path;
        Uri uri = Uri.parse(path);

        if (musicPlayer.isPlaying()) {
            musicPlayer.pause();
            musicPlayer.stop();
        }

        musicPlayer.reset();
        pos = 0;

        try {
            musicPlayer.setDataSource(getApplicationContext(), uri);
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
            musicPlayer.setOnCompletionListener(this);
        }
        catch (Exception e) {
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

    /**
     * Mediaplayer terminé
     *
     * @param mp Mediaplayer
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        caller.onMusicFinished();
    }

    /**
     * Tâche retirée
     *
     * @param rootIntent Intention
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // super.onTaskRemoved(rootIntent);
        stopSelf();
    }
}

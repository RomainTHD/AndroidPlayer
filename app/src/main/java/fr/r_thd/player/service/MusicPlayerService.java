package fr.r_thd.player.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fr.r_thd.player.activity.MusicPlayerActivity;

/**
 * Service de lecture de musique
 */
public class MusicPlayerService extends Service {
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
    @Nullable
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
    @NonNull
    private final IBinder binder = new MusicPlayerServiceBinder();

    /**
     * Caller
     */
    @NonNull
    private MusicPlayerActivity caller;

    /**
     * Timecode dans la musique
     */
    private int pos = 0;

    /**
     * Music player
     */
    @NonNull
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
        @NonNull
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    /**
     * Set caller
     *
     * @param activity Activity
     */
    public void setCaller(@NonNull MusicPlayerActivity activity) {
        caller = activity;
    }

    /**
     * Service lié
     */
    @NonNull
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
    public void setMusic(@NonNull String uriStr, final Boolean shouldPlay) {
        if (uriStr.equals(currentPath))
            return;

        Uri uri = Uri.parse(uriStr);

        currentPath = uriStr;

        if (musicPlayer.isPlaying()) {
            musicPlayer.pause();
            musicPlayer.stop();
        }

        musicPlayer.reset();
        pos = 0;

        musicPlayer.setLooping(false);
        musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                currentPath = "";
                caller.onMusicFinished();
            }
        });
        musicPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (shouldPlay) {
                    isPlaying = true;
                    mp.start();
                }
            }
        });

        try {
            musicPlayer.setDataSource(getApplicationContext(), uri);
            musicPlayer.prepareAsync();
        }
        catch (Exception e) {
            Toast.makeText(this, "Erreur lors du chargement de la musique", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Service détruit
     */
    @Override
    public void onDestroy() {
        if (musicPlayer.isPlaying())
            musicPlayer.stop();

        isRunning = false;
        super.onDestroy();
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

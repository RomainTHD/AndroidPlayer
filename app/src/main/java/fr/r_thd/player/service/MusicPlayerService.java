package fr.r_thd.player.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

import fr.r_thd.player.activity.MusicPlayerActivity;

/**
 * Service de lecture de musique
 */
public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private final IBinder binder = new MusicPlayerServiceBinder();

    private MusicPlayerActivity caller;

    /**
     * Music player
     */
    private MediaPlayer musicPlayer;

    public static class MusicPlayerServiceBinder extends Binder {
        MusicPlayerServiceBinder getService() {
            return this;
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

    /**
     * Service créé
     */
    @Override
    public void onCreate() {
        super.onCreate();
        musicPlayer = new MediaPlayer();
        musicPlayer.setLooping(false);
    }

    /**
     * Service lancé
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Uri uri = Uri.parse((String) intent.getSerializableExtra(MusicPlayerActivity.EXTRA_URI));
            musicPlayer.setDataSource(this, uri);
            musicPlayer.setOnPreparedListener(this);
            musicPlayer.prepareAsync();
            musicPlayer.setOnCompletionListener(this);
        }
        catch (IOException e) {
            Toast.makeText(this, "Erreur lors du chargement de la musique", Toast.LENGTH_SHORT).show();
        }

        return START_STICKY;
    }

    /**
     * Service détruit
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (musicPlayer.isPlaying()) {
            musicPlayer.stop();
        }
    }

    /**
     * MediaPlayer prêt
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        musicPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        caller.onMusicFinished();
    }
}

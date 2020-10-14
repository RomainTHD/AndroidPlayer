package fr.r_thd.player.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

import fr.r_thd.player.activity.MusicPlayerActivity;

/**
 * Service de lecture de musique
 */
public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener {
    /**
     * Music player
     */
    private MediaPlayer musicPlayer;

    /**
     * Service lié
     */
    public IBinder onBind(Intent intent) {
        return null;
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
        Toast.makeText(this, "Music Service started by user.", Toast.LENGTH_LONG).show();

        try {
            Uri uri = Uri.parse((String) intent.getSerializableExtra(MusicPlayerActivity.EXTRA_URI));
            musicPlayer.setDataSource(this, uri);
            musicPlayer.setOnPreparedListener(this);
            musicPlayer.prepareAsync();
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
        musicPlayer.stop();
        Toast.makeText(this, "Music Service destroyed by user.", Toast.LENGTH_LONG).show();
    }

    /**
     * MediaPlayer prêt
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        musicPlayer.start();
    }
}

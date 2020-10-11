package fr.r_thd.player.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

import fr.r_thd.player.R;
import fr.r_thd.player.activity.MusicPlayerActivity;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener {
    private MediaPlayer musicPlayer;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Uri test = Uri.parse("file:///android_asset/song.mp3");
        musicPlayer = new MediaPlayer();
        musicPlayer.setLooping(false);
    }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        musicPlayer.stop();
        Toast.makeText(this, "Music Service destroyed by user.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        musicPlayer.start();
    }
}

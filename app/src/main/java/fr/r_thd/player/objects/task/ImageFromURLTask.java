package fr.r_thd.player.objects.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import fr.r_thd.player.objects.Music;

/**
 * Image depuis une URL
 */
public class ImageFromURLTask extends AsyncTask<String, Void, Bitmap> {
    /**
     * Musique
     */
    @NonNull
    private final Music music;

    /**
     * Constructeur
     *
     * @param music Musique
     */
    public ImageFromURLTask(@NonNull Music music) {
        this.music = music;
    }

    /**
     * Do in background
     *
     * @param urls URLs
     *
     * @return Image
     */
    @Nullable
    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap bitmap = null;

        try {
            InputStream is = new URL(urls[0]).openStream();
            bitmap = BitmapFactory.decodeStream(is);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * On post execute
     *
     * @param bitmap Bitmap
     */
    @Override
    protected void onPostExecute(@Nullable Bitmap bitmap) {
        if (bitmap != null)
            music.setPicture(bitmap);
    }
}

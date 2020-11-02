package fr.r_thd.player.objects.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import fr.r_thd.player.objects.Music;

public class ImageFromURLTask extends AsyncTask<String, Void, Bitmap> {
    private Music music;

    public ImageFromURLTask(Music music) {
        this.music = music;
    }

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

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        music.setPicture(bitmap);
    }
}

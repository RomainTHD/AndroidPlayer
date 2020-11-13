package fr.r_thd.player.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

import fr.r_thd.player.objects.task.ImageFromTitleTask;

/**
 * Musique
 */
public class Music {
    /**
     * ID
     */
    private int id;

    /**
     * ID de la playlist
     */
    private final int playlistId;

    /**
     * Titre
     */
    @NonNull
    private String title;

    /**
     * URI
     */
    @NonNull
    private final String uri;

    /**
     * Photo
     */
    @Nullable
    private transient Bitmap picture;

    /**
     * Listener
     */
    @Nullable
    private transient OnBitmapUpdateListener listener;

    /**
     * Constructeur sans id
     *
     * @param playlistId Id de la playlist
     * @param title Titre
     * @param uri URI
     */
    public Music(int playlistId, @NonNull String title, @NonNull String uri) {
        this(-1, playlistId, title, uri, null);
    }

    /**
     * Constructeur
     *
     * @param id Id
     * @param playlistId Id de la playlist
     * @param title Titre
     * @param uri URI
     * @param imageBase64 Image en base64
     */
    public Music(int id, int playlistId, @NonNull String title, @NonNull String uri, @Nullable String imageBase64) {
        this.id = id;
        this.playlistId = playlistId;
        this.title = title;
        this.uri = uri;
        this.picture = null;

        if (imageBase64 == null)
            setTitle(title);
        else {
            final byte[] decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT);
            this.picture = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
    }

    /**
     * @return Id
     */
    public int getId() {
        return id;
    }

    /**
     * Set id
     *
     * @param id Id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Id de la playlist
     */
    public int getPlaylistId() {
        return playlistId;
    }

    /**
     * @return Titre
     */
    @NonNull
    public String getTitle() {
        return title;
    }

    /**
     * @param title Titre
     */
    public void setTitle(@NonNull String title) {
        this.title = title;
        new ImageFromTitleTask(this).execute(title);
    }

    /**
     * @return URI
     */
    @NonNull
    public String getUri() {
        return uri;
    }

    /**
     * @return Picture
     */
    @Nullable
    public Bitmap getPicture() {
        return picture;
    }

    /**
     * Set picture
     *
     * @param picture Picture
     */
    public void setPicture(@NonNull Bitmap picture) {
        this.picture = picture;

        if (listener != null)
            listener.onBitmapUpdate();
    }

    /**
     * @return Image en base64
     */
    @Nullable
    public String getBase64Picture() {
        if (picture == null)
            return null;

        String res = null;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] byteArrayImage = outputStream.toByteArray();
            res = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * Set le listener
     *
     * @param listener Listener
     */
    public void setOnBitmapUpdateListener(@NonNull OnBitmapUpdateListener listener) {
        this.listener = listener;
    }
}

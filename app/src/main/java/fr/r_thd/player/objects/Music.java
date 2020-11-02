package fr.r_thd.player.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

import fr.r_thd.player.objects.task.ImageFromTitleTask;

/**
 * Musique
 */
public class Music implements Parcelable {
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

    private transient Bitmap picture;

    public OnBitmapUpdateListener listener;

    public Music(int playlistId, @NonNull String title, @NonNull String uri) {
        this(-1, playlistId, title, uri, null);
    }

    /**
     * Constructeur
     *
     * @param title Titre
     * @param uri URI
     */
    public Music(int id, int playlistId, @NonNull String title, @NonNull String uri, @Nullable String imageBase64) {
        this.id = id;
        this.playlistId = playlistId;
        this.title = title;
        this.uri = uri;
        this.picture = null;

        if (imageBase64 == null) {
            setTitle(title);
        }
        else {
            final byte[] decodedBytes = Base64.decode(imageBase64, Base64.DEFAULT);
            this.picture = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(picture, flags);
    }

    public static final Parcelable.Creator<Music> CREATOR = new Parcelable.Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Music[] newArray(int size) {
            return null;
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;

        if (listener != null) {
            listener.onBitmapUpdate();
        }
    }

    public String getBase64Picture() {
        if (picture == null) {
            return null;
        }

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

    public void setOnBitmapUpdateListener(OnBitmapUpdateListener listener) {
        this.listener = listener;
    }
}

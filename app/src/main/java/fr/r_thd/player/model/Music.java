package fr.r_thd.player.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

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
    private int playlistId;

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

    public Music(int playlistId, @NonNull String title, @NonNull String uri) {
        this(-1, playlistId, title, uri);
    }

    /**
     * Constructeur
     *
     * @param title Titre
     * @param uri URI
     */
    public Music(int id, int playlistId, @NonNull String title, @NonNull String uri) {
        this.id = id;
        this.playlistId = playlistId;
        this.title = title;
        this.uri = uri;
    }

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
    }

    /**
     * @return URI
     */
    @NonNull
    public String getUri() {
        return uri;
    }
}

package fr.r_thd.player.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

/**
 * Musique
 */
public class Music implements Serializable {
    /**
     * Titre
     */
    @NonNull
    private String title;

    /**
     * URI
     */
    @NonNull
    private String uri;

    /**
     * Constructeur
     *
     * @param title Titre
     * @param uri URI
     */
    public Music(@NonNull String title, @Nullable String uri) {
        this.title = title;

        // TODO: Mettre en not null

        this.uri = uri;
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

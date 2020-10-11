package fr.r_thd.player.model;

import android.net.Uri;

import java.io.Serializable;

public class Music implements Serializable {
    private String title;
    private String uri;

    public Music(String title, String uri) {
        this.title = title;
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }
}

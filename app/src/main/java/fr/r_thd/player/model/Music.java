package fr.r_thd.player.model;

import java.io.Serializable;

public class Music implements Serializable {
    private String title;
    private String path;
    private int resourceId;

    public Music(String title, String path) {
        this.title = title;
        this.path = path;
        this.resourceId = 0;
    }

    public Music(String title, int resourceId) {
        this.title = title;
        this.path = null;
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public int getResourceId() {
        return resourceId;
    }
}

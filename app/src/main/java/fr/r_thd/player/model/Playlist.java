package fr.r_thd.player.model;

import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.r_thd.player.activity.PlaylistActivity;

/**
 * Playlist de musiques
 */
public class Playlist {
    /**
     * ID de la playlist
     */
    private int id;

    /**
     * Titre
     */
    @NonNull
    private String name;

    /**
     * Contenu, musiques
     */
    @NonNull
    private ArrayList<Music> content;

    /**
     * Indexes, ordre des musiques
     */
    @NonNull
    private transient ArrayList<Integer> indexes;

    /**
     * Index de lecture courant
     */
    private transient int currentIndex;

    public Playlist(@NonNull String name) {
        this(-1, name, new ArrayList<Music>());
    }

    public Playlist(int id, @NonNull String name, List<Music> musicList) {
        this.content = new ArrayList<>();
        this.indexes = new ArrayList<>();
        this.currentIndex = 0;
        this.name = name;
        this.id = id;

        for (Music music : musicList) {
            if (music.getPlaylistId() == id) {
                add(music);
            }
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param name Nom
     */
    public void setName(@NonNull String name) {
        this.name = name;
    }

    /**
     * Ajoute une musique
     *
     * @param m Musique
     */
    public void add(@NonNull Music m) {
        content.add(m);
        indexes.add(indexes.size());
    }

    /**
     * Supprime une musique
     *
     * @param i Index
     */
    public void remove(int i) {
        content.remove(i);
        indexes.remove((Integer) i);
    }

    public int getId() {
        return id;
    }

    public Music get() {
        return get(currentIndex);
    }

    public String getName() {
        return name;
    }

    public int size() {
        return content.size();
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = (currentIndex + size()) % size();
    }

    public Music get(int i) {
        if (size() != 0) {
            i = (i + size()) % size();
        }
        int index = indexes.get(i);
        return content.get(index);
    }

    public List<Music> getArray() {
        return content;
    }

    public Music previous() {
        currentIndex = (currentIndex + size() - 1) % size();
        return get();
    }

    public Music next() {
        currentIndex = (currentIndex + 1) % size();
        return get();
    }

    public Music getPrevious() {
        return get(currentIndex - 1);
    }

    public Music getNext() {
        return get(currentIndex + 1);
    }

    public void shuffle() {
        Random r = new Random();

        for (int i = size() - 1; i > 0; i--) {
            int j = r.nextInt(i);

            Integer temp = indexes.get(i);
            indexes.set(i, indexes.get(j));
            indexes.set(j, temp);
        }
    }
}

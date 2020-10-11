package fr.r_thd.player.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Playlist implements Serializable {
    private ArrayList<Music> content;
    private ArrayList<Integer> indexes;

    private int id;
    private int currentIndex;
    private String name;

    private static int GLOBAL_ID = 1;

    public Playlist(String name) {
        this.content = new ArrayList<>();
        this.indexes = new ArrayList<>();
        this.currentIndex = 0;
        this.name = name;
        this.id = GLOBAL_ID;
        GLOBAL_ID ++;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void add(Music m) {
        content.add(m);
        indexes.add(indexes.size());
    }

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
        i = (i + size()) % size();
        int index = indexes.get(i);
        return content.get(index);
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

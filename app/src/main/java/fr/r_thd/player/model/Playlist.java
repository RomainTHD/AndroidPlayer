package fr.r_thd.player.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Playlist extends ArrayList<Music> implements Serializable {
    private int id;
    private int index;
    private String name;

    private static int GLOBAL_ID = 1;

    public Playlist(String name) {
        this.index = 0;
        this.name = name;
        this.id = GLOBAL_ID;
        GLOBAL_ID ++;
    }

    public int getId() {
        return id;
    }

    public Music get() {
        return get(index);
    }

    public String getName() {
        return name;
    }

    @Override
    public Music get(int i) {
        i = (i + size()) % size();
        return super.get(i);
    }

    public Music previous() {
        index = (index + size() - 1) % size();
        return get();
    }

    public Music next() {
        index = (index + 1) % size();
        return get();
    }

    public Music getPrevious() {
        return get(index - 1);
    }

    public Music getNext() {
        return get(index + 1);
    }

    public void shuffle() {
        Random r = new Random();

        for (int i = size() - 1; i > 0; i--) {
            int j = r.nextInt(i);

            Music temp = get(i);
            set(i, get(j));
            set(j, temp);
        }
    }
}

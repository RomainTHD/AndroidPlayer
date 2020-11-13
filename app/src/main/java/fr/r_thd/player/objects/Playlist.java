package fr.r_thd.player.objects;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private final ArrayList<Music> content;

    /**
     * Indexes, ordre des musiques
     */
    @NonNull
    private final ArrayList<Integer> indexes;

    /**
     * Index de lecture courant
     */
    private int currentIndex;

    /**
     * Constructeur vide
     *
     * @param name Nom
     */
    public Playlist(@NonNull String name) {
        this(-1, name, new ArrayList<Music>());
    }

    /**
     * Constructeur
     *
     * @param id Id
     * @param name Nom
     * @param musicList Liste de musiques
     */
    public Playlist(int id, @NonNull String name, @NonNull List<Music> musicList) {
        this.content = new ArrayList<>();
        this.indexes = new ArrayList<>();
        this.currentIndex = 0;
        this.name = name;
        this.id = id;

        for (Music music : musicList)
            add(music);
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
     * @return Id
     */
    public int getId() {
        return id;
    }

    /**
     * @return Musique courante
     */
    @NonNull
    public Music get() {
        return get(currentIndex);
    }

    /**
     * @return Nom de la playlist
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * @return Taille
     */
    public int size() {
        return content.size();
    }

    /**
     * Set index courant
     *
     * @param currentIndex Index courant
     */
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = (currentIndex + size()) % size();
    }

    /**
     * @param i Index
     *
     * @return Musique `i`
     */
    @NonNull
    public Music get(int i) {
        return content.get(indexes.get((i + size()) % size()));
    }

    /**
     * @return Liste des musiques
     */
    @NonNull
    public List<Music> getArray() {
        return content;
    }

    /**
     * @return Musique précédente
     */
    @NonNull
    public Music getPrevious() {
        return get(currentIndex - 1);
    }

    /**
     * @return Index précédent
     */
    public int getPreviousIndex() {
        return indexes.get((currentIndex + size() - 1) % size());
    }

    /**
     * @return Musique suivante
     */
    @NonNull
    public Music getNext() {
        return get(currentIndex + 1);
    }

    /**
     * @return Index suivant
     */
    public int getNextIndex() {
        return indexes.get((currentIndex + 1) % size());
    }

    /**
     * @return Index random
     */
    public int getRandomIndex() {
        return indexes.get(new Random().nextInt(size()));
    }

    /**
     * Shuffle la playlist
     */
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

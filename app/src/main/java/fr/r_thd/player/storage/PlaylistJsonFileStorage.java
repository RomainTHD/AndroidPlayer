package fr.r_thd.player.storage;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import fr.r_thd.player.model.Music;
import fr.r_thd.player.model.Playlist;
import fr.r_thd.player.storage.utility.JSONFileStorage;

public class PlaylistJsonFileStorage extends JSONFileStorage<Playlist> {
    private static final String NAME = "playlist";

    private static final String PLAYLIST_ID = "id";
    private static final String PLAYLIST_NAME = "name";
    private static final String PLAYLIST_ARRAY = "content";

    private static PlaylistJsonFileStorage storage;

    public static PlaylistJsonFileStorage get(Context context) {
        if (storage == null) {
            storage = new PlaylistJsonFileStorage(context);
        }

        return storage;
    }

    private PlaylistJsonFileStorage(Context context) {
        super(context, NAME);
    }

    @Override
    protected JSONObject objectToJsonObject(int id, Playlist object) {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();

        MusicJsonFileStorage storage = MusicJsonFileStorage.get(getContext());

        try {
            json.put(PLAYLIST_ID, object.getId());
            json.put(PLAYLIST_NAME, object.getName());

            List<Music> musicList = object.getArray();

            for (int i = 0; i < musicList.size(); i++) {
                array.put(storage.objectToJsonObject(i, musicList.get(i)));
            }

            json.put(PLAYLIST_ARRAY, array);
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erreur", Toast.LENGTH_SHORT).show();
            json = null;
        }

        return json;
    }

    @Override
    protected Playlist jsonObjectToObject(JSONObject jsonObject) {
        Playlist playlist = null;

        MusicJsonFileStorage storage = MusicJsonFileStorage.get(getContext());

        try {
            playlist = new Playlist(jsonObject.getInt(PLAYLIST_ID), jsonObject.getString(PLAYLIST_NAME));

            JSONArray array = jsonObject.getJSONArray(PLAYLIST_ARRAY);

            for (int i = 0; i < array.length(); i++) {
                playlist.add(storage.jsonObjectToObject(array.getJSONObject(i)));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erreur", Toast.LENGTH_SHORT).show();
        }

        return playlist;
    }
}

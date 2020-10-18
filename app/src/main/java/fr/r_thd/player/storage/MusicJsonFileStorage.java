package fr.r_thd.player.storage;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import fr.r_thd.player.model.Music;
import fr.r_thd.player.storage.utility.JSONFileStorage;

public class MusicJsonFileStorage extends JSONFileStorage<Music> {
    private static final String NAME = "music";
    private static final String MUSIC_TITLE = "title";
    private static final String MUSIC_URI = "uri";

    private static MusicJsonFileStorage storage;

    public static MusicJsonFileStorage get(Context context) {
        if (storage == null) {
            storage = new MusicJsonFileStorage(context);
        }

        return storage;
    }

    private MusicJsonFileStorage(Context context) {
        super(context, NAME);
    }

    @Override
    protected Music jsonObjectToObject(JSONObject jsonObject) {
        Music m = null;

        try {
            m = new Music(jsonObject.getString(MUSIC_TITLE), jsonObject.getString(MUSIC_URI));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return m;
    }

    @Override
    protected JSONObject objectToJsonObject(int id, Music object) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(MUSIC_TITLE, object.getTitle());
            jsonObject.put(MUSIC_URI, object.getUri());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}

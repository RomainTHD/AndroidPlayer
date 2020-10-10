package fr.r_thd.player.storage;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import fr.r_thd.player.model.Playlist;
import fr.r_thd.player.storage.utility.JSONFileStorage;

public class PlaylistJsonFileStorage extends JSONFileStorage<Playlist> {
    private static final String NAME = "playlist";
    private static final String PLAYLIST_ID = "id";

    public PlaylistJsonFileStorage(Context context, String name) {
        super(context, name);
    }

    @Override
    protected JSONObject objectToJsonObject(int id, Playlist object) {
        JSONObject json = new JSONObject();

        try {
            json.put(PLAYLIST_ID, object.getId());
        }
        catch (JSONException e) {
            e.printStackTrace();
            json = null;
        }

        return json;
    }

    @Override
    protected Playlist jsonObjectToObject(JSONObject jsonObject) {
        try {
            return new Playlist(Integer.parseInt(jsonObject.getString(PLAYLIST_ID)));
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

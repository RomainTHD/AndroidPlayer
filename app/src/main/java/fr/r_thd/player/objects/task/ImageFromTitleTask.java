package fr.r_thd.player.objects.task;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.r_thd.player.config.API;
import fr.r_thd.player.objects.Music;

/**
 * Image depuis un titre
 */
public class ImageFromTitleTask extends AsyncTask<String, Void, String> {
    /**
     * Musique
     */
    @NonNull
    private final Music music;

    /**
     * Constructeur
     *
     * @param music Musique
     */
    public ImageFromTitleTask(@NonNull Music music) {
        this.music = music;
    }

    /**
     * Background
     *
     * @param titles Titres
     *
     * @return JSON de YouTube
     */
    @Nullable
    @Override
    protected String doInBackground(@NonNull String... titles) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String title = titles[0];
        String apiKey = API.API_KEY.replace('_', 'a');

        try {
            URL url = new URL("https://youtube.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&q=" + title + "&relevanceLanguage=fr-FR&type=video&key=" + apiKey);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder buffer = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
                // Log.d("Response", "> " + line); // Full response
            }

            return buffer.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Après l'exécution
     *
     * @param JSONString String JSON
     */
    @Override
    protected void onPostExecute(@Nullable String JSONString) {
        if (JSONString == null) {
            return;
        }

        String url = null;

        try {
            JSONObject jsonObject = new JSONObject(JSONString);
            JSONObject pageInfo = jsonObject.getJSONObject("pageInfo");
            int nbResults = pageInfo.getInt("totalResults");

            if (nbResults != 0) {
                JSONArray items = jsonObject.getJSONArray("items");

                if (items.length() != 0) {
                    JSONObject item = items.getJSONObject(0);
                    JSONObject snippet = item.getJSONObject("snippet");
                    JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                    JSONObject medium = thumbnails.getJSONObject("medium");
                    url = medium.getString("url");
                }
            }
        }
        catch (JSONException e) {
            // Ban ou pas de résultats
            e.printStackTrace();
        }

        if (url != null) {
            new ImageFromURLTask(this.music).execute(url);
        }
    }
}

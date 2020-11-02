package fr.r_thd.player.objects.task;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import fr.r_thd.player.objects.Music;

public class ImageFromTitleTask extends AsyncTask<String, Void, String> {
    private final Music music;

    private static final String API_KEY = "AIzaSyAn0oOafyYgufgOzAQdmHzAlCTKaUmZm0c";

    public ImageFromTitleTask(Music music) {
        this.music = music;
    }

    @Override
    protected String doInBackground(String... titles) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String title = titles[0];
        String apiKey = API_KEY;

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

    @Override
    protected void onPostExecute(String JSONString) {
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(JSONString);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JSONObject pageInfo;

        try {
            pageInfo = jsonObject.getJSONObject("pageInfo");
        }
        catch (JSONException e) {
            // Sûrement ban
            e.printStackTrace();
            return;
        }

        int nbResults;

        try {
            nbResults = pageInfo.getInt("totalResults");
        }
        catch (JSONException e) {
            // Sûrement ban
            e.printStackTrace();
            return;
        }

        if (nbResults == 0) {
            return;
        }

        JSONArray items;

        try {
            items = jsonObject.getJSONArray("items");
        }
        catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        if (items.length() == 0) {
            return;
        }

        JSONObject item;

        try {
            item = items.getJSONObject(0);
        }
        catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JSONObject snippet;

        try {
            snippet = item.getJSONObject("snippet");
        }
        catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JSONObject thumbnails;

        try {
            thumbnails = snippet.getJSONObject("thumbnails");
        }
        catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JSONObject medium;

        try {
            medium = thumbnails.getJSONObject("medium");
        }
        catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String url;

        try {
            url = medium.getString("url");
        }
        catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        new ImageFromURLTask(this.music).execute(url);
    }
}

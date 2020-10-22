package fr.r_thd.player.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.util.Pair;

import fr.r_thd.player.model.Music;
import fr.r_thd.player.storage.utility.DatabaseStorage;

public class MusicDatabaseStorage extends DatabaseStorage<Music> {
    public static final String TABLE_NAME = "music";

    private static final Pair<String, Integer> COL_ID = new Pair<>(BaseColumns._ID, 0);
    private static final Pair<String, Integer> COL_TITLE = new Pair<>("title", 1);
    private static final Pair<String, Integer> COL_URI = new Pair<>("uri", 2);
    private static final Pair<String, Integer> COL_PLAYLIST_ID = new Pair<>("playlistId", 3);

    private static MusicDatabaseStorage storage;

    public static MusicDatabaseStorage get(Context context) {
        if (storage == null) {
            storage = new MusicDatabaseStorage(context);
        }

        return storage;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "Music.db";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY, "
                        + COL_TITLE.first + " TEXT, "
                        + COL_URI.first + " TEXT,"
                        + COL_PLAYLIST_ID.first
                            + " INTEGER CONSTRAIN fk_music_playlist"
                            + " REFERENCES " + PlaylistDatabaseStorage.TABLE_NAME + "(" + BaseColumns._ID + "))";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Oops, osef des upgrades non ?
        }
    }

    private MusicDatabaseStorage(Context context) {
        super(new DatabaseHelper(context), TABLE_NAME);
    }

    @Override
    protected ContentValues objectToContentValues(int id, Music object) {
        ContentValues values = new ContentValues();
        values.put(COL_TITLE.first, object.getTitle());
        values.put(COL_URI.first, object.getUri());
        values.put(COL_PLAYLIST_ID.first, object.getPlaylistId());
        return values;
    }

    @Override
    protected Music cursorToObject(Cursor cursor) {
        return new Music(
                cursor.getInt(COL_ID.second),
                cursor.getInt(COL_PLAYLIST_ID.second),
                cursor.getString(COL_TITLE.second),
                cursor.getString(COL_URI.second)
        );
    }
}

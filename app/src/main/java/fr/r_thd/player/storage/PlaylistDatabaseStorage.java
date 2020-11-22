package fr.r_thd.player.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Pair;

import androidx.annotation.NonNull;

import fr.r_thd.player.objects.Playlist;
import fr.r_thd.player.storage.utility.DatabaseStorage;

/**
 * Database de playlist
 */
public class PlaylistDatabaseStorage extends DatabaseStorage<Playlist> {
    public static final String TABLE_NAME = "playlist";

    private static final Pair<String, Integer> COL_ID = new Pair<>(BaseColumns._ID, 0);
    private static final Pair<String, Integer> COL_NAME = new Pair<>("title", 1);

    private static PlaylistDatabaseStorage storage;

    /**
     * Contexte, obligatoire pour récupérer la liste des musiques
     */
    private final Context context;

    public static PlaylistDatabaseStorage get(Context context) {
        if (storage == null) {
            storage = new PlaylistDatabaseStorage(context);
        }

        return storage;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "Playlist.db";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + BaseColumns._ID + " INTEGER PRIMARY KEY, "
                        + COL_NAME.first + " TEXT)";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    }

    private PlaylistDatabaseStorage(Context context) {
        super(new DatabaseHelper(context), TABLE_NAME);
        this.context = context;
    }

    @NonNull
    @Override
    protected ContentValues objectToContentValues(int id, Playlist object) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME.first, object.getName());
        return values;
    }

    @NonNull
    @Override
    protected Playlist cursorToObject(@NonNull Cursor cursor) {
        int playlistId = cursor.getInt(COL_ID.second);
        return new Playlist(
                playlistId,
                cursor.getString(COL_NAME.second),
                MusicDatabaseStorage.get(context).findAll(playlistId)
        );
    }
}

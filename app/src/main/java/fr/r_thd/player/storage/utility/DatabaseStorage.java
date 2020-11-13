package fr.r_thd.player.storage.utility;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Database storage
 *
 * @param <T> Contenu
 */
public abstract class DatabaseStorage<T> implements Storage<T> {
    /**
     * Helper database
     */
    @NonNull
    protected final SQLiteOpenHelper helper;

    /**
     * Nom de la table
     */
    @NonNull
    protected final String table;

    /**
     * Constructeur
     *
     * @param helper Helper
     * @param table Nom de la table
     */
    public DatabaseStorage(@NonNull SQLiteOpenHelper helper, @NonNull String table) {
        this.helper = helper;
        this.table = table;
    }

    /**
     * Objet vers content values
     *
     * @param id Id
     * @param object Objet
     *
     * @return Content values
     */
    @NonNull
    protected abstract ContentValues objectToContentValues(int id, T object);

    /**
     * Cursor vers objet
     *
     * @param cursor Curseur
     *
     * @return Objet
     */
    @NonNull
    protected abstract T cursorToObject(@NonNull Cursor cursor);

    @Override
    public int insert(@NonNull T object) {
        return (int) helper.getWritableDatabase().insert(
                table,
                null,
                objectToContentValues(-1, object)
        );
    }

    @Override
    @NonNull
    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        Cursor cursor = helper.getReadableDatabase().query(
                table,
                null,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext())
            list.add(cursorToObject(cursor));
        cursor.close();
        return list;
    }

    @Override
    @Nullable
    public T find(int id) {
        Cursor cursor = helper.getReadableDatabase().query(
                table,
                null,
                BaseColumns._ID + " = ?",
                new String[]{"" + id},
                null,
                null,
                null
        );
        T object = cursor.moveToNext() ? cursorToObject(cursor) : null;
        cursor.close();
        return object;
    }

    @Override
    public int size() {
        Cursor cursor = helper.getReadableDatabase().query(
                table,
                null,
                null,
                null,
                null,
                null,
                null
        );
        int nb = cursor.getCount();
        cursor.close();
        return nb;
    }

    @Override
    public boolean update(int id, @NonNull T object) {
        return helper.getWritableDatabase().update(
                table,
                objectToContentValues(id, object),
                BaseColumns._ID + " = ?",
                new String[]{"" + id}
        ) != 0;
    }

    @Override
    public boolean delete(int id) {
        return (helper.getWritableDatabase().delete(table, BaseColumns._ID + " = ?", new String[]{"" + id})) != 0;
    }
}

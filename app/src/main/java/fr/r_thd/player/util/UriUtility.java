package fr.r_thd.player.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;

import java.io.File;

/**
 * Utility d'URI
 */
public abstract class UriUtility {
    /**
     * FileName from URI
     *
     * @param uri URI
     * @param contentResolver Content resolver
     *
     * @return File name
     */
    @NonNull
    public static String getFileName(@NonNull Uri uri, @NonNull ContentResolver contentResolver) {
        String result = null;

        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = contentResolver.query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }

        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');

            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }

        return result;
    }

    @NonNull
    public static String getPath(@NonNull Uri uri) {
        File file = new File(uri.getPath());
        final String[] split = file.getPath().split(":");
        return split[1];
    }
}

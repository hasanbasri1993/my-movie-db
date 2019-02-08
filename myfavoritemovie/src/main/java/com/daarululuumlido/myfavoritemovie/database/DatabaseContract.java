package com.daarululuumlido.myfavoritemovie.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public DatabaseContract() {
    }

    public static final String TABLE_NAME_FAVORITE = "table_favorite";
    static final String DATABASE_NAME = "dbmyfavoritemovie";
    static final int DATABASE_VERSION = 1;

    public static final class FavoriteMovieColumns implements BaseColumns {
        public static String IDMOVIE = "IDMOVIE";
        public static String TITLEMOVIE = "TITLEMOVIE";
        public static String OVERVIEWMOVIE = "OVERVIEWMOVIE";
        public static String POSTERMOVIE = "POSTERMOVIE";
    }

    public static final String AUTHORITY = "com.daarululuumlido.mymoviedb";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME_FAVORITE)
            .build();

    public static String getColomnString(Cursor cursor, String colomnName) {
        return cursor.getString(cursor.getColumnIndex(colomnName));
    }

    public static int getColomnInt(Cursor cursor, String colomnName) {
        return cursor.getInt(cursor.getColumnIndex(colomnName));
    }

    public static Long getColomnLong(Cursor cursor, String colomnName) {
        return cursor.getLong(cursor.getColumnIndex(colomnName));
    }


}

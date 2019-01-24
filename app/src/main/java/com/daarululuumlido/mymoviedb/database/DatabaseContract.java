package com.daarululuumlido.mymoviedb.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    public DatabaseContract(){}

    static final String TABLE_NAME_FAVORITE = "table_favorite";
    static final String DATABASE_NAME = "dbmyfavoritemovie";
    static final int DATABASE_VERSION = 1;

    public static final class FavoriteMovieColumns implements BaseColumns {
        public static String IDMOVIE = "IDMOVIE";
        public static String TITLEMOVIE = "TITLEMOVIE";
        public static String OVERVIEWMOVIE = "OVERVIEWMOVIE";
        public static String POSTERMOVIE = "POSTERMOVIE";
    }
}

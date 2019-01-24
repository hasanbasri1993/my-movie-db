package com.daarululuumlido.mymoviedb.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.daarululuumlido.mymoviedb.database.DatabaseContract.DATABASE_NAME;
import static com.daarululuumlido.mymoviedb.database.DatabaseContract.DATABASE_VERSION;
import static com.daarululuumlido.mymoviedb.database.DatabaseContract.FavoriteMovieColumns.IDMOVIE;
import static com.daarululuumlido.mymoviedb.database.DatabaseContract.FavoriteMovieColumns.OVERVIEWMOVIE;
import static com.daarululuumlido.mymoviedb.database.DatabaseContract.FavoriteMovieColumns.POSTERMOVIE;
import static com.daarululuumlido.mymoviedb.database.DatabaseContract.FavoriteMovieColumns.TITLEMOVIE;
import static com.daarululuumlido.mymoviedb.database.DatabaseContract.TABLE_NAME_FAVORITE;


public class DatabaseHelper extends SQLiteOpenHelper {


    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static String CREATE_TABLE_FAVORITE = "create table " + TABLE_NAME_FAVORITE +
            " (" + _ID + " integer primary key autoincrement, " +
            IDMOVIE + " text not null, " +
            TITLEMOVIE + " text not null, " +
            OVERVIEWMOVIE + " text not null, " +
            POSTERMOVIE + " text not null);";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FAVORITE);
        onCreate(db);
    }
}

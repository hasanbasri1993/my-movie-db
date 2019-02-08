package com.daarululuumlido.mymoviedb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.daarululuumlido.mymoviedb.model.MovieListModel;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.daarululuumlido.mymoviedb.database.DatabaseContract.FavoriteMovieColumns.IDMOVIE;
import static com.daarululuumlido.mymoviedb.database.DatabaseContract.FavoriteMovieColumns.OVERVIEWMOVIE;
import static com.daarululuumlido.mymoviedb.database.DatabaseContract.FavoriteMovieColumns.POSTERMOVIE;
import static com.daarululuumlido.mymoviedb.database.DatabaseContract.FavoriteMovieColumns.TITLEMOVIE;


public class FavoriteHelper {

    private static String TABLE_NAME_FAVORITE = DatabaseContract.TABLE_NAME_FAVORITE;

    private Context context;
    private DatabaseHelper dataBaseHelper;
    private SQLiteDatabase database;

    public FavoriteHelper(Context context) {
        this.context = context;
    }

    public FavoriteHelper open() throws SQLException {
        dataBaseHelper = new DatabaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dataBaseHelper.close();
    }

    public long insertFavoriteMovie(MovieListModel movieListModel) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(IDMOVIE, movieListModel.getId());
        initialValues.put(TITLEMOVIE, movieListModel.getTitle());
        initialValues.put(OVERVIEWMOVIE, movieListModel.getOverview());
        initialValues.put(POSTERMOVIE, movieListModel.getPoster_path());
        return database.insert(TABLE_NAME_FAVORITE, null, initialValues);
    }


    public int deleteFavoriteIdMovie(String id) {
        return database.delete(TABLE_NAME_FAVORITE, IDMOVIE + " = '" + id + "'", null);
    }

    public boolean checkAvailability(String id) {
        Cursor cursor = database.query(TABLE_NAME_FAVORITE, null, IDMOVIE + " = ?", new String[]{id}, null, null, _ID + " ASC", null);
        cursor.moveToFirst();
        boolean isTrue = false;
        if (cursor.getCount() > 0) {
            isTrue = true;
        }
        cursor.close();
        return isTrue;
    }

    public ArrayList<MovieListModel> getAllData() {
        Cursor cursor = database.query(TABLE_NAME_FAVORITE, null, null, null, null, null, TITLEMOVIE + " ASC", null);
        cursor.moveToFirst();
        ArrayList<MovieListModel> arrayList = new ArrayList<>();
        MovieListModel movieListModel;
        if (cursor.getCount() > 0) {
            do {
                movieListModel = new MovieListModel();
                movieListModel.setId(cursor.getString(cursor.getColumnIndexOrThrow(IDMOVIE)));
                movieListModel.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLEMOVIE)));
                movieListModel.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEWMOVIE)));
                movieListModel.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(POSTERMOVIE)));
                arrayList.add(movieListModel);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(TABLE_NAME_FAVORITE, null
                , IDMOVIE + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null
        );
    }

    public Cursor queryProvider() {
        return database.query(TABLE_NAME_FAVORITE, null
                , null
                , null
                , null
                , null
                , TITLEMOVIE + " ASC"
        );
    }

    public long insertProvider(ContentValues values) {
        return database.insert(TABLE_NAME_FAVORITE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(TABLE_NAME_FAVORITE, values, IDMOVIE + " = '" + id + "'", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(TABLE_NAME_FAVORITE, IDMOVIE + " = '" + id + "'",null);
    }

}

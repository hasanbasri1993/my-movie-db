package com.daarululuumlido.myfavoritemovie;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.daarululuumlido.myfavoritemovie.adapter.MovieFavoriteAdapter;

import static com.daarululuumlido.myfavoritemovie.database.DatabaseContract.CONTENT_URI;
import static com.daarululuumlido.myfavoritemovie.database.DatabaseContract.FavoriteMovieColumns.IDMOVIE;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private MovieFavoriteAdapter movieAdapter;
    private final int LOAD_MOVIE_ID = 110;

    GridView gvFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gvFavorite = findViewById(R.id.gv_favorite);
        movieAdapter = new MovieFavoriteAdapter(this, null, true);
        gvFavorite.setAdapter(movieAdapter);
        gvFavorite.setOnItemClickListener(this);
        getSupportLoaderManager().initLoader(LOAD_MOVIE_ID, null, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOAD_MOVIE_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        movieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor cursor = (Cursor) movieAdapter.getItem(i);

        int id = cursor.getInt(cursor.getColumnIndexOrThrow(IDMOVIE));
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
        intent.setData(Uri.parse(CONTENT_URI + "/" + id));
        startActivity(intent);
    }

}
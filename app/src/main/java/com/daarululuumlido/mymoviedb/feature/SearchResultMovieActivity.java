package com.daarululuumlido.mymoviedb.feature;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.daarululuumlido.mymoviedb.BuildConfig;
import com.daarululuumlido.mymoviedb.R;
import com.daarululuumlido.mymoviedb.adapter.MovieAdapter;
import com.daarululuumlido.mymoviedb.database.FavoriteHelper;
import com.daarululuumlido.mymoviedb.model.MovieListModel;
import com.daarululuumlido.mymoviedb.tools.ItemClickSupport;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.daarululuumlido.mymoviedb.database.DatabaseContract.FavoriteMovieColumns.IDMOVIE;

public class SearchResultMovieActivity extends AppCompatActivity {
    @BindView(R.id.listView)
    RecyclerView listView;

    @BindView(R.id.progressbar_find_movie)
    ProgressBar progressBarFindMovie;
    FavoriteHelper favoriteHelper;
    MovieAdapter movieAdapter;
    List<MovieListModel> MovieListes = new ArrayList<>();
    private static final String INTENTS_QUERY = "search";
    private static final String INTENTS_TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_movie);
        ButterKnife.bind(this);

        favoriteHelper = new FavoriteHelper(this);
        movieAdapter = new MovieAdapter(this);

        movieAdapter.notifyDataSetChanged();
        showRecyclerList();
        if (getIntent() != null) {
            if (getIntent().getStringExtra(INTENTS_TAG).equals("search")) {
                String q = getIntent().getStringExtra(INTENTS_QUERY);
                getListMovie(q);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle(getString(R.string.search_titile_tab) + ": " + q);
                }
            } else {
                getFavorite();
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle(getString(R.string.favorite_title_tab));
                }
            }
        }
    }

    private void showRecyclerList() {
        listView.setLayoutManager(new GridLayoutManager(this, 3));
        listView.setHasFixedSize(true);

        MovieAdapter listMovieAdapter = new MovieAdapter(this);
        listMovieAdapter.setListMovie(MovieListes);
        listView.setAdapter(listMovieAdapter);

        ItemClickSupport.addTo(listView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                Intent moveWithDataIntent = new Intent(v.getContext(), MovieDetailActivity.class);
                moveWithDataIntent.putExtra(IDMOVIE, MovieListes.get(position).getId());
                v.getContext().startActivity(moveWithDataIntent);
            }
        });
    }

    private void getFavorite() {
        progressBarFindMovie.setVisibility(View.VISIBLE);
        MovieListes.clear();
        try {
            favoriteHelper.open();
            MovieListes = favoriteHelper.getAllData();
            favoriteHelper.close();

            movieAdapter.setListMovie(MovieListes);
            progressBarFindMovie.setVisibility(View.GONE);
            Log.d("LOG", "onSuccess: Selesai..... getFavorite()");
        } catch (Exception e) {
            Log.d("LOG", "onSuccess: Gagal..... getFavorite()");
            e.printStackTrace();
            progressBarFindMovie.setVisibility(View.GONE);
        }
    }

    private void getListMovie(String movieName) {
        progressBarFindMovie.setVisibility(View.VISIBLE);
        MovieListes.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = BuildConfig.BASE_URL + "search/movie?api_key=" + BuildConfig.TMDB_API_KEY + "&language=en-US&query=" + movieName;
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String hasil = new String(responseBody);
                    JSONObject responseObject = new JSONObject(hasil);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movies = list.getJSONObject(i);
                        MovieListModel MovieList = new MovieListModel(movies);
                        MovieListes.add(MovieList);
                    }
                    movieAdapter.setListMovie(MovieListes);
                    progressBarFindMovie.setVisibility(View.GONE);
                    Log.d("LOG", "onSuccess: Selesai....." + MovieListes.get(0));
                } catch (Exception e) {
                    Log.d("LOG", "onSuccess: Gagal.....");
                    e.printStackTrace();
                    progressBarFindMovie.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("LOG", "onFailure: Gagal.....");
                progressBarFindMovie.setVisibility(View.GONE);
            }
        });
    }

}

package com.daarululuumlido.mymoviedb.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daarululuumlido.mymoviedb.BuildConfig;
import com.daarululuumlido.mymoviedb.R;
import com.daarululuumlido.mymoviedb.database.FavoriteHelper;
import com.daarululuumlido.mymoviedb.model.GenreListModel;
import com.daarululuumlido.mymoviedb.model.MovieListModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.daarululuumlido.mymoviedb.database.DatabaseContract.FavoriteMovieColumns.IDMOVIE;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    final ArrayList<GenreListModel> GenreLists = new ArrayList<>();
    final ArrayList<MovieListModel> MovieLists = new ArrayList<>();
    private Menu menuItem;
    String MOVIE_ID = "";
    Boolean isFavorite = false;

    FavoriteHelper favoriteHelper;

    @BindView(R.id.tv_title_detail)
    TextView textViewTitle;

    @BindView(R.id.tv_language_detail)
    TextView textViewLanguage;

    @BindView(R.id.tv_genres_detail)
    TextView textViewGenres;

    @BindView(R.id.tv_overview_detail)
    TextView textViewOverview;

    @BindView(R.id.tv_rating_detail)
    TextView textViewVote;

    @BindView(R.id.tv_duration_detail)
    TextView textViewRuntime;

    @BindView(R.id.tv_release_detail)
    TextView textViewRelease;

    @BindView(R.id.tv_tagline_detail)
    TextView textViewTagline;

    @BindView(R.id.img_poster_detail)
    ImageView imageViewPoster;

    @BindView(R.id.progressbar_detail_movie)
    ProgressBar progressBarDetailMovie;

    @BindView(R.id.ln_detail)
    LinearLayout linearLayoutDetailMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.detail_movie_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        MOVIE_ID = getIntent().getStringExtra(IDMOVIE);
        getDetailMovie(MOVIE_ID);

        favoriteHelper = new FavoriteHelper(this);
        favoriteHelper.open();
        favoriteState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        menuItem = menu;
        menuItem.setGroupVisible(0, false);
        setFavorite();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.add_favorite) {
            if (isFavorite) {
                removeFavorite();
            } else {
                addFavorite();
            }
            isFavorite = !isFavorite;
            setFavorite();
        }
        return super.onOptionsItemSelected(item);
    }

    void favoriteState() {
        isFavorite = favoriteHelper.checkAvailability(MOVIE_ID);
    }

    void addFavorite() {
        try {
            for (MovieListModel model : MovieLists) {
                favoriteHelper.insertFavoriteMovie(model);
            }
            Toast.makeText(this, "Berhasil ditambah favorite", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            // Jika gagal maka do nothing
        }
    }

    void removeFavorite() {
        favoriteHelper.deleteFavoriteIdMovie(MOVIE_ID);
        Toast.makeText(this, "Berhasil dihapus dari favorite", Toast.LENGTH_LONG).show();


    }


    void setFavorite() {
        if (isFavorite)
            menuItem.getItem(0).setIcon(R.drawable.ic_added_to_favorites);
        else
            menuItem.getItem(0).setIcon(R.drawable.ic_add_to_favorites);
    }


    public void getDetailMovie(String idMovie) {
        progressBarDetailMovie.setVisibility(View.VISIBLE);
        linearLayoutDetailMovie.setVisibility(View.GONE);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = BuildConfig.BASE_URL + "movie/" + idMovie + "?api_key=" + API_KEY;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);

                try {
                    JSONObject responseObject = new JSONObject(result);
                    MovieListModel MovieList = new MovieListModel(responseObject);
                    MovieLists.add(MovieList);

                    JSONArray list = responseObject.getJSONArray("genres");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject genres = list.getJSONObject(i);
                        GenreListModel GenreList = new GenreListModel(genres);
                        GenreLists.add(GenreList);
                    }
                    String original_title = responseObject.getString("original_title");
                    String original_language = responseObject.getString("original_language");
                    String overview = responseObject.getString("overview");
                    String vote_average = responseObject.getString("vote_average");
                    String runtime = responseObject.getString("runtime");
                    String poster_path = responseObject.getString("poster_path");
                    String tagline = responseObject.getString("tagline");
                    String release_date = responseObject.getString("release_date");


                    StringBuilder sb = new StringBuilder();
                    for (GenreListModel u : GenreLists) {
                        sb.append(" " + u.name + " ");
                    }

                    textViewTitle.setText(original_title);
                    textViewGenres.setText(sb.toString());
                    textViewOverview.setText(overview);
                    textViewVote.setText(vote_average);
                    textViewRuntime.setText(runtime);
                    textViewLanguage.setText(original_language);
                    textViewTagline.setText(tagline);
                    textViewRelease.setText(release_date);

                    Glide
                            .with(MovieDetailActivity.this)
                            .load(BuildConfig.IMAGE_URL + poster_path)
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.ic_placeholder))
                            .into(imageViewPoster);
                    progressBarDetailMovie.setVisibility(View.GONE);
                    linearLayoutDetailMovie.setVisibility(View.VISIBLE);
                    menuItem.setGroupVisible(0, true);


                } catch (Exception e) {
                    progressBarDetailMovie.setVisibility(View.GONE);
                    linearLayoutDetailMovie.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBarDetailMovie.setVisibility(View.GONE);
                linearLayoutDetailMovie.setVisibility(View.VISIBLE);
            }
        });
    }

}

package com.daarululuumlido.mymoviedb.Feature;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daarululuumlido.mymoviedb.BuildConfig;
import com.daarululuumlido.mymoviedb.Model.GenreList;
import com.daarululuumlido.mymoviedb.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String API_KEY = BuildConfig.TMDB_API_KEY;

    final ArrayList<GenreList> GenreLists = new ArrayList<>();

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

    @BindView(R.id.tv_tagline_detail)
    TextView textViewTagline;

    @BindView(R.id.img_poster_detail)
    ImageView imageViewPoster;

    @BindView(R.id.progressbar_detail_movie)
    ProgressBar progressBarDetailMovie;

    @BindView(R.id.ln_detail)
    LinearLayout linearLayoutDetailMovie;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.detail_movie_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        String MOVIE_ID = getIntent().getStringExtra("MOVIE_ID");
        getDetailMovie(MOVIE_ID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDetailMovie(String idMovie) {
        progressBarDetailMovie.setVisibility(View.VISIBLE);
        linearLayoutDetailMovie.setVisibility(View.GONE);
        Log.d("LOG", "getDetailMovie: Mulai....., Dengan idMOvie : " + idMovie);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = BuildConfig.BASE_URL + "/" + idMovie + "?api_key=" + API_KEY;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);

                try {
                    JSONObject responseObject = new JSONObject(result);

                    String original_title = responseObject.getString("original_title");
                    String original_language = responseObject.getString("original_language");
                    JSONArray list = responseObject.getJSONArray("genres");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject genres = list.getJSONObject(i);
                        GenreList GenreList = new GenreList(genres);
                        GenreLists.add(GenreList);
                    }
                    String genres = responseObject.getJSONArray("genres").getJSONObject(0).getString("name");
                    String overview = responseObject.getString("overview");
                    String vote_average = responseObject.getString("vote_average");
                    String runtime = responseObject.getString("runtime");
                    String poster_path = responseObject.getString("poster_path");
                    String tagline = responseObject.getString("tagline");


                    textViewLanguage.setText(original_language);


                    StringBuilder sb = new StringBuilder();
                    for (GenreList u : GenreLists) {
                        sb.append(" " + u.name + " ");
                    }

                    textViewTitle.setText(original_title);
                    textViewGenres.setText(sb.toString());
                    textViewOverview.setText(overview);
                    textViewVote.setText(vote_average);
                    textViewRuntime.setText(runtime);
                    textViewTagline.setText(tagline);

                    Glide
                            .with(MovieDetailActivity.this)
                            .load(BuildConfig.IMAGE_URL + poster_path)
                            .into(imageViewPoster);

                    Log.d("LOG", "onSuccess: Selesai.....");
                    progressBarDetailMovie.setVisibility(View.GONE);
                    linearLayoutDetailMovie.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    Log.d("LOG", "onSuccess: Gagal.....");
                    progressBarDetailMovie.setVisibility(View.GONE);
                    linearLayoutDetailMovie.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("LOG", "onFailure: Gagal.....");
                progressBarDetailMovie.setVisibility(View.GONE);
                linearLayoutDetailMovie.setVisibility(View.VISIBLE);
            }
        });
    }
}

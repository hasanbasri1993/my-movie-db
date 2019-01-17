package com.daarululuumlido.mymoviedb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daarululuumlido.mymoviedb.SetterList.GenreList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieDetail extends AppCompatActivity {

    private static final String API_KEY = BuildConfig.TMDB_API_KEY;

    final ArrayList<GenreList> GenreLists = new ArrayList<>();

    TextView textViewTitle, textViewLanguage, textViewGenres, textViewOverview, textViewVote, textViewRuntime, textViewTagline;
    ImageView imageViewPoster;
    ProgressBar progressBarDetailMovie;
    LinearLayout linearLayoutDetailMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        progressBarDetailMovie = findViewById(R.id.progressbar_detail_movie);
        linearLayoutDetailMovie = findViewById(R.id.ln_detail);

        String MOVIE_ID = getIntent().getStringExtra("MOVIE_ID");
        getDetailMovie(MOVIE_ID);
    }

    private void getDetailMovie(String idMovie) {
        progressBarDetailMovie.setVisibility(View.VISIBLE);
        linearLayoutDetailMovie.setVisibility(View.GONE);
        Log.d("LOG", "getDetailMovie: Mulai....., Dengan idMOvie : " + idMovie);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.themoviedb.org/3/movie/" + idMovie + "?api_key=" + API_KEY;
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

                    textViewTitle = findViewById(R.id.tv_title_detail);
                    textViewTagline = findViewById(R.id.tv_tagline_detail);
                    textViewGenres = findViewById(R.id.tv_genres_detail);
                    textViewOverview = findViewById(R.id.tv_overview_detail);
                    textViewVote = findViewById(R.id.tv_rating_detail);
                    textViewRuntime = findViewById(R.id.tv_duration_detail);
                    textViewLanguage = findViewById(R.id.tv_language_detail);
                    imageViewPoster = findViewById(R.id.img_poster_detail);
                    textViewTitle.setText(original_title);
                    textViewLanguage.setText(original_language);


                    StringBuilder sb = new StringBuilder();
                    for (GenreList u : GenreLists) {

                        sb.append(" " + u.name + " ");

                    }

                    textViewGenres.setText(sb.toString());
                    textViewOverview.setText(overview);
                    textViewVote.setText(vote_average);
                    textViewRuntime.setText(runtime);
                    textViewTagline.setText(tagline);

                    Glide
                            .with(MovieDetail.this)
                            .load("http://image.tmdb.org/t/p/w92" + poster_path)
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

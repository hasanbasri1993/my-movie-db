package com.daarululuumlido.mymoviedb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.daarululuumlido.mymoviedb.SetterList.MovieList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieFind extends AppCompatActivity {

    ListView listView;
    MovieAdapter adapter;
    EditText edtMovie;
    Button btnSearch;
    ProgressBar progressBarFindMovie;

    static final String API_KEY = BuildConfig.TMDB_API_KEY;
    final ArrayList<MovieList> MovieListes = new ArrayList<>();
    private static final String STATE_RESULT = "state_result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_movie);

        adapter = new MovieAdapter(this);
        adapter.notifyDataSetChanged();
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        edtMovie = findViewById(R.id.edt_movie);
        btnSearch = findViewById(R.id.btn_find_movie);
        progressBarFindMovie = findViewById(R.id.progressbar_find_movie);

        btnSearch.setOnClickListener(myListener);
        getToRatedMovie();

        if (savedInstanceState != null) {
            String result = savedInstanceState.getString(STATE_RESULT);
            getListMovie(result);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_RESULT, edtMovie.getText().toString());
    }

    private void getToRatedMovie() {
        progressBarFindMovie.setVisibility(View.VISIBLE);

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY;
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String hasil = new String(responseBody);
                    JSONObject responseObject = new JSONObject(hasil);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movies = list.getJSONObject(i);
                        MovieList MovieList = new MovieList(movies);
                        MovieListes.add(MovieList);
                    }
                    adapter.setData(MovieListes);

                    Log.d("LOG", "onSuccess: Selesai.....");
                    progressBarFindMovie.setVisibility(View.GONE);


                } catch (Exception e) {
                    Log.d("LOG", "onSuccess: Gagal.....");
                    progressBarFindMovie.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("LOG", "onFailure: Gagal.....");
                progressBarFindMovie.setVisibility(View.GONE);
            }
        });

    }

    private void getListMovie(String movieName) {
        progressBarFindMovie.setVisibility(View.VISIBLE);
        MovieListes.clear();

        Log.d("LOG", "getListMovie: Mulai....., Dengan idMOvie : " + movieName);
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.themoviedb.org/3/search/movie?api_key=" +
                API_KEY + "&language=en-US&query=" + movieName;
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);

                try {
                    String hasil = new String(responseBody);
                    JSONObject responseObject = new JSONObject(hasil);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movies = list.getJSONObject(i);
                        MovieList MovieList = new MovieList(movies);
                        MovieListes.add(MovieList);
                    }
                    adapter.setData(MovieListes);
                    progressBarFindMovie.setVisibility(View.GONE);
                    Log.d("LOG", "onSuccess: Selesai.....");


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


    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String movie = edtMovie.getText().toString();

            // Jika edit text-nya kosong maka do nothing
            if (TextUtils.isEmpty(movie)) return;
            getListMovie(movie);
        }
    };
}
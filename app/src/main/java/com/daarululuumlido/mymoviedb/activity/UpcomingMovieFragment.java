package com.daarululuumlido.mymoviedb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.daarululuumlido.mymoviedb.BuildConfig;
import com.daarululuumlido.mymoviedb.R;
import com.daarululuumlido.mymoviedb.adapter.MovieAdapter;
import com.daarululuumlido.mymoviedb.model.MovieListModel;
import com.daarululuumlido.mymoviedb.tools.ItemClickSupport;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.daarululuumlido.mymoviedb.database.DatabaseContract.FavoriteMovieColumns.IDMOVIE;

public class UpcomingMovieFragment extends Fragment {


    @BindView(R.id.listViewUpcomingMovie)
    RecyclerView listView;

    @BindView(R.id.progressbar_upcoming_movie)
    ProgressBar progressBarUpcomingMovie;

    MovieAdapter adapter;
    final ArrayList<MovieListModel> MovieListes = new ArrayList<>();

    public UpcomingMovieFragment() {
        // Required empty public constructor
    }

    public static UpcomingMovieFragment newInstance() {
        UpcomingMovieFragment fragment = new UpcomingMovieFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_upcoming_movie, container, false);
        ButterKnife.bind(this, root);
        adapter = new MovieAdapter(getContext());
        adapter.notifyDataSetChanged();
        showRecyclerList();
        getUpcomingMovie();
        return root;
    }

    private void showRecyclerList() {
        listView.setLayoutManager(new GridLayoutManager(getContext(),3));
        MovieAdapter listMovieAdapter = new MovieAdapter(getContext());
        listMovieAdapter.setListMovie(MovieListes);
        listView.setAdapter(listMovieAdapter);

        ItemClickSupport.addTo(listView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent moveWithDataIntent = new Intent(v.getContext(), MovieDetailActivity.class);
                moveWithDataIntent.putExtra(IDMOVIE, MovieListes.get(position).getId());
                v.getContext().startActivity(moveWithDataIntent);
                Log.d("LOG", "onSuccess: Selesai....." + MovieListes);

            }
        });
    }


    private void getUpcomingMovie() {
        progressBarUpcomingMovie.setVisibility(View.VISIBLE);
        MovieListes.clear();

        AsyncHttpClient client = new AsyncHttpClient();
        String url = BuildConfig.BASE_URL + "movie/upcoming?api_key=" + BuildConfig.TMDB_API_KEY;
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
                    adapter.setListMovie(MovieListes);
                    //adapter.setData(MovieListes);

                    Log.d("LOG", "onSuccess: Selesai.....");
                    progressBarUpcomingMovie.setVisibility(View.GONE);


                } catch (Exception e) {
                    Log.d("LOG", "onSuccess: Gagal.....");
                    progressBarUpcomingMovie.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("LOG", "onFailure: Gagal.....");
                progressBarUpcomingMovie.setVisibility(View.GONE);
            }
        });

    }

}

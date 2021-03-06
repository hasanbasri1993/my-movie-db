package com.daarululuumlido.mymoviedb.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.daarululuumlido.mymoviedb.BuildConfig;
import com.daarululuumlido.mymoviedb.adapter.MovieAdapter;
import com.daarululuumlido.mymoviedb.model.MovieListModel;
import com.daarululuumlido.mymoviedb.R;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class NowPlayingMovieFragment extends Fragment {

    @BindView(R.id.listViewNowPlayingMovie)
    RecyclerView listView;

    @BindView(R.id.progressbar_playing_movie)
    ProgressBar progressBarNowPlayingMovie;

    MovieAdapter adapter;
    ArrayList<MovieListModel> MovieListes = new ArrayList<>();
    int columns = 0;

    public NowPlayingMovieFragment() {
        // Required empty public constructor
    }

    public static NowPlayingMovieFragment newInstance() {
        NowPlayingMovieFragment fragment = new NowPlayingMovieFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_nowplaying_movie, container, false);
        ButterKnife.bind(this, root);

        MovieListes = new ArrayList<>();
        showRecyclerList();

        if(savedInstanceState!=null){
            MovieListes = savedInstanceState.getParcelableArrayList("now");
            adapter.setListMovie(MovieListes);
            listView.setAdapter(adapter);
        }else{
            getNowPlayingMovie();
        }

        return root;
    }

    private void showRecyclerList() {
        columns = getResources().getInteger(R.integer.collumn_count);
        adapter = new MovieAdapter(getActivity());

        listView.setLayoutManager(new GridLayoutManager(getContext(),columns));
        listView.setHasFixedSize(true);
        listView.setItemAnimator(new DefaultItemAnimator());

        ItemClickSupport.addTo(listView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent moveWithDataIntent = new Intent(v.getContext(), MovieDetailActivity.class);
                moveWithDataIntent.putExtra(IDMOVIE, MovieListes.get(position).getId());
                v.getContext().startActivity(moveWithDataIntent);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("now", MovieListes);
    }

    private void getNowPlayingMovie() {
        progressBarNowPlayingMovie.setVisibility(View.VISIBLE);
        MovieListes.clear();

        AsyncHttpClient client = new AsyncHttpClient();
        String url = BuildConfig.BASE_URL + "movie/now_playing?api_key=" + BuildConfig.TMDB_API_KEY;
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
                    listView.setAdapter(adapter);

                    Log.d("LOG", "onSuccess: Selesai.....");
                    progressBarNowPlayingMovie.setVisibility(View.GONE);


                } catch (Exception e) {
                    Log.d("LOG", "onSuccess: Gagal.....");
                    progressBarNowPlayingMovie.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("LOG", "onFailure: Gagal.....");
                progressBarNowPlayingMovie.setVisibility(View.GONE);
            }
        });

    }

}

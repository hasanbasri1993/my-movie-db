package com.daarululuumlido.mymoviedb.Feature;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daarululuumlido.mymoviedb.BuildConfig;
import com.daarululuumlido.mymoviedb.Model.MovieList;
import com.daarululuumlido.mymoviedb.R;
import com.daarululuumlido.mymoviedb.Tools.ItemClickSupport;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class FindMovieFragment extends Fragment {
    @BindView(R.id.listView)
    RecyclerView listView;

    @BindView(R.id.edt_movie)
    EditText edtMovie;

    @BindView(R.id.btn_find_movie)
    Button btnSearch;

    @BindView(R.id.progressbar_find_movie)
    ProgressBar progressBarFindMovie;

    MovieAdapter adapter;
    final ArrayList<MovieList> MovieListes = new ArrayList<>();
    private static final String STATE_RESULT = "state_result";


    public FindMovieFragment() {
        // Required empty public constructor
    }

    public static FindMovieFragment newInstance() {
        FindMovieFragment fragment = new FindMovieFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_find_movie, container, false);
        ButterKnife.bind(this, root);

        adapter = new MovieAdapter(getContext());
        adapter.notifyDataSetChanged();
        showRecyclerList();

        btnSearch.setOnClickListener(myListener);
        getToRatedMovie();

        if (savedInstanceState != null) {
            String result = savedInstanceState.getString(STATE_RESULT);
            getListMovie(result);
        }

        return root;
    }

    private void showRecyclerList() {
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        MovieAdapter listMovieAdapter = new MovieAdapter(getContext());
        listMovieAdapter.setListMovie(MovieListes);
        listView.setAdapter(listMovieAdapter);

        ItemClickSupport.addTo(listView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                Intent moveWithDataIntent = new Intent(v.getContext(), MovieDetailActivity.class);
                moveWithDataIntent.putExtra("MOVIE_ID", MovieListes.get(position).getId());
                v.getContext().startActivity(moveWithDataIntent);
                Log.d("LOG", "iDMovie : " + MovieListes.get(position).getId());
            }
        });
    }

    private void getToRatedMovie() {
        progressBarFindMovie.setVisibility(View.VISIBLE);
        MovieListes.clear();

        AsyncHttpClient client = new AsyncHttpClient();
        String url = BuildConfig.BASE_URL + "/top_rated?api_key=" + BuildConfig.TMDB_API_KEY;
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
                    adapter.setListMovie(MovieListes);

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
        AsyncHttpClient client = new AsyncHttpClient();
        String url = BuildConfig.BASE_URL + "?api_key=" + BuildConfig.TMDB_API_KEY + "&language=en-US&query=" + movieName;
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
                    adapter.setListMovie(MovieListes);
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
            if (TextUtils.isEmpty(movie)) return;
            getListMovie(movie);
        }
    };

    public static class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.CategoryViewHolder> {
        public Context context;
        public ArrayList<MovieList> listMovie;

        public ArrayList<MovieList> getListMovie() {
            return listMovie;
        }

        void setListMovie(ArrayList<MovieList> listMovie) {
            this.listMovie = listMovie;
        }

        MovieAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list, viewGroup, false);
            return new CategoryViewHolder(itemRow);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int position) {
            categoryViewHolder.textViewTitle.setText(getListMovie().get(position).getTitle());
            categoryViewHolder.textViewOverview.setText(getListMovie().get(position).getOverview());

            Glide.with(context)
                    .load(BuildConfig.IMAGE_URL + getListMovie().get(position).getPoster_path())
                    .into(categoryViewHolder.imageViewPoster);
        }

        @Override
        public int getItemCount() {
            return getListMovie().size();
        }

        class CategoryViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.tv_title)
            TextView textViewTitle;
            @BindView(R.id.tv_overview)
            TextView textViewOverview;
            @BindView(R.id.img_poster)
            ImageView imageViewPoster;


            CategoryViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

            }
        }
    }
}

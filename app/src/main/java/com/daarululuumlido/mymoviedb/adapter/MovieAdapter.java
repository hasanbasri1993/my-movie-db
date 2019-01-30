package com.daarululuumlido.mymoviedb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daarululuumlido.mymoviedb.BuildConfig;
import com.daarululuumlido.mymoviedb.R;
import com.daarululuumlido.mymoviedb.activity.MovieDetailActivity;
import com.daarululuumlido.mymoviedb.model.MovieListModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ItemMovieViewHolder> {
    private List<MovieListModel> movieResultList = new ArrayList<>();
    private Context context;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ItemMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemMovieViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list,
                        parent, false)
        );
    }

    public void setListMovie(List<MovieListModel> movieResult) {
        this.movieResultList = movieResult;
    }

    public List<MovieListModel> getListMovie() {
        return movieResultList;
    }

    @Override
    public void onBindViewHolder(ItemMovieViewHolder holder, int position) {
        holder.bindView(movieResultList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieResultList.size();
    }


    //View Holder
    class ItemMovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_poster)
        ImageView item_poster;
        @BindView(R.id.tv_title)
        TextView item_title;
        @BindView(R.id.tv_overview)
        TextView item_overview;

        ItemMovieViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bindView(final MovieListModel movieResult) {
            item_title.setText(movieResult.getTitle());
            item_overview.setText(movieResult.getOverview());

            Glide.with(itemView.getContext())
                    .load(BuildConfig.IMAGE_URL + movieResult.getPoster_path())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_placeholder))
                    .into(item_poster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), MovieDetailActivity.class);
                    intent.putExtra("MOVIE_ID", movieResult.getId());
                    itemView.getContext().startActivity(intent);
                }
            });

        }


    }

}

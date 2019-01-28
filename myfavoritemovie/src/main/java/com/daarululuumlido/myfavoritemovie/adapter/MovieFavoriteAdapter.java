package com.daarululuumlido.myfavoritemovie.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daarululuumlido.myfavoritemovie.BuildConfig;
import com.daarululuumlido.myfavoritemovie.R;

import static com.daarululuumlido.myfavoritemovie.database.DatabaseContract.FavoriteMovieColumns.OVERVIEWMOVIE;
import static com.daarululuumlido.myfavoritemovie.database.DatabaseContract.FavoriteMovieColumns.POSTERMOVIE;
import static com.daarululuumlido.myfavoritemovie.database.DatabaseContract.FavoriteMovieColumns.TITLEMOVIE;
import static com.daarululuumlido.myfavoritemovie.database.DatabaseContract.getColomnString;

public class MovieFavoriteAdapter extends CursorAdapter {

    public MovieFavoriteAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list, viewGroup, false);
        return view;
    }


    @Override
    public Cursor getCursor() {
        return super.getCursor();
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor != null) {
            TextView tvTitle = view.findViewById(R.id.tv_title);
            TextView tvDate = view.findViewById(R.id.tv_overview);
            ImageView imgPoster = view.findViewById(R.id.img_poster);

            tvTitle.setText(getColomnString(cursor, TITLEMOVIE));
            tvDate.setText(getColomnString(cursor, OVERVIEWMOVIE));
            Glide.with(view.getContext())
                    .load(BuildConfig.IMAGE_URL + getColomnString(cursor, POSTERMOVIE))
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_placeholder))
                    .into(imgPoster);
        }
    }
}
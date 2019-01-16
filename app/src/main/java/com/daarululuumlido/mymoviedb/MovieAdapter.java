package com.daarululuumlido.mymoviedb;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daarululuumlido.mymoviedb.SetterList.MovieList;

import java.util.ArrayList;

public class MovieAdapter extends BaseAdapter {

    private ArrayList<MovieList> mData = new ArrayList<>();
    private LayoutInflater mInflater;

    public MovieAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<MovieList> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * Gunakan method ini jika ada 1 data yang ditambahkan
     *
     * @param item data baru yang akan ditambahkan
     */
    public void addItem(final MovieList item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        // Pengecekan null, diperlukan agar tidak terjadi force close ketika datanya null
        // return 0 sehingga adapter tidak akan menampilkan apapun
        if (mData == null) return 0;

        // Jika tidak null, maka return banyaknya jumlah data yang ada
        return mData.size();
    }

    @Override
    public MovieList getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.movie_list, null);
            holder.textViewTitle = (TextView) convertView.findViewById(R.id.tv_title);
            holder.textViewOverview = (TextView) convertView.findViewById(R.id.tv_overview);
            holder.imageViewPoster = (ImageView) convertView.findViewById(R.id.img_poster);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textViewTitle.setText(mData.get(position).getTitle());
        holder.textViewOverview.setText(mData.get(position).getOverview());
        Glide
                .with(convertView.getContext())
                .load("http://image.tmdb.org/t/p/w92" + mData.get(position).getPoster_path())
                .into(holder.imageViewPoster);

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent moveWithDataIntent = new Intent(v.getContext(), MovieDetail.class);
                moveWithDataIntent.putExtra("MOVIE_ID", mData.get(position).getId());
                v.getContext().startActivity(moveWithDataIntent);
                Log.d("LOG", "iDMovie : " + mData.get(position).getId());

            }
        });

        return convertView;
    }


    private static class ViewHolder {
        TextView textViewTitle;
        TextView textViewOverview;
        ImageView imageViewPoster;
    }


}

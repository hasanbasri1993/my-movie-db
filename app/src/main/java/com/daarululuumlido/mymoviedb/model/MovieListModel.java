package com.daarululuumlido.mymoviedb.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class MovieListModel implements Parcelable {

    private String id;
    private String title;
    private String poster_path;
    private String overview;
    private String release_date;

    public MovieListModel(JSONObject object) {

        try {
            String id = object.getString("id");
            String title = object.getString("title");
            String overview = object.getString("overview");
            String poster_path = object.getString("poster_path");
            String release_date = object.getString("release_date");

            this.id = id;
            this.title = title;
            this.poster_path = poster_path;
            this.overview = overview;
            this.release_date = release_date;
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public MovieListModel() {

    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(String release_date) {
        this.title = release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.poster_path);
        dest.writeString(this.overview);
        dest.writeString(this.release_date);
    }

    public static final Parcelable.Creator<MovieListModel> CREATOR = new Parcelable.Creator<MovieListModel>() {
        @Override
        public MovieListModel createFromParcel(Parcel source) {
            return new MovieListModel();
        }

        @Override
        public MovieListModel[] newArray(int size) {
            return new MovieListModel[size];
        }
    };
}

package com.daarululuumlido.mymoviedb.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class MovieListModel implements Parcelable {

    private String id;
    private String title;
    private String poster_path;
    private String overview;

    public MovieListModel(JSONObject object) {

        try {
            String id = object.getString("id");
            String title = object.getString("title");
            String overview = object.getString("overview");
            String poster_path = object.getString("poster_path");

            this.id = id;
            this.title = title;
            this.poster_path = poster_path;
            this.overview = overview;
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public MovieListModel() {
        this.id = id;
        this.title = title;
        this.poster_path = poster_path;
        this.overview = overview;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(overview);
    }
}

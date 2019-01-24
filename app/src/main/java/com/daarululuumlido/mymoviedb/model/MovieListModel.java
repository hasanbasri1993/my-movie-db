package com.daarululuumlido.mymoviedb.model;

import org.json.JSONObject;

public class MovieListModel {

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

    }

    public String getId() { return id; }

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

    public void setTitle(String title) { this.title = title; }

}

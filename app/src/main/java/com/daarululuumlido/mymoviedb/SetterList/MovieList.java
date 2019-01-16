package com.daarululuumlido.mymoviedb.SetterList;

import org.json.JSONObject;

public class MovieList {

    public MovieList(JSONObject object) {

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

    private String id;
    private String title;
    private String poster_path;
    private String overview;
}

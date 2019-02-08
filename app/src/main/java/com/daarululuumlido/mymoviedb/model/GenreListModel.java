package com.daarululuumlido.mymoviedb.model;

import org.json.JSONObject;

public class GenreListModel {
    public GenreListModel(JSONObject object) {

        try {
            String id = object.getString("id");
            String name = object.getString("name");

            this.id = id;
            this.name = name;

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String id;
    public String name;
}

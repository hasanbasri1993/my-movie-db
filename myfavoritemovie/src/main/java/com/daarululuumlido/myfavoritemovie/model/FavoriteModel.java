package com.daarululuumlido.myfavoritemovie.model;

import android.os.Parcel;
import android.os.Parcelable;


public class FavoriteModel implements Parcelable {
    private String id;
    private String idMovie;
    private String titleMovie;
    private String overviewMovie;
    private String posterMovie;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }

    public String getTitleMovie() {
        return titleMovie;
    }

    public void setTitleMovie(String titleMovie) {
        this.titleMovie = titleMovie;
    }

    public String getOverviewMovie() {
        return overviewMovie;
    }

    public void setOverviewMovie(String overviewMovie) {
        this.overviewMovie = overviewMovie;
    }

    public String getPosterMovie() {
        return posterMovie;
    }

    public void setPosterMovie(String posterMovie) {
        this.posterMovie = posterMovie;
    }

    public FavoriteModel(String id) {
        this.id = id;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.idMovie);
        dest.writeString(this.titleMovie);
        dest.writeString(this.overviewMovie);
        dest.writeString(this.posterMovie);
    }



    protected FavoriteModel(Parcel in) {
        this.id = in.readString();
        this.idMovie = in.readString();
        this.titleMovie = in.readString();
        this.overviewMovie = in.readString();
        this.posterMovie = in.readString();
    }

    public static final Creator<FavoriteModel> CREATOR = new Creator<FavoriteModel>() {
        @Override
        public FavoriteModel createFromParcel(Parcel source) {
            return new FavoriteModel(source);
        }

        @Override
        public FavoriteModel[] newArray(int size) {
            return new FavoriteModel[size];
        }
    };
}

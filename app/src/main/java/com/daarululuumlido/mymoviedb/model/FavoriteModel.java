package com.daarululuumlido.mymoviedb.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static android.provider.BaseColumns._ID;
import static com.daarululuumlido.mymoviedb.database.DatabaseContract.FavoriteMovieColumns.OVERVIEWMOVIE;
import static com.daarululuumlido.mymoviedb.database.DatabaseContract.FavoriteMovieColumns.POSTERMOVIE;
import static com.daarululuumlido.mymoviedb.database.DatabaseContract.FavoriteMovieColumns.TITLEMOVIE;
import static com.daarululuumlido.mymoviedb.database.DatabaseContract.getColomnInt;
import static com.daarululuumlido.mymoviedb.database.DatabaseContract.getColomnString;

public class FavoriteModel implements Parcelable {
    private int id;
    private int idMovie;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
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

    private String titleMovie;
    private String overviewMovie;
    private String posterMovie;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.idMovie);
        dest.writeString(this.titleMovie);
        dest.writeString(this.overviewMovie);
        dest.writeString(this.posterMovie);
    }

    public FavoriteModel(Cursor cursor) {
        this.id = getColomnInt(cursor, _ID);
        this.idMovie = getColomnInt(cursor, _ID);
        this.titleMovie = getColomnString(cursor, TITLEMOVIE);
        this.overviewMovie = getColomnString(cursor, OVERVIEWMOVIE);
        this.posterMovie = getColomnString(cursor, POSTERMOVIE);
    }

    protected FavoriteModel(Parcel in) {
        this.id = in.readInt();
        this.idMovie = in.readInt();
        this.titleMovie = in.readString();
        this.overviewMovie = in.readString();
        this.posterMovie = in.readString();
    }

    public static final Parcelable.Creator<FavoriteModel> CREATOR = new Parcelable.Creator<FavoriteModel>() {
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

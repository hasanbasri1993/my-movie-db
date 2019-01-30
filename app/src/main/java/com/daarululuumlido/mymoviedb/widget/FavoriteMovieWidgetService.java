package com.daarululuumlido.mymoviedb.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class FavoriteMovieWidgetService  extends RemoteViewsService{

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MovieRemoteViewsFactory(this.getApplicationContext(), intent);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
}

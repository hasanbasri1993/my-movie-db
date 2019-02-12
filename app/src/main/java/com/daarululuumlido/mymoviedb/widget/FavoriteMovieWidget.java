package com.daarululuumlido.mymoviedb.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.daarululuumlido.mymoviedb.R;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteMovieWidget extends AppWidgetProvider {

    public static final String TOAST_ACTION = "com.daarululuumlido.app.mymoviedb.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.daarululuumlido.app.mymoviedb.EXTRA_ITEM";
    public static final String WIDGET_CLICK = "widgetclick";

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, FavoriteMovieWidgetService.class);

        intent.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_movie_widget);
        views.setRemoteAdapter(R.id.stack_view, intent);
        views.setEmptyView(R.id.stack_view, R.id.empty_view);
        Intent toastIntent = new Intent(context, FavoriteMovieWidget.class);
        toastIntent.setAction(FavoriteMovieWidget.TOAST_ACTION);
        toastIntent.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (WIDGET_CLICK.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_movie_widget);
            Intent toastIntent = new Intent(context, FavoriteMovieWidget.class);
            int appWidgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID, 0);

            toastIntent.setAction(FavoriteMovieWidget.TOAST_ACTION);
            toastIntent.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }

    public PendingIntent getPendingSelfIntent(Context context, int appWidgetID, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        intent.putExtra(EXTRA_APPWIDGET_ID, appWidgetID);
        return PendingIntent.getBroadcast(context, appWidgetID, intent, 0);
    }
}


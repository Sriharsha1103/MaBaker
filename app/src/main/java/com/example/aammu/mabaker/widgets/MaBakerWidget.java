package com.example.aammu.mabaker.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.aammu.mabaker.R;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link MaBakerWidgetConfigureActivity MaBakerWidgetConfigureActivity}
 */
public class MaBakerWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            MaBakerWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
            Toast.makeText(context, R.string.widget_removed,Toast.LENGTH_SHORT).show();
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int mAppWidgetId) {

        CharSequence widgetTitle = MaBakerWidgetConfigureActivity.loadTitlePref(context, mAppWidgetId);
        CharSequence widgetText = MaBakerWidgetConfigureActivity.loadDetailPref(context,mAppWidgetId);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ma_baker_widget);
        views.setTextViewText(R.id.id_widget_title,widgetTitle);
        views.setTextViewText(R.id.id_widget_text, widgetText);
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

    }
}


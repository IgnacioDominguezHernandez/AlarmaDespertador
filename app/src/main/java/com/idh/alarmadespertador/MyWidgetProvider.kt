package com.idh.alarmadespertador

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class MyWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        // Aquí actualizas tu widget, por ejemplo, la imagen
        val views = RemoteViews(context.packageName, R.layout.widget)
        views.setImageViewResource(R.id.widget_image, R.drawable.relojcinco)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}

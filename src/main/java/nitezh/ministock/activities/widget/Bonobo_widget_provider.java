package nitezh.ministock.activities.widget;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RemoteViews;

import nitezh.ministock.CustomAlarmManager;
import nitezh.ministock.R;
import nitezh.ministock.WidgetProvider;

public class Bonobo_widget_provider extends WidgetProviderBase{

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action != null) {
            switch (action) {
                case "LEFT":
                case "RIGHT":
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        int appWidgetId = extras.getInt(
                                AppWidgetManager.EXTRA_APPWIDGET_ID,
                                AppWidgetManager.INVALID_APPWIDGET_ID);
                        handleTouch(context, appWidgetId, action);
                    }
                    break;

                default:
                    super.onReceive(context, intent);
                    break;
            }
        }
    }

    private void handleTouch(Context context, int appWidgetId, String action) {
        if (action.equals("LEFT")) {
            startPreferencesActivity(context, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews mView = initViews(context, appWidgetManager, widgetId);
            appWidgetManager.updateAppWidget(widgetId, mView);
            //
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }
    private RemoteViews initViews(Context context,
                                  AppWidgetManager widgetManager, int widgetId) {

        RemoteViews mView = new RemoteViews(context.getPackageName(),
                R.layout.bonobo_widget_layout);

        Intent intent = new Intent(context, Bonobo_widget_service.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        mView.setRemoteAdapter( R.id.widgetCollectionList, intent);

        Intent leftTouchIntent = new Intent(context, WidgetProvider.class);
        leftTouchIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        leftTouchIntent.setAction("LEFT");
        mView.setOnClickPendingIntent(R.id.widget_left,
                PendingIntent.getBroadcast(context, widgetId, leftTouchIntent, 0));


        return mView;
    }



}

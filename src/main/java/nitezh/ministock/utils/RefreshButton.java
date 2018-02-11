package nitezh.ministock.utils;

/**
 * Created by boli on 2018-02-11.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import nitezh.ministock.PreferenceStorage;
import nitezh.ministock.R;
import nitezh.ministock.utils.DateTools;

public class RefreshButton {

    public static final String WIDGET_BUTTON = "nitezh.ministock.WIDGET_BUTTON";
    private final PreferenceStorage appStorage;
    private final AlarmManager alarmManager;
    private final PendingIntent pendingIntent;

    public RefreshButton(Context context) {
        this.appStorage = PreferenceStorage.getInstance(context);
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0,
                new Intent(WIDGET_BUTTON), 0);
    }

}

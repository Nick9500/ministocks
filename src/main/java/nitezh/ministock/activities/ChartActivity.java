package nitezh.ministock.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import nitezh.ministock.R;
import nitezh.ministock.WidgetProvider;
import nitezh.ministock.activities.widget.WidgetProviderBase;
import nitezh.ministock.activities.widget.WidgetRow;

import static nitezh.ministock.activities.GlobalWidgetData.interval;

/**
 * Created by nicholasfong on 2018-02-21.
 */

public class ChartActivity extends Activity {
    // Public variables
    public static int mAppWidgetId = 0;
    private final String alphavantagekey = "ZKD8M6L9CEQAK89H";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        URL url = null;
        Bitmap bmImg = null;
        int interval = GlobalWidgetData.getInterval();
        int position = getIntent().getIntExtra(WidgetProviderBase.ROW_POSITION, 0);
        //MyData.setInterval(2); /*= getIntent().getIntExtra(WidgetProviderBase.INTERVAL, 2);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bonobo_chart_layout);
        String symbol = GlobalWidgetData.getList().get(position).getSymbol();
        Spanned html = Html.fromHtml("Graph of " + symbol + " <br /><br />");
        TextView text = (TextView) findViewById(R.id.chart_text);
        text.setText(html);
        String intervalStr = intervalSwitcher(interval);
        Log.i("urltext", "https://www.alphavantage.co/query?function=TIME_SERIES_" + intervalStr + "_ADJUSTED&symbol="
                + symbol + "&apikey=" + alphavantagekey);


        List<String> prices = GlobalWidgetData.getValues("https://www.alphavantage.co/query?function=TIME_SERIES_" + intervalStr + "_ADJUSTED&symbol="
                + symbol + "&apikey=" + alphavantagekey, interval);

        new ImageSnatcher((ImageView) findViewById(R.id.chart_img)).execute(GlobalWidgetData.constructImageUrl(prices));

    }

    @Override
    public void onResume() {
        super.onResume();
        int interval = GlobalWidgetData.getInterval();
        int position = getIntent().getIntExtra(WidgetProviderBase.ROW_POSITION, 0);
        Button but7day = (Button) findViewById(R.id.but_7day);
        Button but52wk = (Button) findViewById(R.id.but_52wk);
        Button but12mth = (Button) findViewById(R.id.but_12mth);
        String symbol = GlobalWidgetData.getList().get(position).getSymbol();

        but7day.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GlobalWidgetData.setInterval(1);
                finish();
                startActivity(getIntent());
                //recreate();

                // Log.i("TEST PRINT", "WE CLICKED 7 DAY");
            }
        });
        but52wk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GlobalWidgetData.setInterval(2);
                finish();
                startActivity(getIntent());
                //recreate();

                // Log.i("TEST PRINT", "WE CLICKED 52 WK");
            }
        });
        but12mth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GlobalWidgetData.setInterval(3);
                finish();
                startActivity(getIntent());
                //recreate();

                //Log.i("TEST PRINT", "WE CLICKED 12 MTH");
            }
        });
        String intervalStr = intervalSwitcher(interval);
        Log.i("urltext", "https://www.alphavantage.co/query?function=TIME_SERIES_" + intervalStr + "_ADJUSTED&symbol="
                + symbol + "&apikey=" + alphavantagekey);


        List<String> prices = GlobalWidgetData.getValues("https://www.alphavantage.co/query?function=TIME_SERIES_" + intervalStr + "_ADJUSTED&symbol="
                + symbol + "&apikey=" + alphavantagekey, interval);

        new ImageSnatcher((ImageView) findViewById(R.id.chart_img)).execute(GlobalWidgetData.constructImageUrl(prices));
    }

    public String intervalSwitcher(int interval) {
        if (interval == 1) {
            return "DAILY";
        } else if (interval == 3) {
            return "MONTHLY";
        } else {
            return "WEEKLY";
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
    }
}

class ImageSnatcher extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public ImageSnatcher(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

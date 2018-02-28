package nitezh.ministock.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import nitezh.ministock.R;
import nitezh.ministock.activities.widget.WidgetProviderBase;
import nitezh.ministock.activities.widget.WidgetRow;

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
        int position = getIntent().getIntExtra(WidgetProviderBase.ROW_POSITION, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bonobo_chart_layout);
        String symbol = MyData.getList().get(position).getSymbol();
        Spanned html = Html.fromHtml("Graph of " + symbol + " <br /><br />");
        TextView text = (TextView) findViewById(R.id.chart_text);
        text.setText(html);

        Log.i("urltext", "https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY_ADJUSTED&symbol="
                +symbol+"&apikey=" + alphavantagekey);
        List<String> monthlyPrices = MyData.getValues("https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY_ADJUSTED&symbol="
        +symbol+"&apikey=" + alphavantagekey);

        new ImageSnatcher( (ImageView) findViewById(R.id.chart_img) ).execute(MyData.constructImageUrl(monthlyPrices));

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

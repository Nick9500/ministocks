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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import nitezh.ministock.R;
import nitezh.ministock.activities.widget.WidgetProviderBase;

import static nitezh.ministock.activities.GlobalWidgetData.interval;

/**
 * Created by nicholasfong on 2018-02-21.
 */

public class ChartActivity extends Activity {
    // Public variables
    public static int mAppWidgetId = 0;
    private final String alphavantagekey = "ZKD8M6L9CEQAK89H";
    public String symbol = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        int interval = GlobalWidgetData.getInterval();
        int position = getIntent().getIntExtra(WidgetProviderBase.ROW_POSITION, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bonobo_chart_layout);
        String symbol = GlobalWidgetData.getList().get(position).getSymbol();
        GlobalWidgetData.initXMLForRss(symbol);
        this.symbol = symbol;
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

        // Buttons Related to Graphs
        Button btnRSI = (Button) findViewById(R.id.btn_RSI);
        Button btnMACD = (Button) findViewById(R.id.btn_MACD);

        final String sym = this.symbol;
        Button btn7day = (Button) findViewById(R.id.btn_7day);
        Button btn52wk = (Button) findViewById(R.id.btn_52wk);
        Button btn12mth = (Button) findViewById(R.id.btn_12mth);
        Button btnRss = (Button) findViewById(R.id.btn_rss);
        String symbol = GlobalWidgetData.getList().get(position).getSymbol();

        btn7day.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GlobalWidgetData.setInterval(1);
                finish();
                startActivity(getIntent());
            }
        });
        btn52wk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GlobalWidgetData.setInterval(2);
                finish();
                startActivity(getIntent());
            }
        });
        btn12mth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GlobalWidgetData.setInterval(3);
                finish();
                startActivity(getIntent());
            }
        });
        btnRss.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NewsfeedActivity.mAppWidgetId = mAppWidgetId;
                Intent activity = new Intent(ChartActivity.this, NewsfeedActivity.class);
                activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.putExtra(NewsfeedActivity.SYMBOL, sym);
                ChartActivity.this.startActivity(activity);
            }
        });
        String intervalStr = intervalSwitcher(interval);
        Log.i("urltext", "https://www.alphavantage.co/query?function=TIME_SERIES_" + intervalStr + "_ADJUSTED&symbol="
                + symbol + "&apikey=" + alphavantagekey);

        btnMACD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GlobalWidgetData.setInterval(5);
                finish();
                startActivity(getIntent());
            }
        });

        if (interval == 4 || interval == 5) {
            String graphFct = switchGraphFct(interval);
            String graphParam = switchGraphParam(interval);


            List<String> graphPrices = GlobalWidgetData.getValues("https://www.alphavantage.co/query?function=" + graphFct + "&symbol="
                    + symbol + graphParam + "&series_type=close&apikey=" + alphavantagekey, interval);

            new ImageSnatcher((ImageView) findViewById(R.id.chart_img)).execute(GlobalWidgetData.constructImageUrl(graphPrices));
        } else {
            String intervalStr = intervalSwitcher(interval);
            Log.i("urltext", "https://www.alphavantage.co/query?function=TIME_SERIES_" + intervalStr + "_ADJUSTED&symbol="
                    + symbol + "&apikey=" + alphavantagekey);


            List<String> prices = GlobalWidgetData.getValues("https://www.alphavantage.co/query?function=TIME_SERIES_" + intervalStr + "_ADJUSTED&symbol="
                    + symbol + "&apikey=" + alphavantagekey, interval);

            new ImageSnatcher((ImageView) findViewById(R.id.chart_img)).execute(GlobalWidgetData.constructImageUrl(prices));
        }

    }

    // Helper methods for choosing Graph types / functions
    public String intervalSwitcher(int interval) {
        if (interval == 1) {
            return "DAILY";
        } else if (interval == 3) {
            return "MONTHLY";
        } else {
            return "WEEKLY";
        }
    }

    public String switchGraphFct(int interval) {
        if (interval == 4) {
            return "RSI";
        } else {
            return "MACD";
        }
    }

    public String switchGraphParam(int interval) {
        if (interval == 4) {
            return "&interval=daily&time_period=14";
        } else {
            return "&interval=weekly";
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalWidgetData.setInterval(2);
        finish();
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
}

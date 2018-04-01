package nitezh.ministock.activities;

import android.app.Activity;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        int interval = GlobalWidgetData.getInterval();
        int position = getIntent().getIntExtra(WidgetProviderBase.ROW_POSITION, 0);
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

        // Buttons Related to Graphs
        Button btnRSI = (Button) findViewById(R.id.btn_RSI);
        Button btnMACD = (Button) findViewById(R.id.btn_MACD);

        btnRSI.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GlobalWidgetData.setInterval(4);
                finish();
                startActivity(getIntent());


                Log.i("TEST PRINT", "WE CLICKED RSI GRAPH");
            }
        });

        btnMACD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GlobalWidgetData.setInterval(5);
                finish();
                startActivity(getIntent());


                Log.i("TEST PRINT", "WE CLICKED MACD GRAPH");
            }
        });

        // Buttons Related to Intervals
        Button btn7day = (Button) findViewById(R.id.btn_7day);
        Button btn52wk = (Button) findViewById(R.id.btn_52wk);
        Button btn12mth = (Button) findViewById(R.id.btn_12mth);

        String symbol = GlobalWidgetData.getList().get(position).getSymbol();

        btn7day.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GlobalWidgetData.setInterval(1);
                finish();
                startActivity(getIntent());
                //recreate();

                // Log.i("TEST PRINT", "WE CLICKED 7 DAY");
            }
        });
        btn52wk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GlobalWidgetData.setInterval(2);
                finish();
                startActivity(getIntent());
                //recreate();

                // Log.i("TEST PRINT", "WE CLICKED 52 WK");
            }
        });
        btn12mth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GlobalWidgetData.setInterval(3);
                finish();
                startActivity(getIntent());
            }
        });

        if (interval == 4) {
            List<String> RSIprices = GlobalWidgetData.getValues("https://www.alphavantage.co/query?function=RSI&symbol="
                    + symbol + "&interval=weekly&time_period=14&series_type=close&apikey=" + alphavantagekey, interval);

            new ImageSnatcher((ImageView) findViewById(R.id.chart_img)).execute(GlobalWidgetData.constructImageUrl(RSIprices));
        } else if (interval == 5) {
            List<String> MACDprices = GlobalWidgetData.getValues("https://www.alphavantage.co/query?function=MACD&symbol="
                    + symbol + "&interval=weekly&&series_type=close&apikey=demo" + alphavantagekey, interval);

            new ImageSnatcher((ImageView) findViewById(R.id.chart_img)).execute(GlobalWidgetData.constructImageUrl(MACDprices));
        } else {
            String intervalStr = intervalSwitcher(interval);
            Log.i("urltext", "https://www.alphavantage.co/query?function=TIME_SERIES_" + intervalStr + "_ADJUSTED&symbol="
                    + symbol + "&apikey=" + alphavantagekey);


            List<String> prices = GlobalWidgetData.getValues("https://www.alphavantage.co/query?function=TIME_SERIES_" + intervalStr + "_ADJUSTED&symbol="
                    + symbol + "&apikey=" + alphavantagekey, interval);

            new ImageSnatcher((ImageView) findViewById(R.id.chart_img)).execute(GlobalWidgetData.constructImageUrl(prices));
        }
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

package nitezh.ministock.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

//import nitezh.ministock.R;
import at.theengine.android.simple_rss2_android.RSSItem;
import at.theengine.android.simple_rss2_android.SimpleRss2Parser;
import at.theengine.android.simple_rss2_android.SimpleRss2ParserCallback;
import nitezh.ministock.R;
import nitezh.ministock.activities.widget.WidgetProviderBase;

/**
 * Created by nicholasfong on 2018-03-30.
 */

public class NewsfeedActivity extends Activity {
    // Public variables
    public static int mAppWidgetId = 0;
    public static String SYMBOL = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = "http://finance.yahoo.com/rss/headline?s=MSFT";
        SimpleRss2Parser parser = new SimpleRss2Parser(url,
                new SimpleRss2ParserCallback() {
                    @Override
                    public void onFeedParsed(List<RSSItem> items) {
                        for(int i = 0; i < items.size(); i++){
                            Log.i("SimpleRss2ParserDemo",items.get(i).getTitle());
                            System.out.println(items.get(i).getTitle());
                        }
                    }
                    @Override
                    public void onError(Exception ex) {
                        Toast.makeText(NewsfeedActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        Spanned html = Html.fromHtml("Test of " + SYMBOL + " <br /><br />");
        TextView text = (TextView) findViewById(R.id.news_text);
        text.setText(html);
        parser.parseAsync();
        setContentView(R.layout.bonobo_news_layout);
    }
}

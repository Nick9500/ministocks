package nitezh.ministock.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;

import android.util.Log;
import android.widget.TextView;


import java.util.ArrayList;
import nitezh.ministock.R;
import nitezh.ministock.dataaccess.HandleXML;
import nitezh.ministock.dataaccess.RssItem;

/**
 * Created by nicholasfong on 2018-03-30.
 */

public class NewsfeedActivity extends Activity {
    // Public variables
    public static int mAppWidgetId = 0;
    public static String SYMBOL = "";
    public static HandleXML obj = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bonobo_news_layout);
        TextView text1 = (TextView) findViewById(R.id.news_text1);
        text1.setTextColor(Color.parseColor("#00FFFF"));
        text1.setMovementMethod(new ScrollingMovementMethod());
        obj = GlobalWidgetData.getXMLObj();

        ArrayList<RssItem> list = obj.getRssList();
        String testOut = "";
        int count = 0;
        for (RssItem i : list) {
            Log.i("onCreate test: ", "count:" + count + " " + i.toString());
            testOut = testOut + rssStringUnit(i.getTitle(), i.getDescription(), i.getLink());
        }
        Spanned html = Html.fromHtml( testOut );
        text1.setText(html);
    }

    public String rssStringUnit(String tit, String date, String link){
            String toReturn = "<p><b>" + tit + "</b><br/>  "+ date + "<br/>" + link + "</p>";
            return toReturn;
    }
}

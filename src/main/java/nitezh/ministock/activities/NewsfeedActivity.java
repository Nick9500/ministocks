package nitezh.ministock.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import nitezh.ministock.NewsFeedAdapter;
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
        obj = GlobalWidgetData.getXMLObj();
        ListView listView;
        ArrayList<RssItem> list = obj.getRssList();
        final ArrayList<String> nameArray = new ArrayList<>();
        ArrayList<String> infoArray = new ArrayList<>();
        final ArrayList<String> urlArray = new ArrayList<>();

        int count = 0;
        for (RssItem i : list) {
            Log.i("onCreate test: ", "count:" + count + " " + i.toString());
            nameArray.add(i.getTitle());
            infoArray.add(i.getDescription());
            urlArray.add(i.getLink());
            count ++;
        }

        listView = (ListView) findViewById(R.id.bonobo_news_listView);
        NewsFeedAdapter newsListAdapter = new NewsFeedAdapter(this, nameArray, infoArray);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                String url = urlArray.get(position);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        listView.setAdapter(newsListAdapter);
    }

}

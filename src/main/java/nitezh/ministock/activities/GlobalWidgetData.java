package nitezh.ministock.activities;

import android.app.Application;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nitezh.ministock.activities.widget.WidgetRow;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nitezh.ministock.activities.widget.WidgetRow;
import java.util.Map;
import java.util.Set;

public class GlobalWidgetData extends Application {
    // Use in population of listview
    public static List<WidgetRow> myStockList = new ArrayList<WidgetRow>();
    public String urlString;

    public static List<WidgetRow> getList() {
        return myStockList;
    }

    public void setGlobalList(List<WidgetRow> list) {
        myStockList = list;
    }
    public void setURLString(String urlString){this.urlString = urlString;}

    public String getURLString(){return urlString;};

    public static List<String> getJsonValuesForMyDataList(String sURL){
        List<String> closingValuesWeekly = new ArrayList<>();
        try{

            //"https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY_ADJUSTED&symbol=MSFT&apikey=demo
            URL url = new URL(sURL);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            //Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //use gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream)
                    request.getContent()));

            // Get a handle on the root
            JsonObject rootObj = root.getAsJsonObject();

            // Get a handle on Weekly closing values
            JsonObject weeklyObj = rootObj.getAsJsonObject("Weekly Adjusted Time Series");

            Set<Map.Entry<String, JsonElement>> entries = weeklyObj.entrySet();
            int weekCounter = 0;
            for ( Map.Entry<String, JsonElement> entry : entries )
            {
                if ( weekCounter <= 51 ){
                    JsonObject weeklyStats = entry.getValue().getAsJsonObject();
                    closingValuesWeekly.add( weeklyStats.getAsJsonPrimitive("4. close")
                                                        .toString() );
                }
                weekCounter++;
            }
        }
        catch( Exception e ){
            e.printStackTrace();
        }
        return closingValuesWeekly;
    }
}

package nitezh.ministock.activities;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

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

    public static List<WidgetRow> getList()
    {
        return myStockList;
    }

    public void setGlobalList(List<WidgetRow> list) {
        myStockList = list;
    }

    public void setURLString(String urlString) {
        this.urlString = urlString;
    }

    public String getURLString() {
        return urlString;
    };

    public static JsonObject getJsonObjectRoot(String sURL) {
        JsonObject rootObj;
        try {
            URL url = new URL(sURL);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            //Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //use gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream)
                    request.getContent()));

            // Get a handle on the root
            rootObj = root.getAsJsonObject();
            return rootObj;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getValues(String sURL) {
        List<String> toReturn = null;
        try {
            toReturn = new JsonSnatcher().execute(sURL).get();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return toReturn;
    }

    public static String constructImageUrl( List<String> list) {
        String cht;             //Chart Type
        String chd;             //Chart Data  (i.e. t:Data1,Data2,Data2,etc)
        String chds;            //Text Format custom scaling
        String chxt;            //Visible Axes
        String chs;             //Chart Size
        String chtt;            //Chart Title\
        cht = "cht=ls";                                             //Line Graph
        chd = "chd=t%3A";                                           //Data of line graph. Must begin with t:
        chds = "chds=a";                                            //Automatic text format scaling
        chxt ="chxt=x%2Cy";                                         //Specify X and Y Axes
        chs = "chs=700x690";                                        //chart size
        chtt = "chtt=Test%20Graph";                                 //Name of Graph
        String chdVars = "";

        for(int i = 0; i < list.size(); i++)
        {
            if (i == 0 )
            { //first
                chdVars = chdVars+list.get(i);
            }
            else
                chdVars = chdVars+"%2C"+list.get(i);        //middle & end
        }
        String toReturn = "https://image-charts.com/chart?"+cht+"&"+chd+chdVars+"&"+chds+"&chof=.png&"
                          +chs+"&chdls=000000&chco=F56991%2CFF9F80%2CFFC48C%2CD1F2A5%2CEFFAB4&"+chtt+"&"
                          +chxt+"&chdlp=b&chf=bg%2Cs%2CFFFFFF&chbh=10&icwt=false";
        return toReturn;
    }
}

class JsonSnatcher extends AsyncTask<String, Void, List<String>> {
    String sURL;

    public JsonSnatcher(){}

    public JsonSnatcher(String sURL) {
        this.sURL = sURL;
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        List<String> closingValuesWeekly = new ArrayList<>();

        try{
            URL url = new URL(strings[0]);
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
                            .toString().replace("\"", "") );
                }
                weekCounter++;
            }
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
        return closingValuesWeekly;
    }
}

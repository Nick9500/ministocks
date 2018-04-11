package nitezh.ministock.activities;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import nitezh.ministock.activities.widget.WidgetRow;
import nitezh.ministock.dataaccess.HandleXML;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Cristi Arde on 2/11/2018.
 */

public class GlobalWidgetData extends Application {

    // Use in population of listview 
    public static List<WidgetRow> myStockList = new ArrayList<WidgetRow>();
    public String urlString;
    public static int interval;
    public static HandleXML obj;


    public static List<WidgetRow> getList() {
        return myStockList;
    }

    public void setGlobalList(List<WidgetRow> list) {
        myStockList = list;
    }


    // Variables in Graph Implementation
    public void setURLString(String urlString) {

        this.urlString = urlString;
    }

    public String getURLString() {

        return urlString;
    }

    public static void initXMLForRss( String ticker ){
        String urlBase = "http://articlefeeds.nasdaq.com/nasdaq/symbols?symbol=";
        String url = urlBase + ticker;
        obj = new HandleXML(url);
        obj.fetchXML();
    }

    public static HandleXML getXMLObj(){
        return obj;
    }

    // Used in Time Scale of Graph Implementation
    public static void setInterval(int intervalVal) {

        interval = intervalVal;
    }

    public static int getInterval() {
        return interval;
    }

    // Methods for Graph Implementation
    public static JsonObject getJsonObjectRoot(String sURL) {
        JsonObject rootObj;
        try {
            URL url = new URL(sURL);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            //Convert to a JSON object to print data
            JsonParser jp = new JsonParser();                           //use gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream)
                    request.getContent()));

            // Get a handle on the root
            rootObj = root.getAsJsonObject();
            return rootObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> getValues(String sURL, int interval) {
        List<String> toReturn = null;
        try {
            toReturn = new JsonSnatcher(sURL, interval).execute(sURL).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public static String constructImageUrl(List<String> list) {
        String cht;             //Chart Type
        String chd;             //Chart Data  (i.e. t:Data1,Data2,Data2,etc)
        String chds;            //Text Format custom scaling
        String chxt;            //Visible Axes
        String chs;             //Chart Size
        String chtt;            //Chart Title
        cht = "cht=ls";                                             //Line Graph
        chd = "chd=t%3A";                                           //Data of line graph. Must begin with t:
        chds = "chds=a";                                            //Automatic text format scaling
        chxt = "chxt=x%2Cy";                                         //Specify X and Y Axes
        chs = "chs=980x690";                                        //chart size
        chtt = "chtt=Graph";                                 //Name of Graph
        String chdVars = "";

        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                chdVars = chdVars + list.get(i);                //first
            } else
                chdVars = chdVars + "%2C" + list.get(i);        //middle & end
        }
        String toReturn = "https://image-charts.com/chart?" + cht + "&" + chd + chdVars + "&" + chds + "&chof=.png&"
                + chs + "&chdls=000000&chco=F56991%2CFF9F80%2CFFC48C%2CD1F2A5%2CEFFAB4&" + chtt + "&"
                + chxt + "&chdlp=b&chf=bg%2Cs%2CFFFFFF&chbh=10&icwt=false";
        return toReturn;
    }
}


// Graph Implementation: Extracts values from a Json file
class JsonSnatcher extends AsyncTask<String, Void, List<String>> {
    String sURL;
    int interval;

    public JsonSnatcher() {
    }

    public JsonSnatcher(String sURL, int interval) {
        this.sURL = sURL;
        this.interval = interval;
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        List<String> closingValuesWeekly = new ArrayList<>();

        try {
            URL url = new URL(strings[0]);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            //Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //use gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream)
                    request.getContent()));

            // Get a handle on the root
            JsonObject rootObj = root.getAsJsonObject();

            if (this.interval == 1) {
                // Get a handle on Daily closing values
                JsonObject dailyObj = rootObj.getAsJsonObject("Time Series (Daily)");

                Set<Map.Entry<String, JsonElement>> entries = dailyObj.entrySet();
                int dayCounter = 0;

                for (Map.Entry<String, JsonElement> entry : entries) {
                    if (dayCounter <= 6) {
                        JsonObject dailyStats = entry.getValue().getAsJsonObject();
                        closingValuesWeekly.add(dailyStats.getAsJsonPrimitive("4. close")
                                .toString().replace("\"", ""));

                        // Testing purposes
                        Log.i("DayTest", dailyStats.getAsJsonPrimitive("4. close")
                                .toString().replace("\"", ""));
                    }
                    dayCounter++;
                }
            } else if (this.interval == 3) {
                // Get a handle on Monthly closing values
                JsonObject monthlyObj = rootObj.getAsJsonObject("Monthly Adjusted Time Series");

                Set<Map.Entry<String, JsonElement>> entries = monthlyObj.entrySet();
                int mthCounter = 0;

                for (Map.Entry<String, JsonElement> entry : entries) {
                    if (mthCounter <= 11) {
                        JsonObject dailyStats = entry.getValue().getAsJsonObject();
                        closingValuesWeekly.add(dailyStats.getAsJsonPrimitive("4. close")
                                .toString().replace("\"", ""));

                        // Testing purposes
                        Log.i("MthTest", dailyStats.getAsJsonPrimitive("4. close")
                                .toString().replace("\"", ""));
                    }
                    mthCounter++;
                }
            } else if (this.interval == 2) {

                // Get a handle on Weekly closing values
                JsonObject weeklyObj = rootObj.getAsJsonObject("Weekly Adjusted Time Series");

                Set<Map.Entry<String, JsonElement>> entries = weeklyObj.entrySet();
                int weekCounter = 0;

                for (Map.Entry<String, JsonElement> entry : entries) {
                    if (weekCounter <= 51) {
                        JsonObject weeklyStats = entry.getValue().getAsJsonObject();
                        closingValuesWeekly.add(weeklyStats.getAsJsonPrimitive("4. close")
                                .toString().replace("\"", ""));

                        // Testing purposes
                        Log.i("WkTest", weeklyStats.getAsJsonPrimitive("4. close")
                                .toString().replace("\"", ""));
                    }
                    weekCounter++;
                }
            } else if (this.interval == 4) /* RSI Graph Chosen */ {
                // Get a handle on RSI closing values
                JsonObject RSIObj = rootObj.getAsJsonObject("Technical Analysis: RSI");

                Set<Map.Entry<String, JsonElement>> entries = RSIObj.entrySet();
                int RSICounter = 0;

                for (Map.Entry<String, JsonElement> entry : entries) {
                    if (RSICounter <= 30) {
                        JsonObject weeklyStats = entry.getValue().getAsJsonObject();
                        closingValuesWeekly.add(weeklyStats.getAsJsonPrimitive("RSI")
                                .toString().replace("\"", ""));

                        // Testing purposes
                        Log.i("RSITest", weeklyStats.getAsJsonPrimitive("RSI")
                                .toString().replace("\"", ""));
                    }
                    RSICounter++;
                }
            } else /* MACD Graph Chosen*/ {
                // Get a handle on MACD closing values
                JsonObject MACDObj = rootObj.getAsJsonObject("Technical Analysis: MACD");

                Set<Map.Entry<String, JsonElement>> entries = MACDObj.entrySet();
                int MACDCounter = 0;

                for (Map.Entry<String, JsonElement> entry : entries) {
                    if (MACDCounter <= 51) {
                        JsonObject weeklyStats = entry.getValue().getAsJsonObject();
                        closingValuesWeekly.add(weeklyStats.getAsJsonPrimitive("MACD")
                                .toString().replace("\"", ""));

                        // Testing purposes
                        Log.i("MACDTest", weeklyStats.getAsJsonPrimitive("MACD")
                                .toString().replace("\"", ""));
                    }
                    MACDCounter++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return closingValuesWeekly;
    }
}

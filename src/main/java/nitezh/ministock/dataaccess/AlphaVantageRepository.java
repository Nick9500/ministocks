package nitezh.ministock.dataaccess;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import nitezh.ministock.BuildConfig;
import nitezh.ministock.domain.StockQuote;
import nitezh.ministock.utils.Cache;
import nitezh.ministock.utils.UrlDataTools;

import static android.net.wifi.WifiConfiguration.Status.strings;

/**
 * Created by GRao on 3/22/2018.
 */

public class AlphaVantageRepository {

    private static final String API_KEY = BuildConfig.AlphaVantageSecKey;
    private String baseURL = "https://www.alphavantage.co/query?function=TIME_SERIES_";
    private String cryptoBaseURL = "https://www.alphavantage.co/query?function=DIGITAL_CURRENCY_";
    private final FxChangeRepository fxChangeRepository;

    AlphaVantageRepository(FxChangeRepository fxChangeRepository) {
        this.fxChangeRepository = fxChangeRepository;
    }

    //for regular stock exchange - mostly used for plotting data points on graphs
    JSONArray retrieveQuotesAsJson(Cache cache, String timeInterval, String symbols) throws JSONException {
        String url = buildRequestURL(timeInterval, symbols);
        String quotesString = UrlDataTools.getCachedUrlData(url, cache, 300);
        JSONObject quotesJson = new JSONObject(quotesString);
        JSONArray quotes = new JSONArray();
        Iterator<String> stringKey = quotesJson.keys();
        quotesJson = quotesJson.getJSONObject(stringKey.next());
        for (Iterator key = quotesJson.keys(); key.hasNext();)
        {
            String objKey = (String)key.next();
            JSONObject quoteJson =  quotesJson.getJSONObject(objKey);
            JSONObject data = new JSONObject();
            data.put("date", objKey);
            data.put("open", quoteJson.optString("1. open"));
            data.put("high", quoteJson.optString("2. high"));
            data.put("low", quoteJson.optString("3. low"));
            data.put("close", quoteJson.optString("4. close"));
            data.put("volume", quoteJson.optString("5. volume"));
            quotes.put(data);
       }

        return quotes;
    }

    //For Crypto Currency
    JSONArray retrieveQuotesAsJson(Cache cache, String timeInterval, String symbols, String market) throws JSONException {
        String url = buildRequestURL(timeInterval, symbols, market);
        String quotesString = UrlDataTools.getCachedUrlData(url, cache, 300);
        JSONObject quotesJson = new JSONObject(quotesString);
        JSONArray quotes = new JSONArray();
        Iterator<String> stringKey = quotesJson.keys();
        quotesJson = quotesJson.getJSONObject(stringKey.next());
        for (Iterator key = quotesJson.keys(); key.hasNext();)
        {
            String objKey = (String)key.next();
            JSONObject quoteJson =  quotesJson.getJSONObject(objKey);
            JSONObject data = new JSONObject();
            data.put("date", objKey);
            data.put("open", quoteJson.optString("1a. open ("+market+")"));
            data.put("high", quoteJson.optString("2a. high ("+market+")"));
            data.put("low", quoteJson.optString("3a. low ("+market+")"));
            data.put("close", quoteJson.optString("4a. close ("+market+")"));
            data.put("volume", quoteJson.optString("5. volume"));
            data.put("market cap", quoteJson.optString("6. market cap (USD)")); //market cap always in USD
            quotes.put(data);
        }

        return quotes;
    }

    private String buildRequestURL(String timeInterval, String symbol){
        return baseURL+timeInterval+"&symbol="+symbol+"&interval=15min&outputsize=full&apikey="+API_KEY;
    }

    //for crypto
    private String buildRequestURL(String timeInterval, String symbol, String market){
        return cryptoBaseURL+timeInterval+"&symbol="+symbol+"&market="+market+"&apikey="+API_KEY;
    }
}

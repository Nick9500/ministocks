package nitezh.ministock.dataaccess;

/**
 * Created by raj34 on 2018-04-04.
 */


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;

import nitezh.ministock.domain.StockQuote;
import nitezh.ministock.utils.UrlDataTools;

public class CoinMarketCapRepo {

  private FxChangeRepository fxChangeRepo;
  private String baseURL = "https://api.coinmarketcap.com/v1/ticker/";

   //constructor
   public CoinMarketCapRepo(FxChangeRepository fxChangeRepo){
        this.fxChangeRepo = fxChangeRepo;
    }

    //Retrieve stock exchange info
    JSONArray retrieveQuotesAsJson(String id, int limit){
        String url = GetURL(id, limit);
        try{
            String quotesString = UrlDataTools.getUrlData(url);
            JSONArray quotes = new JSONArray(quotesString);
            return quotes;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //for crypto
    public HashMap<String, StockQuote> getQuotes(String symbol){
        HashMap<String, StockQuote> quotes = new HashMap<>();
        JSONArray jsonArray;
        try {
            jsonArray = retrieveQuotesAsJson(symbol, 1);
            JSONObject stock = jsonArray.getJSONObject(0);
            StockQuote quote = new StockQuote(
                    stock.getString("symbol"),
                    stock.getString("price_usd"),
                    "",
                    stock.getString("percent_change_1h"),
                    "",
                    stock.getString("24h_volume_usd"),
                    stock.getString("name"),
                    "",
                    Locale.US);
            quotes.put(quote.getSymbol(), quote);

        }
        catch(Exception e){
            e.printStackTrace();
        }

        return quotes;
    }

    public String findName(String symbol){
        JSONArray jsonArray;
        try {
            jsonArray = retrieveQuotesAsJson("", 100);
            for (int i = 0; i < jsonArray.length(); i++) {
                String symbolToCompare = jsonArray.getJSONObject(i).getString("symbol");
                if(symbol.equals(symbolToCompare)){
                    return jsonArray.getJSONObject(i).getString("name");
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private String GetURL(String ID, int limit){

        if (limit > 0)
            return baseURL+ID+"/?"+limit;
        else
            return baseURL+ID;
    }

}

package nitezh.ministock.dataaccess;

/**
 * Created by raj34 on 2018-04-04.
 */


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import nitezh.ministock.utils.Cache;
import nitezh.ministock.utils.UrlDataTools;

public class CoinMarketCapRepo {

   FxChangeRepository fxChangeRepo;
   String url = "https://api.coinmarketcap.com/v1/ticker/";

   //constructor
    CoinMarketCapRepo(FxChangeRepository fxChangeRepo){
        this.fxChangeRepo = fxChangeRepo;
    }

    //Retrieve stock exchange info
    JSONArray retrieveQuotesAsJson(Cache cache) throws JSONException {

        String quotesString = UrlDataTools.getCachedUrlData(url, cache, 300);
        JSONObject quotesJson = new JSONObject(quotesString);
        JSONArray quotes = new JSONArray();
        Iterator<String> stringKey = quotesJson.keys();
        quotesJson = quotesJson.getJSONObject(stringKey.next());

        for (Iterator i = quotesJson.keys(); i.hasNext();){

            String objKey = (String)i.next();
            JSONObject quoteJson =  quotesJson.getJSONObject(objKey);
            JSONObject APIData = new JSONObject();

            APIData.put("id", objKey);
            APIData.put("name", quoteJson.optString("name"));
            APIData.put("symbol", quoteJson.optString("symbol"));
            APIData.put("rank", quoteJson.optString("rank"));
            APIData.put("USDPrice", quoteJson.optString("price_usd"));
            APIData.put("BTCPrice", quoteJson.optString("price_btc"));
            APIData.put("24HVolumeUSD", quoteJson.optString("24h_volume_usd"));
            APIData.put("marketCapUSD", quoteJson.optString("market_cap_usd"));
            APIData.put("availableSupply", quoteJson.optString("available_supply"));
            APIData.put("totalSupply", quoteJson.optString("total_supply"));
            APIData.put("maxSupply", quoteJson.optString("max_supply"));
            APIData.put("percentChangeEvery1H", quoteJson.optString("percent_change_1h"));
            APIData.put("percentChangeEvery24H", quoteJson.optString("percent_change_24h"));
            APIData.put("percentChangeEvery7D", quoteJson.optString("percent_change_7d"));
            APIData.put("lastUpdated", quoteJson.optString("last_updated"));

            quotes.put(APIData);
        }

        return quotes;
    }

}

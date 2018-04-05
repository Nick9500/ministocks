package nitezh.ministock.dataaccess;

/**
 * Created by raj34 on 2018-04-04.
 */


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nitezh.ministock.utils.UrlDataTools;

public class CoinMarketCapRepo {

  private FxChangeRepository fxChangeRepo;
  private String baseURL = "https://api.coinmarketcap.com/v1/ticker/";

   //constructor
    CoinMarketCapRepo(FxChangeRepository fxChangeRepo){
        this.fxChangeRepo = fxChangeRepo;
    }

    //Retrieve stock exchange info
    JSONArray retrieveQuotesAsJson(String id) throws JSONException {
        String url = GetURL(id);
        String quotesString = UrlDataTools.getUrlData(url);
        JSONArray quotes = new JSONArray(quotesString);

        return quotes;
    }

    private String GetURL(String ID){
        return baseURL+ID;
    }

}

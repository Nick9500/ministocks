package nitezh.ministock.activities;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nitezh.ministock.utils.UrlDataTools;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;


/**
 * Created by Cristi Arde on 4/4/2018.
 */

public class StockSuggestionsTest {
    String btcSymbol;
    String userInput;
    JSONArray cryptoJson;

    @Before
    public void setUp()
    {
        userInput = "Bt";
        btcSymbol = "BTC";
        cryptoJson = null;
    }

    @Test
    public void checkRegex()
    {
        final Pattern STOCK_REGEX = Pattern.compile("^" + userInput + "[A-Z0-9]*", Pattern.CASE_INSENSITIVE);
        Matcher cryptoMatcher = STOCK_REGEX.matcher(btcSymbol);
        assertTrue(cryptoMatcher.matches());
    }

    @Test
    public void urlRetrivalFromCoinMarketCap() throws IOException
    {
        //set up
        String url = "https://api.coinmarketcap.com/v1/ticker/bitcoin/";

        // Act
        String result = UrlDataTools.urlToString(url).substring(0, 85);
        String result2 = UrlDataTools.getUrlData(url).substring(0, 85);

        // Assert
        String expected = "[\n" +
                "    {\n" +
                "        \"id\": \"bitcoin\", \n" +
                "        \"name\": \"Bitcoin\", \n" +
                "        \"symbol\": \"BTC\"";
        assertEquals(expected, result);
        assertEquals(expected, result2);
    }

    @Test
    public void retriveStockInfoFromJSON() throws JSONException {
        //set up
        String url = "https://api.coinmarketcap.com/v1/ticker/bitcoin/";
        // Act
        String result = UrlDataTools.getUrlData(url);
        cryptoJson = new JSONArray(result);

        JSONObject jsonObject = cryptoJson.optJSONObject(0);
        System.out.println(jsonObject.optString("name"));
        assertTrue(!jsonObject.optString("name").isEmpty());
        assertEquals("Bitcoin", jsonObject.optString("name"));
        System.out.println(jsonObject.optString("symbol"));
        assertTrue(!jsonObject.optString("symbol").isEmpty());
        assertEquals("BTC", jsonObject.optString("symbol"));
    }
}

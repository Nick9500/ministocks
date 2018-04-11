package nitezh.ministock.dataaccess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nitezh.ministock.domain.StockQuote;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by raj34 on 2018-04-04.
 */

public class CoinMarketCapRepoTest {
    CoinMarketCapRepo coinMarketCapRepo;

    @Before
    public void setup(){
        coinMarketCapRepo = new CoinMarketCapRepo(new FxChangeRepository());
    }

    @Test
    public void getAllDefaultAPIData() throws JSONException{  //By Default, we should be getting the first 100 stocks
        String blankID = "";  //should get all the data if no id is provided
        int NumberOfTotalDefaultStocks = 100;  //Number of total stocks from CoinMarketRepo by default

        JSONArray jsonArray = coinMarketCapRepo.retrieveQuotesAsJson(blankID, 0);
        assertNotNull(jsonArray);
        assertEquals(NumberOfTotalDefaultStocks , jsonArray.length());  //we should be getting every stock

        for (int i = 0; i< NumberOfTotalDefaultStocks; i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);

            assertFalse(jsonObject.optString("name").isEmpty());
            assertFalse(jsonObject.optString("symbol").isEmpty());
            assertFalse(jsonObject.optString("rank").isEmpty());
            assertFalse(jsonObject.optString("price_usd").isEmpty());
            assertFalse(jsonObject.optString("price_btc").isEmpty());
            assertFalse(jsonObject.optString("24h_volume_usd").isEmpty());
            assertFalse(jsonObject.optString("market_cap_usd").isEmpty());
            assertFalse(jsonObject.optString("available_supply").isEmpty());
            assertFalse(jsonObject.optString("total_supply").isEmpty());
            assertFalse(jsonObject.optString("percent_change_1h").isEmpty());
            assertFalse(jsonObject.optString("percent_change_24h").isEmpty());
            assertFalse(jsonObject.optString("percent_change_7d").isEmpty());
            assertFalse(jsonObject.optString("last_updated").isEmpty());
        }
    }
    @Test
    public void getAPIDataByID() throws JSONException{
        String id = "bitcoin"; //will get the data only related to bitcoin
        JSONArray jsonArray = coinMarketCapRepo.retrieveQuotesAsJson(id, 0);
        assertNotNull(jsonArray);
        assertEquals(1, jsonArray.length());  //we should only be getting bitcoin

        JSONObject jsonObject = jsonArray.optJSONObject(0);

        assertFalse(jsonObject.optString("name").isEmpty());
        assertFalse(jsonObject.optString("symbol").isEmpty());
        assertFalse(jsonObject.optString("rank").isEmpty());
        assertFalse(jsonObject.optString("price_usd").isEmpty());
        assertFalse(jsonObject.optString("price_btc").isEmpty());
        assertFalse(jsonObject.optString("24h_volume_usd").isEmpty());
        assertFalse(jsonObject.optString("market_cap_usd").isEmpty());
        assertFalse(jsonObject.optString("available_supply").isEmpty());
        assertFalse(jsonObject.optString("total_supply").isEmpty());
        assertFalse(jsonObject.optString("percent_change_1h").isEmpty());
        assertFalse(jsonObject.optString("percent_change_24h").isEmpty());
        assertFalse(jsonObject.optString("percent_change_7d").isEmpty());
        assertFalse(jsonObject.optString("last_updated").isEmpty());
    }

    //Tests The limit feature.
    @Test
    public void getAPIDataWithLimit() throws JSONException {
        String blankID = "";  //should get all possible data if no id is provided
        int limit = 90;  //Limit the number of stocks to 90

        JSONArray jsonArray = coinMarketCapRepo.retrieveQuotesAsJson(blankID, limit);
        assertNotNull(jsonArray);

        for (int i = 0; i< limit; i++) {
           JSONObject jsonObject = jsonArray.optJSONObject(i);

            assertFalse(jsonObject.optString("name").isEmpty());
            assertFalse(jsonObject.optString("symbol").isEmpty());
            assertFalse(jsonObject.optString("rank").isEmpty());
            assertFalse(jsonObject.optString("price_usd").isEmpty());
            assertFalse(jsonObject.optString("price_btc").isEmpty());
            assertFalse(jsonObject.optString("24h_volume_usd").isEmpty());
            assertFalse(jsonObject.optString("market_cap_usd").isEmpty());
            assertFalse(jsonObject.optString("available_supply").isEmpty());
            assertFalse(jsonObject.optString("total_supply").isEmpty());
            assertFalse(jsonObject.optString("percent_change_1h").isEmpty());
            assertFalse(jsonObject.optString("percent_change_24h").isEmpty());
            assertFalse(jsonObject.optString("percent_change_7d").isEmpty());
            assertFalse(jsonObject.optString("last_updated").isEmpty());
       }
    }

    @Test
    public void getQuotes() throws JSONException{
        // Arrange
        String symbol ="bitcoin";

        // Act
        HashMap<String, StockQuote> stockQuotes = coinMarketCapRepo.getQuotes(symbol);

        // Assert
        assertEquals(1, stockQuotes.size());
        StockQuote BTCQuote = stockQuotes.get("BTC");
        assertEquals("BTC", BTCQuote.getSymbol());
        assertEquals("Bitcoin", BTCQuote.getName());
        assertFalse(BTCQuote.getVolume().isEmpty());
        assertFalse(BTCQuote.getPrice().isEmpty());
        assertFalse(BTCQuote.getPercent().isEmpty());
    }

    @Test
    public void findNameBySymbol() throws JSONException{
       List<String> symbols = new ArrayList<>();
       symbols.add("BTC");
       symbols.add("ETH");
       symbols.add("TRX");
       symbols.add("XLM");

       List<String> names = new ArrayList<>();
       names.add("Bitcoin");
       names.add("Ethereum");
       names.add("TRON");
       names.add("Stellar");

       for(int i = 0 ; i < symbols.size(); i++){
           assertEquals(names.get(i), coinMarketCapRepo.findName(symbols.get(i)));
       }
    }
}

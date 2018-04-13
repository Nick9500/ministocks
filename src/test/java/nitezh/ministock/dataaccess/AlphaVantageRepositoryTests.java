package nitezh.ministock.dataaccess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import nitezh.ministock.mocks.MockCache;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by GRao on 3/22/2018.
 */

public class AlphaVantageRepositoryTests {
    private AlphaVantageRepository alphaVantageRepository;
    JSONArray vantageList;
    List<String> timeInterval;
    String symbol;

    @Before
    public void setUp() {
        timeInterval = new ArrayList<>();
        timeInterval.add("DAILY");
        timeInterval.add("WEEKLY");
        timeInterval.add("MONTHLY");
        vantageList = null;
        FxChangeRepository fxRepository = new FxChangeRepository();
            this.alphaVantageRepository = new AlphaVantageRepository(fxRepository);
    }

    @Test
    public void retrieveRegularStockValues() throws JSONException{
        Assume.assumeTrue(System.getenv("CIRCLECI") == null);
        symbol = "GOOG";
        //test data retrieval for all time intervals
        for(String timeInterval : this.timeInterval) {
            vantageList = returnJSONArray(timeInterval, symbol);
            assertNotNull(vantageList);
            assertNotEquals(0, vantageList.length());
            JSONObject jsonObject = vantageList.optJSONObject(0);                    //validate if all fields of a specific date are properly received
            assertFalse(jsonObject.optString("date").isEmpty());
            assertFalse(jsonObject.optString("open").isEmpty());
            assertFalse(jsonObject.optString("high").isEmpty());
            assertFalse(jsonObject.optString("low").isEmpty());
            assertFalse(jsonObject.optString("close").isEmpty());
            assertFalse(jsonObject.optString("volume").isEmpty());
            assertTrue(jsonObject.optString("invalid_field").isEmpty());
        }
    }

    @Test
    public void retrieveDailyCryptoCurrencyValues() throws JSONException{
        symbol = "BTC";
        String market = "USD";
        //test data retrieval for all time intervals
        for(String timeInterval : this.timeInterval) {
            vantageList = returnCryptoJSONArray(timeInterval, symbol, market);
            assertNotNull(vantageList);
            assertNotEquals(0, vantageList.length());
            JSONObject jsonObject = vantageList.optJSONObject(0);                    //validate if all fields of a specific date are properly received
            assertFalse(jsonObject.optString("date").isEmpty());
            assertFalse(jsonObject.optString("open").isEmpty());
            assertFalse(jsonObject.optString("high").isEmpty());
            assertFalse(jsonObject.optString("low").isEmpty());
            assertFalse(jsonObject.optString("close").isEmpty());
            assertFalse(jsonObject.optString("volume").isEmpty());
            assertFalse(jsonObject.optString("market cap").isEmpty());
            assertTrue(jsonObject.optString("invalid_field").isEmpty());
        }
    }

    private JSONArray returnJSONArray(String timeInterval, String symbols) throws JSONException{
        return alphaVantageRepository.retrieveQuotesAsJson(new MockCache(), timeInterval, symbols);
    }

    private JSONArray returnCryptoJSONArray(String timeInterval, String symbols, String market) throws JSONException{
        return alphaVantageRepository.retrieveQuotesAsJson(new MockCache(), timeInterval, symbols, market);
    }

}

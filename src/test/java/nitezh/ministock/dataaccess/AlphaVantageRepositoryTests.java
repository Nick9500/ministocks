package nitezh.ministock.dataaccess;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nitezh.ministock.mocks.MockCache;
import static org.junit.Assert.assertNotNull;

/**
 * Created by GRao on 3/22/2018.
 */

public class AlphaVantageRepositoryTests {
    private AlphaVantageRepository alphaVantageRepository;
    JSONArray vantageList;
    String symbols;
    String timeInterval;
    @Before
    public void setUp() {
        vantageList = null;
        FxChangeRepository fxRepository = new FxChangeRepository();
        this.alphaVantageRepository = new AlphaVantageRepository(fxRepository);
    }

    @Test
    public void retrieveDailyValues() throws JSONException{
        symbols = "GOOG";
         timeInterval = "TIME_SERIES_DAILY";
        vantageList = returnJSONArray(timeInterval,symbols);
        assertNotNull(vantageList);
        Assert.assertEquals(1005, vantageList.length());
        /*JSONObject jsonObject = vantageList.optJSONObject(0);                    <---- returns unordered dates after it was retrieved
        //Assert.assertEquals("1092.7400", jsonObject.optString("open"));
        //Assert.assertEquals("1106.3000", jsonObject.optString("high"));*/
    }

    private JSONArray returnJSONArray(String timeInterval, String symbols) throws JSONException{
        return alphaVantageRepository.retrieveQuotesAsJson(new MockCache(), timeInterval, symbols);
    }

}

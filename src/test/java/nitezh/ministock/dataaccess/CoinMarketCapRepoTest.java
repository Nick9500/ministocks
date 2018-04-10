package nitezh.ministock.dataaccess;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

import nitezh.ministock.mocks.MockCache;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by raj34 on 2018-04-04.
 */

public class CoinMarketCapRepoTest {
    CoinMarketCapRepo coinMarketCapRepo;

    @Test
    public void getAllAPIData() throws JSONException{
        String blankID = "";  //should get all the data if no id is provided
        int NumberOfTotalStocks = 100;  //Number of total stocks from CoinMarketRepo

        coinMarketCapRepo = new CoinMarketCapRepo(new FxChangeRepository());
        JSONArray jsonArray = coinMarketCapRepo.retrieveQuotesAsJson(blankID);
        assertNotNull(jsonArray);
        assertEquals(NumberOfTotalStocks , jsonArray.length());  //we should be getting every stock
    }
    @Test
    public void getAPIDataByID() throws JSONException{
        String id = "bitcoin"; //will get the data only related to bitcoin
        coinMarketCapRepo = new CoinMarketCapRepo(new FxChangeRepository());
        JSONArray jsonArray = coinMarketCapRepo.retrieveQuotesAsJson(id);
        assertNotNull(jsonArray);
        assertEquals(1, jsonArray.length());  //we should only be getting bitcoin
    }



}

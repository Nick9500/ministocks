package nitezh.ministock.dataaccess;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

import nitezh.ministock.mocks.MockCache;

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
    public void getAPIData() throws JSONException{
        String id = "bitcoin";
        coinMarketCapRepo = new CoinMarketCapRepo(new FxChangeRepository());
        JSONArray jsonArray = coinMarketCapRepo.retrieveQuotesAsJson(id);
        assertNotNull(jsonArray);
    }

}

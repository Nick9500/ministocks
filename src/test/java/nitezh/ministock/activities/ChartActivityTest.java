package nitezh.ministock.activities;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import nitezh.ministock.utils.UrlDataTools;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;


public class ChartActivityTest {

    String symbol;


    @Test
    public void retrieveRSIJSONvalues() throws JSONException {
        symbol = "TSLA";
        String RSIurl = buildRSIUrl(symbol);
        String rsiVal = UrlDataTools.getUrlData(RSIurl);
        JSONObject rsi = new JSONObject(rsiVal);

        // Assert that the proper symbol has been pulled
        String rsiSymbol = rsi.getJSONObject("Meta Data").getString("1: Symbol");
        assertEquals("TSLA", rsiSymbol);

        // Assert that the RSI values are pulled
        assertFalse(rsi.optString("Technical Analysis: RSI").isEmpty());


    }

    @Test
    public void retrieveMACDJSONvalues() throws JSONException {
        symbol = "MSFT";
        String MACDurl = buildMACDURL(symbol);
        String macdVal = UrlDataTools.getUrlData(MACDurl);
        JSONObject macd = new JSONObject(macdVal);

        // Assert that the proper symbol has been pulled
        String macdSymbol = macd.getJSONObject("Meta Data").getString("1: Symbol");
        assertEquals("MSFT", macdSymbol);

        // Assert that the MACD values are pulled
        assertFalse(macd.optString("Technical Analysis: MACD").isEmpty());

    }


    private String buildRSIUrl(String symbol) {
        String RSIurl = "https://www.alphavantage.co/query?function=RSI&symbol="+symbol+"&interval=daily&time_period=14&series_type=close&apikey=ZKD8M6L9CEQAK89H";
        return  RSIurl;
    }

    private String buildMACDURL(String symbol) {
        String MACDurl = "https://www.alphavantage.co/query?function=MACD&symbol="+symbol+"&interval=weekly&series_type=close&apikey=ZKD8M6L9CEQAK89H";
        return MACDurl;
    }

}

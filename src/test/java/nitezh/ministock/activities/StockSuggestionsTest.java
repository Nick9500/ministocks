package nitezh.ministock.activities;


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

    @Before
    public void setUp()
    {
        userInput = "Bt";
        btcSymbol = "BTC";
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

        // Assert
        String expected = "[\n" +
                "    {\n" +
                "        \"id\": \"bitcoin\", \n" +
                "        \"name\": \"Bitcoin\", \n" +
                "        \"symbol\": \"BTC\"";
        assertEquals(expected, result);
    }
}

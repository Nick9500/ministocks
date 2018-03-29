package nitezh.ministock.domain;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


import nitezh.ministock.mocks.MockCache;
import nitezh.ministock.mocks.MockStorage;
import nitezh.ministock.mocks.MockWidgetRepository;

import static junit.framework.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


/**
 * Created by Cristi Arde on 3/22/2018.
 */




public class ImportFeatureTest {

    private StockQuoteRepository stockRepository;

    @Before
    public void setUp() {

         MockWidgetRepository mockWidgetRepository = new MockWidgetRepository();
         mockWidgetRepository.setWidgetsStockSymbols(new HashSet<>(Arrays.asList(
                 "AAPL",
                 "TSLA"
                )));
         stockRepository = new StockQuoteRepository(
                 new MockStorage(), new MockCache(), mockWidgetRepository);

    }
   private static File getFileFromPath(String fileName) {

       return new File("src/test/java/nitezh/ministock/domain/"+ fileName);
   }

    @Test
    public void testIfFileExists()
    {
        //using project path since SD card directory cannot be accessible during tests
        File file = new File("src/test/java/nitezh/ministock/domain/stocks.csv");
        assertTrue(file.exists());
    }
    @Test
    public void mockBufferReader() throws IOException {
        BufferedReader bufferedReader = mock(BufferedReader.class);
        String line1 = "AAPL";
        String line2 = "GOOGL";
        when(bufferedReader.readLine()).thenReturn(line1).thenReturn(line2);
        assertEquals(line1, bufferedReader.readLine());
        assertEquals(line2, bufferedReader.readLine());
    }


    @Test
    public void csvToString() throws IOException {

        File file = getFileFromPath("stocks.csv");
        assertThat(file, notNullValue());

        String testSteam = "AAPL\nGOOGL\nTESL";
        InputStream stream = new ByteArrayInputStream(testSteam.getBytes(StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        assertEquals("AAPL", reader.readLine());
        assertEquals("GOOGL", reader.readLine());
        assertEquals("TESL", reader.readLine());
    }

    @Test
    public void getQuotefromSymbol()
    {
        List<String> symbols = new ArrayList<String>();
        symbols.add("AAPL");
        symbols.add("TSLA");

        //getLiveQuotes
        HashMap<String, StockQuote> quotes = stockRepository.getLiveQuotes(symbols);

        //check if return anything
        System.out.println(quotes.size());
        assertEquals(2, quotes.size());

        //check if returned real data
        StockQuote aaplQuote = quotes.get("AAPL");
        assertEquals("AAPL", aaplQuote.getSymbol());
        assertThat(aaplQuote.getSymbol(), notNullValue());
        assertThat(aaplQuote.getPrice(), notNullValue());
        assertThat(aaplQuote.getPercent(), notNullValue());
        System.out.println("Symbol: "+ aaplQuote.getSymbol());
        System.out.println("Price: "+ aaplQuote.getPrice());
        System.out.println("Percent: "+ aaplQuote.getPercent());

        StockQuote teslaQuote = quotes.get("TSLA");
        assertEquals("TSLA", teslaQuote.getSymbol());
        assertThat(teslaQuote.getSymbol(), notNullValue());
        assertThat(teslaQuote.getPrice(), notNullValue());
        assertThat(teslaQuote.getPercent(), notNullValue());
        System.out.println("Symbol: "+ teslaQuote.getSymbol());
        System.out.println("Price: "+ teslaQuote.getPrice());
        System.out.println("Percent: "+ teslaQuote.getPercent());

    }

}

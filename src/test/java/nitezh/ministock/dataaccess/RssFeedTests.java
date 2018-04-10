package nitezh.ministock.dataaccess;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.net.HttpURLConnection;
import java.net.URL;

import static junit.framework.Assert.assertEquals;

/**
 * Created by nicholasfong on 2018-04-04.
 */

@RunWith(RobolectricTestRunner.class)
public class RssFeedTests {
    @Test
    public void accessNasdaqRSSTest(){
        URL url;
        String urlString ="http://articlefeeds.nasdaq.com/nasdaq/symbols?symbol=MSFT";
        try {
            url = new URL(urlString);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            request.connect();
            assertEquals(request.HTTP_OK, request.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void accessNasdaqRSSfailTest(){
        URL url;
        String urlString ="http://articlefeeds.nasdaaq.com/nasdaq/symbols?symbol=MSFT";
        try {
            url = new URL(urlString);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            request.connect();
            assertEquals(request.HTTP_BAD_REQUEST, request.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

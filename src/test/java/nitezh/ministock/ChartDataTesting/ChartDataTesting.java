package nitezh.ministock.ChartDataTesting;

import android.os.Bundle;
import android.test.AndroidTestCase;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import java.net.URL;
import java.net.HttpURLConnection;

import nitezh.ministock.activities.ChartActivity;
import nitezh.ministock.activities.MyData;

import static org.mockito.Mockito.mock;

/**
 * Created by GRao on 2/27/2018.
 */


@RunWith(RobolectricTestRunner.class)
public class ChartDataTesting extends AndroidTestCase {

    String cht;             //Chart Type
    String chd;             //Chart Data  (i.e. t:Data1,Data2,Data2,etc)
    String chds;            //Text Format custom scaling
    String chxt;            //Visible Axes
    String chs;             //Chart Size
    String chtt;            //Chart Title\
    String urlString;
    URL url;
    MyData myData;
    ChartActivity chartActivity;
    Bundle bundle;

    @Before
    public void setUp() {
        cht = "cht=ls";                                             //Line Graph
        chd = "chd=t%3A";                                           //Data of line graph. Must begin with t:
        chds = "chds=a";                                            //Automatic text format scaling
        chxt ="chxt=x%2Cy";                                         //Specify X and Y Axes
        chs = "chs=500x500";                                        //chart size
        chtt = "chtt=Test%20Graph";                                 //Name of Graph
        chartActivity = mock(ChartActivity.class);
        bundle = mock(Bundle.class);
    }

    @Test
    public void generateGraphTest(){
        int var1 = 10;
        int var2 = 5;
        int var3 = 20;
        int var4 = 55;

        urlString = "https://image-charts.com/chart?"+cht+"&"+chd+var1+"%2C"+var2+"%2C"+var3+"%2C"+var4+"&"+chds+"&chof=.png&"+chs+
                "&chdls=000000&chco=F56991%2CFF9F80%2CFFC48C%2CD1F2A5%2CEFFAB4&"+chtt+"&"+chxt+"&chdlp=b&chf=bg%2Cs%2CFFFFFF&chbh=10&icwt=false";


        try{
           url = new URL(urlString);
           HttpURLConnection request = (HttpURLConnection) url.openConnection();
           request.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
           request.connect();
           assertEquals(request.HTTP_OK, request.getResponseCode());

           myData.setURLString(urlString);
           //chartActivity.onCreate(bundle);
        }
        catch( Exception e ){
            e.printStackTrace();
        }

    }
}
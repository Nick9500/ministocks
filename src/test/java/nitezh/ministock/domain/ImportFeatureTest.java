package nitezh.ministock.domain;

import android.os.Environment;

import com.thoughtworks.xstream.io.path.Path;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import nitezh.ministock.activities.GlobalWidgetData;
import nitezh.ministock.activities.PreferencesActivity;
import nitezh.ministock.activities.widget.WidgetRow;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.*;

/**
 * Created by Cristi Arde on 3/22/2018.
 */




public class ImportFeatureTest {

   /* @Before
    public void setUp() {



    }*/
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
    public void csvToString()
    {
        File file = getFileFromPath("stocks.csv");
        assertThat(file, notNullValue());
    }


}

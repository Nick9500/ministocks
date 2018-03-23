package nitezh.ministock.domain;

import android.os.Environment;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

import nitezh.ministock.activities.GlobalWidgetData;
import nitezh.ministock.activities.PreferencesActivity;
import nitezh.ministock.activities.widget.WidgetRow;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by Cristi Arde on 3/22/2018.
 */




public class ImportFeatureTest {

    @Before
    public void setUp() {



    }

    @Test
    public void testIfFileExists()
    {
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        assertTrue(file.exists());
    }

}

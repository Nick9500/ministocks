package nitezh.ministock;


import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Created by Cristi Arde on 4/11/2018.
 */

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class ImportUITests {

    UiDevice mDevice;

    @Before
    public void setup() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @After
    public void finish() { mDevice.pressBack(); }

    private void selectPreferences() throws UiObjectNotFoundException {
        String preferencesResourceId = "nitezh.ministock:id/prefs_but";
        UiObject button = mDevice.findObject(new UiSelector().resourceId(preferencesResourceId));
        button.clickAndWaitForNewWindow();
    }

    private void clickImport() throws UiObjectNotFoundException  {
        String importBtn = "Import";
        UiScrollable preferencesListView = new UiScrollable(new UiSelector());
        preferencesListView.setMaxSearchSwipes(100);
        preferencesListView.scrollTextIntoView(importBtn);
        preferencesListView.waitForExists(3000);
        UiObject preferencesListItem =
                preferencesListView.getChildByText(new UiSelector().className(android.widget.TextView.class.getName()),
                        importBtn);
        preferencesListItem.click();
    }


    @Test
    public void importUITest()throws UiObjectNotFoundException{
        selectPreferences();
        clickImport();
    }
}

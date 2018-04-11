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
 * Created by Cristi Arde on 4/10/2018.
 */

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class ExportUITests {
    UiDevice mDevice;


    @Before
    public void setup() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @After
    public void finish() {
        mDevice.pressBack();    //After every test go back to home screen
        mDevice.pressBack();    //After every test go back to home screen
    }

    private void selectPreferences() throws UiObjectNotFoundException {
        String preferencesResourceId = "nitezh.ministock:id/prefs_but";
        UiObject button = mDevice.findObject(new UiSelector().resourceId(preferencesResourceId));
        button.clickAndWaitForNewWindow();
    }

    private void selectStockSetup() throws UiObjectNotFoundException {
        String stockSetup = "Stocks setup";
        UiScrollable preferencesListView = new UiScrollable(new UiSelector());
        preferencesListView.setMaxSearchSwipes(100);
        preferencesListView.scrollTextIntoView(stockSetup);
        preferencesListView.waitForExists(3000);
        UiObject preferencesListItem =
                preferencesListView.getChildByText(new UiSelector().className(android.widget.TextView.class.getName()),
                        stockSetup);
        preferencesListItem.click();
    }

    private UiObject selectStockView(int index) throws UiObjectNotFoundException {
        String searchFieldResourceId = "android:id/search_src_text";
        UiScrollable stockListView = new UiScrollable(new UiSelector());
        stockListView.getChild(new UiSelector().clickable(true).index(index)).click();
        UiObject searchField = mDevice.findObject(new UiSelector().resourceId(searchFieldResourceId));
        return searchField;
    }

    private void setStock(int index, String symbolToAdd) throws UiObjectNotFoundException {
        UiObject searchField = selectStockView(index);
        searchField.setText(symbolToAdd);
        searchField.clickAndWaitForNewWindow(5000);
        mDevice.pressDPadDown();
        mDevice.pressDPadUp();
        mDevice.pressEnter();

    }

    private void removeStock(int index) throws UiObjectNotFoundException {
        UiObject searchField = selectStockView(index);
        searchField.clearTextField();
        searchField.clickAndWaitForNewWindow(3000);
        mDevice.click(540, 200);
    }
    private void clickExport() throws UiObjectNotFoundException  {
        String export = "Export";
        UiScrollable preferencesListView = new UiScrollable(new UiSelector());
        preferencesListView.setMaxSearchSwipes(100);
        preferencesListView.scrollTextIntoView(export);
        preferencesListView.waitForExists(3000);
        UiObject preferencesListItem =
                preferencesListView.getChildByText(new UiSelector().className(android.widget.TextView.class.getName()),
                        export);
        preferencesListItem.click();
    }

    private void addEmail()throws UiObjectNotFoundException {


    }
    @Test
    public void exportTest() throws UiObjectNotFoundException{
        selectPreferences();                        // Click Preferences Button
        selectStockSetup();
        setStock(1, "FB");       // Add 2nd Stock
        setStock(0, "MMD");
        removeStock(1);
        mDevice.pressBack();
        clickExport();

    }


}


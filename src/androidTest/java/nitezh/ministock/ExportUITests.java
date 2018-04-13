package nitezh.ministock;


import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.view.KeyEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertTrue;

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
        mDevice.pressBack();    // After every test go back to home screen
        mDevice.pressBack();    // After every test go back to home screen
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
        searchField.waitForExists(3000);
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
        for (int i = 0; i < 8; i++) // 8 is max length of a stock, defined in stock suggestions
            mDevice.pressKeyCode(KeyEvent.KEYCODE_DEL);
        searchField.clickAndWaitForNewWindow(2000);
        mDevice.pressKeyCode(KeyEvent.KEYCODE_SPACE);
        mDevice.pressDPadDown();
        mDevice.pressDPadDown();
        mDevice.pressDPadDown();
        mDevice.pressEnter();
    }

    private void clickExport() throws UiObjectNotFoundException  {
        String export = "Export";
        UiScrollable preferencesListView = new UiScrollable(new UiSelector());
        preferencesListView.setMaxSearchSwipes(100);
        preferencesListView.scrollTextIntoView(export);
        preferencesListView.waitForExists(3000);
        UiObject exportListItem =
                preferencesListView.getChildByText(new UiSelector().className(android.widget.TextView.class.getName()),
                        export);
        exportListItem.click();
    }

    private UiObject selectEmailField() throws UiObjectNotFoundException {
        String emailFieldID = "nitezh.ministock:id/emailFieldID";
        UiObject emailField = mDevice.findObject(new UiSelector().resourceId(emailFieldID));
        emailField.waitForExists(3000);

        return emailField;
    }

    private void setEmailAddress(String emailAddress) throws UiObjectNotFoundException {
        UiObject emailField = selectEmailField();
        emailField.setText(emailAddress);
    }

    private void sendEmail() throws UiObjectNotFoundException {
        String sendButtonID = "android:id/button1";
        UiObject sendButton = mDevice.findObject(new UiSelector().resourceId(sendButtonID));
        sendButton.waitForExists(3000);
        sendButton.click();
    }

    private void setStock(int index, List<Integer> keyCodes) throws UiObjectNotFoundException {
        UiObject searchField = selectStockView(index);
        searchField.clearTextField();
        for (Integer keyCode : keyCodes)
            mDevice.pressKeyCode(keyCode);
        searchField.clickAndWaitForNewWindow(2000);
        mDevice.pressDPadDown();
        mDevice.pressDPadUp();
        mDevice.pressEnter();
    }

    // Confirms the presence of the alert dialog on invalid e-mail
    private void confirmFailure() throws UiObjectNotFoundException{
        String alertTitleField = "android:id/alertTitle";
        UiObject alertTitle = mDevice.findObject(new UiSelector().resourceId(alertTitleField));
        alertTitle.waitForExists(3000);
        assertTrue(alertTitle.exists());
    }

    //@Test
    public void exportTest() throws UiObjectNotFoundException{
        selectPreferences();                        // Click Preferences Button
        selectStockSetup();
        setStock(1, Arrays.asList(KeyEvent.KEYCODE_F, KeyEvent.KEYCODE_B));                     // Stock: FB
        setStock(0, Arrays.asList(KeyEvent.KEYCODE_M, KeyEvent.KEYCODE_M, KeyEvent.KEYCODE_D)); // Stock: MMD
        removeStock(1);
        mDevice.pressBack();
        clickExport();
        String emailAddress = "ministocks34@gmail.com";
        setEmailAddress(emailAddress);
        sendEmail();
    }

    @Test
    public void emailValidatorTest() throws UiObjectNotFoundException{
        selectPreferences();    // Click Preferences Button
        clickExport();          // Select Export Item
        String emailAddress = "Invalid Email Format";
        setEmailAddress(emailAddress);
        sendEmail();
        confirmFailure();
    }
}


package nitezh.ministock.activities;

import android.util.Log;

import junit.framework.Assert;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import nitezh.ministock.BuildConfig;
import nitezh.ministock.activities.widget.WidgetRow;
import nitezh.ministock.domain.Widget;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;

/**
 * Created by raj34 on 2018-03-15.
 */
public class EmailTest {

    //Testing if an exception is thrown when an invalid email is provided by the user
    @Test
    public void TestInvalidEmail() {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(BuildConfig.EmailSecID, BuildConfig.EmailSecPass);
                    }
                });
        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);
            String fromAddress = BuildConfig.EmailSecID;
            String toAddress = "InvalidEmail";
            //Setting sender address
            mm.setFrom(new InternetAddress(fromAddress));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            //Adding subject
            mm.setSubject("Ministocks: Data CSV file Export");
            //Adding message
            mm.setText("You will find your requested data csv file attached to this email!", "utf-8", "html");

            //Sending email
            Transport.send(mm);
            //If this point is reached, it means no exception was thrown
            Assert.fail();
        } catch (MessagingException max) {
            max.printStackTrace();
        }
    }

    @Test
    public void ReadFromFileSent() {
        //read from file, data can be stubbed
        //create a file and store it
        File file = new File("data.txt");

        Widget widget = mock(Widget.class);
        WidgetRow row = new WidgetRow(widget);

        try {  //write data to file from a Widget Row
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter outWriter = new OutputStreamWriter(fOut);

            row.setSymbol("$XDJ1");

            outWriter.append(row.getSymbol());
            outWriter.append("\n");

            outWriter.close();

            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

        //reading the symbol value from file
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        assertNotNull(row.getSymbol());
        assertNotNull(text.toString());
        assertEquals(row.getSymbol(),text.toString());
    }

}
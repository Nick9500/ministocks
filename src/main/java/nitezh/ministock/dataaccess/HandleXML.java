package nitezh.ministock.dataaccess;

/**
 * Created by nicholasfong on 2018-04-04.
 */

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class HandleXML {
    private String title = "title";
    private String link = "link";
    private String description = "description";
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    public boolean parsingComplete;
    public ArrayList<RssItem> rssList = new ArrayList<>();

    public HandleXML(String url){
        this.urlString = url;
        this.parsingComplete = false;
    }

    public String getTitle(){
        return title;
    }

    public String getLink(){
        return link;
    }

    public String getDescription(){
        return description;
    }

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text=null;

        try {
            event = myParser.getEventType();
            boolean sawPageTitle = false;
            RssItem item = new RssItem();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();
                switch (event){
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:

                        if (name.equals("title") && !sawPageTitle) {
                            //Because the first "title" tag is "Latest News" not linked to a story
                            sawPageTitle = true;
                        }
                        if(name.equals("title") && sawPageTitle){
                            item.setTitle(text);
                        }
                        else if(name.equals("link") && sawPageTitle){
                            item.setLink(text);
                        }
                        else if(name.equals("pubDate") && sawPageTitle){
                            item.setDescription(text);
                        }
                        else{
                        }

                        break;
                }
                if ( sawPageTitle && item.getDescription() != "" && item.getTitle() != "" && item.getLink() != "" ){
                    // If it's a complete RSSItem object with all field filled out, add it to RssList
                    rssList.add(item);
                    // Re-init a new item
                    item = new RssItem();
                    event = myParser.next();
                }
                else {
                    event = myParser.next();
                }
            }
            parsingComplete = true;
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<RssItem> getRssList(){
        return this.rssList;
    }

    public void fetchXML(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);

                    parseXMLAndStoreIt(myparser);
                    stream.close();
                }

                catch (Exception e) {
                }
            }
        });
        thread.start();
    }
}

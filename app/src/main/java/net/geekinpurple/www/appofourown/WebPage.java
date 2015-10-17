package net.geekinpurple.www.appofourown;


import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Briannon on 2015-10-16.
 */
public class WebPage {

    public static Document retrieve(String url, String callingClass) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            return doc;
        }
        catch (IOException ioEx) {
            Log.d("connection", callingClass + " connection failed " + url);
            return null;
        }
    }

}

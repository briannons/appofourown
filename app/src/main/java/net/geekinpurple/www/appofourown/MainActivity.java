package net.geekinpurple.www.appofourown;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import net.geekinpurple.www.appofourown.R;

public class MainActivity extends ActionBarActivity {
    String homeUrl = "https://archiveofourown.org";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Document ao3 =  Jsoup.connect(homeUrl).get();
            Elements media = ao3.getElementsByClass("browse");
            Element ele = media.get(0);  // there should only be one class "browse"
            media = ele.getElementsByTag("a");
            StringBuffer message = new StringBuffer();
            for (Element medium : media) {
                message.append(medium.data()).append('\n');
            }

            TextView textView = new TextView(this);
            textView.setTextSize(18);
            textView.setText(message);
        }
        catch (IOException aeiou) {  // I was going to use ioe. heh, all that's missing is au
            TextView textView = new TextView(this);
            textView.setTextSize(18);
            textView.setText("Sorry!");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

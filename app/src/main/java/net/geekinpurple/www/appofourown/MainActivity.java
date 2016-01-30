package net.geekinpurple.www.appofourown;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;

public class MainActivity extends ActionBarActivity {
    protected final static String HOME_URL = "https://archiveofourown.org";
    public final static String EXTRA_SEARCH_URL = "net.geekinpurple.www.appofourown.QUERY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Retrieval().execute(HOME_URL);
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

    public void throwSearch(View view) {
        try {
            Intent intent = new Intent(this, SearchResults.class);
            EditText editText = (EditText) findViewById(R.id.searchQuery);
            String query = editText.getText().toString();
            query = URLEncoder.encode(query, "UTF-8");
            String urlBase = new String("http://archiveofourown.org/works/search?utf8=%E2%9C%93&work_search%5Bquery%5D=");
            String queryUrl = urlBase.concat(query);
            Log.d("testing", queryUrl);
            intent.putExtra(EXTRA_SEARCH_URL, queryUrl);
            startActivity(intent);
        }
        catch (Exception e) {
        }
    }

    private class Retrieval extends AsyncTask<String, Void, String> {
        Document doc;

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            Document ao3 = WebPage.retrieve(url, this.getClass().toString());

            Elements media = ao3.getElementsByClass("browse");
            Element ele = media.get(0);  // there should only be one class "browse"

            media = ele.getElementsByTag("a");
            StringBuffer message = new StringBuffer();
            for (Element medium : media) {
                message.append(medium.text()).append('\n');
            }

            String result = message.toString();
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView textView = (TextView) findViewById(R.id.options);
            textView.setText(result);
        }
    }
}

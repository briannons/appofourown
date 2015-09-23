package net.geekinpurple.www.appofourown;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SearchResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();
        String url = intent.getStringExtra(MainActivity.EXTRA_SEARCH_URL);

        new Retrieval().execute(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
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

    private class Retrieval extends AsyncTask<String, Void, String> {
        Document doc;

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            try {
                Document ao3 =  Jsoup.connect(url).get();
                Elements works = ao3.getElementsByClass("blurb");

                StringBuffer message = new StringBuffer();
                for (Element work : works) { // for each story
                    Elements workLinks = work.getElementsByTag("a");
                    Element title = workLinks.get(0); // work title
                    message.append(title.text()).append('\n');
                }

                Log.d("something", message.toString());
                String result = message.toString();
                return result;
            }
            catch (IOException aeiou) {
                return "Sorry!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            TextView textView = (TextView) findViewById(R.id.results);
            textView.setText(result);
        }
    }
}

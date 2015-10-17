package net.geekinpurple.www.appofourown;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SearchResults extends ListActivity {
    ArrayAdapter result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();
        String url = intent.getStringExtra(MainActivity.EXTRA_SEARCH_URL);

        result = new ArrayAdapter(this, R.layout.individual_work, R.id.details);
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Work message = (Work) l.getItemAtPosition(position);
        Intent i = new Intent(this, WorkInstance.class);
        i.putExtra("work", message);
        Log.d("testing", message.toString());
        startActivity(i);
    }

    private class Retrieval extends AsyncTask<String, Void, ArrayList<Work>> {
        @Override
        protected ArrayList<Work> doInBackground(String... params) {
            String url = params[0];
            Document doc = WebPage.retrieve(url, this.getClass().toString());
            if (doc == null) {
                return new ArrayList<Work>();
            }

            Elements works = doc.getElementsByClass("blurb");

            StringBuffer message = new StringBuffer();

            // Build the array to hand to ArrayAdapter
            ArrayList<Work> worksList = new ArrayList<Work>();
            for (Element work : works) {
                Work w = new Work(work);
                worksList.add(w);
                message.append(w.toString());
            }

            return worksList;
        }

        @Override
        protected void onPostExecute(ArrayList<Work> works) {
            result.addAll(works);
            setListAdapter(result);
        }
    }
}

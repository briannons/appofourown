package net.geekinpurple.www.appofourown;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class WorkInstance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_instance);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        Work work = b.getParcelable("work");
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(work.title);

        // Set Rating Box Details
        TextView rating = (TextView) findViewById(R.id.rating);
        work.setView(work.rating, rating);

        // Set Warnings Box Details
        TextView warnings = (TextView) findViewById(R.id.warnings);
        work.setView(work.warnings, warnings);

        // Set Category Box Details
        TextView category = (TextView) findViewById(R.id.category);
        work.setView(work.category, category);

        // Set Status Box Details
        TextView status = (TextView) findViewById(R.id.status);
        work.setView(work.status, status);

        TextView summary = (TextView) findViewById(R.id.summary);
        summary.setText(work.summary);
        Log.d("testing", work.summary);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_work_instance, menu);
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

    //region Utilities
    private void tagHelper(SpannableStringBuilder build, String href, String tag) {
        ClickableSpan clickableSpan = new ClickableSpan() {
            public void onClick(View v) {
                // TODO: throw to search results-esque page for tags
                return;
            }
        };

        if (build.length() > 0) {
            build.append(", ");
        }
        int len = build.length();
        build.append(tag);
        build.setSpan(clickableSpan, len, build.length(), 0);
    }

    //endregion
}

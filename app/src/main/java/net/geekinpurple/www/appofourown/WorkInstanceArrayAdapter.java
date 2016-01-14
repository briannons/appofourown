package net.geekinpurple.www.appofourown;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Briannon on 2015-11-25.
 */
public class WorkInstanceArrayAdapter extends ArrayAdapter<Work> {

    public WorkInstanceArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v==null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=vi.inflate(R.layout.individual_work, null);
        }

        Work work = getItem(pos);
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setText(work.title);
        title.setTextColor(Color.BLACK);
        title.setBackgroundColor(Color.WHITE);

        // Set Rating Box Details
        TextView rating = (TextView) v.findViewById(R.id.rating);
        work.setView(work.rating, rating);

        // Set Warnings Box Details
        TextView warnings = (TextView) v.findViewById(R.id.warnings);
        work.setView(work.warnings, warnings);

        // Set Category Box Details
        TextView category = (TextView) v.findViewById(R.id.category);
        work.setView(work.category, category);

        // Set Status Box Details
        TextView status = (TextView) v.findViewById(R.id.status);
        work.setView(work.status, status);

        return v;
    }

}

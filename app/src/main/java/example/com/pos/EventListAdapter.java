package example.com.pos;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by root on 9/9/17.
 */

public class EventListAdapter extends ArrayAdapter<EventDataModel> {

    private Context context;
    private ArrayList<EventDataModel> dataModelArrayList;

    public EventListAdapter(Context context, ArrayList<EventDataModel> dataModelArrayList) {
        super(context, R.layout.event_list_item, dataModelArrayList);

        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.event_list_item, parent, false);

        TextView labelView = (TextView) rowView.findViewById(R.id.label);
        TextView valueView = (TextView) rowView.findViewById(R.id.value);
        TextView todayTextView = (TextView) rowView.findViewById(R.id.todayTextView);

        final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.eventItemCheckBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(checkBox.isChecked()) {
                    EventsListActivity.deletionIdList.add(dataModelArrayList.get(position).getID());
                }
                else if(!checkBox.isChecked()) {
                    if(EventsListActivity.deletionIdList.contains(dataModelArrayList.get(position).getID())) {
                        EventsListActivity.deletionIdList.remove(dataModelArrayList.get(position).getID());
                    }
                }
            }
        });

        labelView.setText(dataModelArrayList.get(position).getTitle());
        valueView.setText(dataModelArrayList.get(position).getLocation());

        Calendar today = Calendar.getInstance();
        Calendar eventDate = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            eventDate.setTime(dateFormat.parse(dataModelArrayList.get(position).getTimeAndDate()));
        } catch (ParseException ex) {
            Log.d("PARSE_EXCEPTION", ex.toString());
        }

        if (today.get(Calendar.DAY_OF_MONTH) == eventDate.get(Calendar.DAY_OF_MONTH)
                && today.get(Calendar.MONTH) == eventDate.get(Calendar.MONTH)
                && today.get(Calendar.YEAR) == eventDate.get(Calendar.YEAR)) {
            todayTextView.setVisibility(View.VISIBLE);
        }

        return rowView;
    }
}
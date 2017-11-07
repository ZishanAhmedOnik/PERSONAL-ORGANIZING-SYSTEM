package example.com.pos;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class EventsListActivity extends AppCompatActivity {

    public static final String EVENTS_ID = "EVENTS_ID";

    public static ArrayList<String> deletionIdList = new ArrayList<>();

    EventsDBHelper eventsDBHelper;

    ListView lvEvents;

    ArrayList<EventDataModel> eventDataModels;

    EventListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        lvEvents = (ListView) findViewById(R.id.lvEvents);
        lvEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventDataModel model = adapter.getItem(i);

                Intent intent = new Intent(EventsListActivity.this, EventDetailsActivity.class);
                intent.putExtra(EVENTS_ID, model.getID());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        fetchFromDB();
    }

    @Override
    protected void onPause() {
        eventsDBHelper.close();
        deletionIdList.clear();

        super.onPause();
    }

    public void addNewEvent(View view) {
        Intent intent = new Intent(EventsListActivity.this, EventDetailsActivity.class);
        this.startActivity(intent);
    }

    public void fetchFromDB() {
        eventsDBHelper = new EventsDBHelper(this);

        SQLiteDatabase db = eventsDBHelper.getReadableDatabase();

        String projection[] = {
                EventsDBHelper.EventEntity._ID,
                EventsDBHelper.EventEntity.COL_EVENT_NAME,
                EventsDBHelper.EventEntity.COL_EVENT_DATE_TIME,
                EventsDBHelper.EventEntity.COL_EVENT_LOCATION
        };

        Cursor cursor = db.query(EventsDBHelper.EventEntity.TABLE_NAME, projection,  null, null, null, null, null);

        ArrayList<EventDataModel> eventDataModelArrayList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String ID = cursor.getString( cursor.getColumnIndexOrThrow(EventsDBHelper.EventEntity._ID));
            String eventName = cursor.getString( cursor.getColumnIndexOrThrow(EventsDBHelper.EventEntity.COL_EVENT_NAME));
            String dateTime = cursor.getString( cursor.getColumnIndexOrThrow(EventsDBHelper.EventEntity.COL_EVENT_DATE_TIME));
            String location = cursor.getString( cursor.getColumnIndexOrThrow(EventsDBHelper.EventEntity.COL_EVENT_LOCATION));

            EventDataModel model = new EventDataModel(ID, eventName, location, dateTime);
            eventDataModelArrayList.add(model);
        }

        adapter = new EventListAdapter(this, eventDataModelArrayList);
        lvEvents.setAdapter(adapter);

        db.close();
    }

    public void deleteEvents(View view) {
//        try {
//            for (String str : deletionIdList) {
//                Log.d("DELETE_ID", str);
//            }
//        }catch (Exception ex) {
//            Log.d("DELETE_ID", ex.toString());
//        }

        new AlertDialog.Builder(this)
                .setTitle("DELETE?")
                .setMessage("ARE YOU SURE?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(String ID : deletionIdList) {
                            String selection = FriendsDbHelper.FriendEntry._ID + " = ?";
                            String[] selectionArgs = {ID};

                            SQLiteDatabase db = eventsDBHelper.getWritableDatabase();
                            db.delete(EventsDBHelper.EventEntity.TABLE_NAME, selection, selectionArgs);
                        }

                        fetchFromDB();
                        deletionIdList.clear();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
}

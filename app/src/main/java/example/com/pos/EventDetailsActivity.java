package example.com.pos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventDetailsActivity extends AppCompatActivity {
    TextView etDate;
    TextView etTime;

    EditText etEventName;
    EditText etEventLocation;

    String time;
    String date;
    String ID;

    EventsDBHelper eventsDBHelper;

    int day;
    int month;
    int year;

    int hh;
    int mm;

    boolean timeSet = false;
    boolean dateSet = false;
    boolean updateChanges = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        etDate = (TextView) findViewById(R.id.etDate);
        etTime = (TextView) findViewById(R.id.etTime);

        etEventName = (EditText) findViewById(R.id.etEventName);
        etEventLocation = (EditText) findViewById(R.id.etEventLocation);

        Date d = new Date();
    }

    @Override
    protected void onResume() {
        super.onResume();

        eventsDBHelper = new EventsDBHelper(this);

        Bundle extras = this.getIntent().getExtras();
        if(extras != null) {
            ID = extras.getString(EventsListActivity.EVENTS_ID);
            filloutEventsFrom();

            updateChanges = true;

            Button eventSaveButton = (Button) findViewById(R.id.eventSaveButton);
            eventSaveButton.setText("SAVE CHANGES");
        }
    }

    @Override
    protected void onPause() {
        eventsDBHelper.close();

        super.onPause();
    }

    public void openDatePicker(View view) {

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        date = dayOfMonth + " - " + (monthOfYear + 1) + " - " + year;

                        etDate.setText("Date: " + date);
                        etDate.setVisibility(View.VISIBLE);
                        dateSet = true;

                        day = dayOfMonth;
                        month = monthOfYear;
                        EventDetailsActivity.this.year = year;
                    }
                }
        );


        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    public void openTimePicker(View view) {

        final TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        int hod = hourOfDay;

                        if(hod > 12) {
                            hod -= 12;
                            time = hod + " : " + minute + "pm";
                        }
                        else {
                            time = hod + " : " + minute + "am";
                        }

                        etTime.setText("Time: " + time);
                        etTime.setVisibility(View.VISIBLE);

                        timeSet = true;

                        hh = hourOfDay;
                        mm = minute;
                    }
                }, false);


        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    public void saveEvent(View view) {
        if(!formValidate()) {
            Toast.makeText(this, "PLEASE FILLOUT ALL THE FIELDS!", Toast.LENGTH_SHORT).show();

            return;
        }

        String eventName = etEventName.getText().toString();
        String eventLocation = etEventLocation.getText().toString();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.HOUR_OF_DAY, hh);
        cal.set(Calendar.MINUTE, mm);
        cal.set(Calendar.SECOND, 0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTimeString = dateFormat.format(cal.getTime());

        Log.d("EVENT", eventName);
        Log.d("EVENT", eventLocation);
        Log.d("EVENT", dateTimeString);

        SQLiteDatabase db = eventsDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventsDBHelper.EventEntity.COL_EVENT_NAME, eventName);
        values.put(EventsDBHelper.EventEntity.COL_EVENT_DATE_TIME, dateTimeString);
        values.put(EventsDBHelper.EventEntity.COL_EVENT_LOCATION, eventLocation);

        if(updateChanges) {
            String selection = EventsDBHelper.EventEntity._ID + " = ?";
            String[] selectionArgs = { ID };

            db.update(EventsDBHelper.EventEntity.TABLE_NAME, values, selection, selectionArgs);
        }
        else {
            ID = db.insert(EventsDBHelper.EventEntity.TABLE_NAME, null, values) + "";
        }

        Toast.makeText(this, "SAVED!", Toast.LENGTH_SHORT).show();
    }

    private void filloutEventsFrom() {
        SQLiteDatabase db = eventsDBHelper.getReadableDatabase();

        String[] projection = {
                EventsDBHelper.EventEntity._ID,
                EventsDBHelper.EventEntity.COL_EVENT_NAME,
                EventsDBHelper.EventEntity.COL_EVENT_DATE_TIME,
                EventsDBHelper.EventEntity.COL_EVENT_LOCATION
        };

        String selection = EventsDBHelper.EventEntity._ID + " = ?";
        String[] selectionArgs = { ID };

        Cursor cursor = db.query(EventsDBHelper.EventEntity.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            String ID = cursor.getString( cursor.getColumnIndexOrThrow(EventsDBHelper.EventEntity._ID));
            String eventName = cursor.getString( cursor.getColumnIndexOrThrow(EventsDBHelper.EventEntity.COL_EVENT_NAME));
            String dateTime = cursor.getString( cursor.getColumnIndexOrThrow(EventsDBHelper.EventEntity.COL_EVENT_DATE_TIME));
            String location = cursor.getString( cursor.getColumnIndexOrThrow(EventsDBHelper.EventEntity.COL_EVENT_LOCATION));

            etEventName.setText(eventName);
            etEventLocation.setText(location);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {
                calendar.setTime(dateFormat.parse(dateTime));
            } catch (ParseException ex) {
                Log.d("PARSE_EXCEPTION", ex.toString());
            }

            date = calendar.get(Calendar.DAY_OF_MONTH) + " - " + (calendar.get(Calendar.MONTH) + 1) + " - " + calendar.get(Calendar.YEAR);

            etDate.setText("Date: " + date);
            etDate.setVisibility(View.VISIBLE);
            dateSet = true;

            int hod = calendar.get(Calendar.HOUR_OF_DAY);

            if(hod > 12) {
                hod -= 12;
                time = hod + " : " + calendar.get(Calendar.MINUTE) + "pm";
            }
            else {
                time = hod + " : " + calendar.get(Calendar.MINUTE) + "am";
            }

            etTime.setText("Time: " + time);
            etTime.setVisibility(View.VISIBLE);
            timeSet = true;

//            Log.d("EVENT_DETAILS", calendar.get(Calendar.DAY_OF_MONTH) + "");
//            Log.d("EVENT_DETAILS", calendar.get(Calendar.MONTH) + "");
//            Log.d("EVENT_DETAILS", calendar.get(Calendar.YEAR) + "");
//            Log.d("EVENT_DETAILS", dateTime);
//            Log.d("EVENT_DETAILS", location);
        }
    }

    private boolean formValidate() {
        if(etEventName.getText().length() != 0 && etEventLocation.getText().length() != 0 && dateSet && timeSet) {
            return true;
        }

        return false;
    }
}
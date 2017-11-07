package example.com.pos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by root on 9/7/17.
 */

public class EventsDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "pos.db";

    public static class EventEntity implements BaseColumns {
        public static final String  TABLE_NAME          =   "event";
        public static final String  COL_EVENT_NAME      =   "event_name";
        public static final String  COL_EVENT_DATE_TIME =   "event_date_time";
        public static final String  COL_EVENT_LOCATION  =   "event_location";
    }

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + EventEntity.TABLE_NAME + " (" +
                                                    EventEntity._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                    EventEntity.COL_EVENT_NAME + " TEXT, " +
                                                    EventEntity.COL_EVENT_DATE_TIME + " DATETIME, " +
                                                    EventEntity.COL_EVENT_LOCATION + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EventEntity.TABLE_NAME;

    public EventsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

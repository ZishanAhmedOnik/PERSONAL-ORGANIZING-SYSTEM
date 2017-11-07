package example.com.pos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by root on 9/6/17.
 */

public class FriendsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "pos1.db";

    public static class FriendEntry implements BaseColumns {
        public static final String  TABLE_NAME      =   "friend";
        public static final String  COL_FIRST_NAME  =   "first_name";
        public static final String  COL_LAST_NAME   =   "last_name";
        public static final String  COL_GENDER      =   "gender";
        public static final String  COL_AGE         =   "age";
        public static final String  COL_ADDR        =   "addr";
    }

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + FriendEntry.TABLE_NAME + "(" +
                                                    FriendEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                    FriendEntry.COL_FIRST_NAME + " TEXT, " +
                                                    FriendEntry.COL_LAST_NAME + " TEXT, " +
                                                    FriendEntry.COL_GENDER + " TEXT, " +
                                                    FriendEntry.COL_AGE + " INTEGER, " +
                                                    FriendEntry.COL_ADDR + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FriendEntry.TABLE_NAME;

    public FriendsDbHelper(Context context) {
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

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
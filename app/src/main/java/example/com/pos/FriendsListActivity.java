package example.com.pos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FriendsListActivity extends AppCompatActivity {

    public static final String FRIENDS_INDEX = "FRIENDS_INDEX";

    ListView lvFriends;

    FriendsDbHelper dbHelper;

    String[] namesArr;
    String[] idsArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        lvFriends = (ListView) findViewById(R.id.lvFriends);
        lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("INDEX", idsArr[position]);

                Intent intent = new Intent(FriendsListActivity.this, FriendDetailsActivity.class);

                intent.putExtra(FRIENDS_INDEX, idsArr[position]);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbHelper = new FriendsDbHelper(this);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                FriendsDbHelper.FriendEntry._ID,
                FriendsDbHelper.FriendEntry.COL_FIRST_NAME,
                FriendsDbHelper.FriendEntry.COL_LAST_NAME
        };

        Cursor cursor = db.query(FriendsDbHelper.FriendEntry.TABLE_NAME, projection, null, null, null, null, null);

        namesArr = new String [cursor.getCount()];
        idsArr = new String[cursor.getCount()];

        for(int i = 0; cursor.moveToNext(); i++) {
            namesArr[i] = cursor.getString( cursor.getColumnIndexOrThrow(FriendsDbHelper.FriendEntry.COL_FIRST_NAME) ) + " " +
                            cursor.getString( cursor.getColumnIndexOrThrow(FriendsDbHelper.FriendEntry.COL_LAST_NAME) );

            idsArr[i] = cursor.getString( cursor.getColumnIndexOrThrow(FriendsDbHelper.FriendEntry._ID) );
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, namesArr);
        lvFriends.setAdapter(adapter);
    }


    @Override
    protected void onPause() {
        dbHelper.close();
        super.onPause();
    }

    public void addNewFriend(View view) {
        Intent intent = new Intent(FriendsListActivity.this, FriendDetailsActivity.class);
        this.startActivity(intent);
    }
}

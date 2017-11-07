package example.com.pos;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class FriendDetailsActivity extends AppCompatActivity {

    EditText etFirstName;
    EditText etLastName;
    EditText etAge;
    EditText etAddress;

    Button saveButton;
    Button deleteButton;

    RadioButton maleRdButton;
    RadioButton femaleRdButton;

    String gender;
    String id;
    boolean updateChanges = false;

    FriendsDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_details);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etAge = (EditText) findViewById(R.id.etAge);
        etAddress = (EditText) findViewById(R.id.etAddress);

        saveButton = (Button) findViewById(R.id.friendSaveButton);
        deleteButton = (Button) findViewById(R.id.friendDeleteButton);

        maleRdButton = (RadioButton) findViewById(R.id.rbMale);
        femaleRdButton = (RadioButton) findViewById(R.id.rbFemale);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbHelper = new FriendsDbHelper(this);

        Bundle extras = this.getIntent().getExtras();
        if(extras != null) {
            fillFriendsForm(extras.getString(FriendsListActivity.FRIENDS_INDEX));
            id = extras.getString(FriendsListActivity.FRIENDS_INDEX);

            saveButton.setText("SAVE CHANGES");
            updateChanges = true;

            deleteButton.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onPause() {
        dbHelper.close();
        super.onPause();
    }

    public void saveFriend(View view) {
//        Log.d("FRIEND", etFirstName.getText() + "\n"
//                    + etLastName.getText() + "\n"
//                    + etAge.getText() + "\n"
//                    + gender + "\n"
//                    + etAddress.getText() + "\n");

        if(!formValidate()) {
            Toast.makeText(FriendDetailsActivity.this, "PLEASE FILLOUT ALL THE FIELDS!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FriendsDbHelper.FriendEntry.COL_FIRST_NAME, etFirstName.getText().toString());
        values.put(FriendsDbHelper.FriendEntry.COL_LAST_NAME, etLastName.getText().toString());
        values.put(FriendsDbHelper.FriendEntry.COL_AGE, etAge.getText().toString());
        values.put(FriendsDbHelper.FriendEntry.COL_GENDER, gender);
        values.put(FriendsDbHelper.FriendEntry.COL_ADDR, etAddress.getText().toString());


        if(updateChanges) {
            String selection = FriendsDbHelper.FriendEntry._ID + " = ?";
            String[] selectionArgs = { id };

            db.update(FriendsDbHelper.FriendEntry.TABLE_NAME, values, selection, selectionArgs);
        }else {
            id = db.insert(FriendsDbHelper.FriendEntry.TABLE_NAME, null, values) + "";
        }

        Log.d("FRIEND", "SAVED!");
        Toast.makeText(FriendDetailsActivity.this, "SAVED!", Toast.LENGTH_SHORT).show();
    }

    public void onRadioButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.rbMale:
                gender = "MALE";

                break;

            case R.id.rbFemale:
                gender = "FEMALE";

                break;
        }
    }

    private void fillFriendsForm(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                FriendsDbHelper.FriendEntry._ID,
                FriendsDbHelper.FriendEntry.COL_FIRST_NAME,
                FriendsDbHelper.FriendEntry.COL_LAST_NAME,
                FriendsDbHelper.FriendEntry.COL_GENDER,
                FriendsDbHelper.FriendEntry.COL_AGE,
                FriendsDbHelper.FriendEntry.COL_ADDR
        };

        String selection = FriendsDbHelper.FriendEntry._ID + " = ?";
        String[] selectionArgs = { id };

        Cursor cursor = db.query(FriendsDbHelper.FriendEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            etFirstName.setText(cursor.getString(cursor.getColumnIndexOrThrow(FriendsDbHelper.FriendEntry.COL_FIRST_NAME)));
            etLastName.setText(cursor.getString(cursor.getColumnIndexOrThrow(FriendsDbHelper.FriendEntry.COL_LAST_NAME)));
            etAge.setText(cursor.getString(cursor.getColumnIndexOrThrow(FriendsDbHelper.FriendEntry.COL_AGE)));
            etAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow(FriendsDbHelper.FriendEntry.COL_ADDR)));

            if(cursor.getString(cursor.getColumnIndexOrThrow(FriendsDbHelper.FriendEntry.COL_GENDER)).equals("MALE")) {
                maleRdButton.setChecked(true);
                gender = "MALE";
            }
            else {
                femaleRdButton.setChecked(true);
                gender = "FEMALE";
            }
        }
    }

    public void deleteFriend(View view) {
        new AlertDialog.Builder(this)
                .setTitle("DELETE?")
                .setMessage("ARE YOU SURE?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selection = FriendsDbHelper.FriendEntry._ID + " = ?";
                        String[] selectionArgs = { id };

                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.delete(FriendsDbHelper.FriendEntry.TABLE_NAME, selection, selectionArgs);

                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private boolean formValidate() {
        if(etFirstName.getText().length() != 0 &&
                etLastName.getText().length() != 0 &&
                etAge.getText().length() != 0 &&
                etAddress.getText().length() != 0 &&
                (maleRdButton.isChecked() || femaleRdButton.isChecked())) {
            return true;
        }

        return false;
    }
}

package example.com.pos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startFriendsActivity(View view) {
        Intent intent = new Intent(MainActivity.this, FriendsListActivity.class);
        this.startActivity(intent);
    }

    public  void startEventsActivity(View view) {
        Intent intent = new Intent(MainActivity.this, EventsListActivity.class);
        this.startActivity(intent);
    }
}

package de.albert.bihler.andrvoc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class CreateUserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_create_user);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.create_user, menu);
	return true;
    }

    public void doCreateUser(View view) {
	TextView username = (TextView) findViewById(R.id.createUser_Username);
	// TODO: Auf Null prüfen
	// TODO: Prüfen ob Benutzer bereits in der DB ist.
	DBHelper db = new DBHelper(getApplicationContext());
	db.getWritableDatabase();// this line responsible to
	db.insertUser(username.getText().toString());
	db.closeDB();
	this.finish();
    }

}

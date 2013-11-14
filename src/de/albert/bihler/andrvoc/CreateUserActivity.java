package de.albert.bihler.andrvoc;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CreateUserActivity extends Activity {

    private AppPreferences appPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        appPrefs = new AppPreferences(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_user, menu);
        return true;
    }

    public void doCreateUser(View view) {
        TextView username = (TextView) findViewById(R.id.createUser_Username);

        // Leeren Benutzernamen abfangen
        if (username.getText().toString().length() == 0) {
            Toast.makeText(this, R.string.Message_Username_empty, Toast.LENGTH_SHORT).show();
        } else {
            DBHelper db = new DBHelper(getApplicationContext());
            db.getWritableDatabase();
            List<String> userliste = db.getAllUsers();
            // Prüfen ob Benutzer bereits in der DB ist
            if (userliste.contains(username.getText().toString())) {
                Toast.makeText(this, R.string.Message_Username_exists, Toast.LENGTH_SHORT).show();
            } else {
                db.insertUser(username.getText().toString());
                db.closeDB();
                appPrefs.saveUser(username.getText().toString());
                this.finish();
            }
        }
    }
}

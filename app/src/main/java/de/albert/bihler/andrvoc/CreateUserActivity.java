package de.albert.bihler.andrvoc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import de.albert.bihler.andrvoc.db.DataSource;
import de.albert.bihler.andrvoc.model.User;
import de.albert.bihler.andrvoc.orangeiron.R;

public class CreateUserActivity extends Activity {

    private DataSource db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Open database
        if (db == null) {
            db = new DataSource(getApplicationContext());
        }
        db.open();
    }

    public void doCreateUser(View view) {
        TextView username = (TextView) findViewById(R.id.createUser_Username);
        String newUsername = username.getText().toString().trim();

        // check if username is empty
        if (newUsername.length() == 0) {
            Toast.makeText(this, R.string.Message_Username_empty, Toast.LENGTH_SHORT).show();
        } else {
            // check if user already exists in the database
            User existentUser = db.getUserByName(newUsername);
            if (existentUser != null) {
                Toast.makeText(this, R.string.Message_Username_exists, Toast.LENGTH_SHORT).show();
            } else {
                User user = new User();
                user.setName(newUsername);
                db.saveUser(user);
                finish();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Close the db connection
        db.close();
    }
}

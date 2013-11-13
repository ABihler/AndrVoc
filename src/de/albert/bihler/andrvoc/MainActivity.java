package de.albert.bihler.andrvoc;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

    public final static String EXTRA_MESSAGE = "de.albert.bihler.MESSAGE";
    private AppPreferences appPrefs;
    private Spinner unitSpinner;
    private Spinner userSpinner;
    private TextView textLog;
    private TextView textTop;
    private final boolean logActive = true;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);

	return true;
    }

    @Override
    protected void onResume() {
	super.onResume();
	init();
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
    }

    /** Called when the user clicks the Neu button */
    public void neuButton(View view) {
	// Intent intent = new Intent(this, DisplayMessageActivity.class);
	// //EditText editText = (EditText) findViewById(R.id.edit_message);
	// Spinner planet = (Spinner)findViewById(R.id.planets_spinner);
	// // planet.toString();
	// //.toString();
	// String message = "Hardcoded Text " +
	// planet.getSelectedItem().toString();
	// intent.putExtra(EXTRA_MESSAGE, message);
	// startActivity(intent);
    }

    /** Called when the user clicks the start question button */
    public void startQuestion(View view) {

	String unit = unitSpinner.getSelectedItem().toString();
	appPrefs.saveUnit(unit);

	Intent intent = new Intent(this, QuestionActivity.class);
	startActivity(intent);
    }

    /** Called when the user clicks the create user button */
    public void createUser(View view) {
	Intent intent = new Intent(this, CreateUserActivity.class);
	startActivity(intent);
    }

    // Zeugs initialisieren.

    public void init() {

	unitSpinner = (Spinner) findViewById(R.id.main_spinner_unit);
	userSpinner = (Spinner) findViewById(R.id.main_spinner_user);
	textLog = (TextView) findViewById(R.id.main_field_log);
	textTop = (TextView) findViewById(R.id.main_field_top);
	log("initialisieren");

	// TODO: Das Array aus der DB lesen.
	String array_spinner[] = new String[] { "test", "benny_01", "benny_02", "benny_03", "en_unit00_01", "en_unit01_01", "en_unit01_02" };
	ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this, R.layout.spinner_list, array_spinner);
	adapter.setDropDownViewResource(R.layout.spinner);
	unitSpinner.setAdapter(adapter);

	db = new DBHelper(getApplicationContext());
	db.getWritableDatabase();
	List<String> users = db.getAllUsers();
	db.closeDB();
	// Wenn es keinen User in der DB gibt, dann muss man einen anlegen.
	if (0 == users.size()) {
	    Intent intent = new Intent(this, CreateUserActivity.class);
	    startActivity(intent);
	}

	appPrefs = new AppPreferences(getApplicationContext());
	// appPrefs.saveUser("Erik");
	log("User: " + appPrefs.getUser());

	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	userSpinner.setAdapter(dataAdapter);
	userSpinner.setSelection(dataAdapter.getPosition(appPrefs.getUser()));

	setTopLine();
    }

    private void log(String s) {
	if (logActive) {
	    textLog.append("\n" + s);
	}
    }

    // Setzt aktuelle TopLine
    private void setTopLine() {
	textTop.setText("  Benutzer: " + appPrefs.getUser());
    }
}

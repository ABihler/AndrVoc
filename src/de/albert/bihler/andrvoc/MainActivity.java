package de.albert.bihler.andrvoc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends Activity {

    public final static String EXTRA_MESSAGE = "de.albert.bihler.MESSAGE";
    private AppPreferences appPrefs;
    private Spinner unitSpinner;

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

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
    }

    /** Called when the user clicks the Neu button */
    public void neuButton(View view) {
    }

    /** Called when the user clicks the start question button */
    public void startQuestion(View view) {

	String unit = unitSpinner.getSelectedItem().toString();
	appPrefs.saveUnit(unit);

	Intent intent = new Intent(this, QuestionActivity.class);
	startActivity(intent);
    }

    // Zeugs initialisieren.
    public void init() {

	unitSpinner = (Spinner) findViewById(R.id.main_spinner_unit);

	String array_spinner[] = new String[] { "benny_01", "en_unit00_01", "en_unit01_01", "en_unit01_02" };
	ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this, R.layout.spinner_list, array_spinner);
	adapter.setDropDownViewResource(R.layout.spinner);

	unitSpinner.setAdapter(adapter);

	appPrefs = new AppPreferences(getApplicationContext());
    }

}

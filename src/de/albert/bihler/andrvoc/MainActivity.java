package de.albert.bihler.andrvoc;

import de.albert.bihler.andrvoc.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "de.albert.bihler.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Vokabel v= new Vokabel();
 
        
     Spinner spinner = (Spinner) findViewById(R.id.planets_spinner);
     // Create an ArrayAdapter using the string array and a default spinner layout
     ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
    R.array.planets_array, android.R.layout.simple_spinner_item);
    // adapter.add(new String("Test"));
     //("dynamisch");
     // Specify the layout to use when the list of choices appears
     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     // Apply the adapter to the spinner
     spinner.setAdapter(adapter);
        
     Spinner s2 = (Spinner) findViewById(R.id.dates);
     
     
     ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.date_array, android.R.layout.simple_spinner_item);
     
     //adapter2.add(new String("August"));
     
     adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     s2.setAdapter(adapter2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    
}

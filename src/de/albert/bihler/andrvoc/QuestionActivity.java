package de.albert.bihler.andrvoc;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;

public class QuestionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		
		TextView textWord = (TextView) findViewById(R.id.question_field_word);
        textWord.setText("morgens");
        
        //TODO:answerSpinner über Klassen property ansprechen
        Spinner answerSpinner = (Spinner) findViewById(R.id.question_spinner_answer);
        
        String array_spinner[]=new String[5];
        array_spinner[0]="at the morning";
        array_spinner[1]="in the morning";
        array_spinner[2]="morning";
        array_spinner[3]="during the morning";
        array_spinner[4]="it's morning";
        
        ArrayAdapter adapter = new ArrayAdapter(this,
        		R.layout.spinner_list, array_spinner);
        
        adapter.setDropDownViewResource(R.layout.spinner);
        answerSpinner.setAdapter(adapter);        
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
		return true;
	}

	// Check answer
	public void doCheck(View view) {
		Spinner answerSpinner = (Spinner) findViewById(R.id.question_spinner_answer);
		String answer = answerSpinner.getSelectedItem().toString();
		//TODO:result Text über Klassen property ansprechen
	    if (answer.equals("in the morning")) {
	    	TextView textResult = (TextView) findViewById(R.id.question_field_result);
	    	textResult.setTextColor(Color.GREEN);
	        textResult.setText("Richtig!!!");
	    }
	    else {
	    	TextView textResult = (TextView) findViewById(R.id.question_field_result);
	    	textResult.setTextColor(Color.RED);
	        textResult.setText("Die Antwort ist leider falsch.");
	    }
	    	
	}
	
}

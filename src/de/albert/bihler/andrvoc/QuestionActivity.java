package de.albert.bihler.andrvoc;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;

public class QuestionActivity extends Activity {
//	private Spinner spinner findViewById(R.id.question_spinner_answer);
	private TextView textWord;
	private TextView textResult;
	private Spinner answerSpinner;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);

		textWord = (TextView) findViewById(R.id.question_field_word);
        textWord.setText("morgens");
        textResult = (TextView) findViewById(R.id.question_field_result);
        textResult.setText("");
        
        //      spinner.setOnItemSelectedListener(new OnItemSelectedListener();
        answerSpinner = (Spinner) findViewById(R.id.question_spinner_answer);
                
        
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
        
        //answerSpinner.setOnItemSelectedListener(new OnItemSelectedListener());
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
		if (answer.equals("in the morning")) {
	    	textResult = (TextView) findViewById(R.id.question_field_result);
	    	textResult.setTextColor(Color.GREEN);
	        textResult.setText("Richtig!!!");
	    }
	    else {
	    	textResult = (TextView) findViewById(R.id.question_field_result);
	    	textResult.setTextColor(Color.RED);
	        textResult.setText("Die Antwort ist leider falsch.");
	    }
	}
	
	// Nächste Frage
	public void doNext(View view) {
		textWord.setText("Entschuldigen Sie bitte.");
		clearResult();
		
		String array_spinner[]=new String[4];
        array_spinner[0]="sorry";
        array_spinner[1]="I'm sorry";
        array_spinner[2]="excuse me";
        array_spinner[3]="excuse please";
//        array_spinner[4]="it's morning";
        ArrayAdapter adapter = new ArrayAdapter(this,
        		R.layout.spinner_list, array_spinner);
        
        adapter.setDropDownViewResource(R.layout.spinner);
                
        answerSpinner.setAdapter(adapter);
        
	}
	
	   private void clearResult()
	   {
		   textResult.setText("");
	   }
	
//   public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//	        // your code here
//	    }

//    @Override
//    public void onNothingSelected(AdapterView<?> parentView) {
//	        // your code here
//	    }
	
	
}

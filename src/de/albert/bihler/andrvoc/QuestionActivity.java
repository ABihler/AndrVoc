package de.albert.bihler.andrvoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;

public class QuestionActivity extends Activity {
//	private Spinner spinner findViewById(R.id.question_spinner_answer);
	private TextView textWord;
	private TextView textResult;
	private TextView textStatus;
	private Spinner answerSpinner;
	//TODO:String List brauchen wir nicht wirklich
	private List<String> stringList;
	private List<Vokabel> vocList;
	private int numTest = 0;
	private int actTest = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);

		setStatus("Status: unbekannt");
		loadVocabulary();
		
		if (numTest > 0)
		{
			populateFields(actTest);
		}

		
//        
//         // //answerSpinner.setOnItemSelectedListener(new OnItemSelectedListener());
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
		if (answer.equals(vocList.get(actTest).name)) {
	    	textResult = (TextView) findViewById(R.id.question_field_result);
	    	textResult.setTextColor(Color.GREEN);
	        textResult.setText("Die Antwort ist richtig!!!");
	    }
	    else {
	    	textResult = (TextView) findViewById(R.id.question_field_result);
	    	textResult.setTextColor(Color.RED);
	        textResult.setText("Die Antwort ist leider falsch.");
	    }
	}
	
	// Nächste Frage
	public void doNext(View view) {
		
		//TODO ist hier noch nicht sauber. Steigt aus, wenn Ende der Liste erreicht ist.
		if (actTest <= numTest -1)
		{
			clearResult();
			setStatus("Fragen:" + actTest);
			actTest++;
			populateFields(actTest);
		}
		else
		{
			setStatus("Ende der Lektion erreicht.");
		}
	}
	
	// Füllt Felder mit Daten aus Vokabel Objekt
	private void populateFields(int index)
	   {
		textWord = (TextView) findViewById(R.id.question_field_word);
        textWord.setText(vocList.get(index).translation);
        textResult = (TextView) findViewById(R.id.question_field_result);
        textResult.setText("");
//        
//        //      //spinner.setOnItemSelectedListener(new OnItemSelectedListener();
        answerSpinner = (Spinner) findViewById(R.id.question_spinner_answer);
//
        int max = vocList.get(index).altList.size();
        	String array_spinner[]=new String[max];
        	
        	try{
	        	for (int i = 0; i <= max -1 ; i++ )
	        	{
	        		array_spinner[i]=vocList.get(index).altList.get(i);			
	        	}
        	}
	    		catch (Exception e)
	    		{
	    			exceptionOutput("Exception: " + e.toString());
	    		}
	        	
        
        ArrayAdapter adapter = new ArrayAdapter(this,
        		R.layout.spinner_list, array_spinner);
        
        adapter.setDropDownViewResource(R.layout.spinner);
                
        answerSpinner.setAdapter(adapter);

	   }
	
	   private void clearResult()
	   {
		   textResult.setText("");
	   }
	   
	   // Setzt Statusfeld
	   private void setStatus(String message)
	   {
		   textStatus = (TextView) findViewById(R.id.question_field_status);
		   textStatus.setText(message);		   
	   }
	   
private void exceptionOutput(String s)
{
	textStatus.setText(s);
}
private void loadVocabulary()
{
	try
	{
	stringList= Arrays.asList("milc#milch#mulk".split("#"));
	Vokabel v = new Vokabel ("milk", "Milch", stringList);
	
	vocList = new ArrayList<Vokabel>(Arrays.asList(v));
	stringList= Arrays.asList("toogeter#togeter#toogether".split("#"));
	Vokabel v2 = new Vokabel ("together", "zusammen", stringList);
	vocList.add(v2);
	
	stringList = new ArrayList<String>(Arrays.asList("ihr, ihre,#her#his#she#he#its".split("#")));
	setStatus(stringList.get(0));
	Vokabel v3 = new Vokabel (stringList );
	vocList.add(v3);
	
	
	Vokabel v4 = new Vokabel (new ArrayList<String>(Arrays.asList("unser, unsere#our#hers#his#us#we".split("#"))));
	vocList.add(v4);
	vocList.add(new Vokabel (new ArrayList<String>(Arrays.asList("Ich bin dran.#It's my turn.#It's me#his#us#we".split("#")))));
//	
	numTest = vocList.size();

	}
	catch (Exception e)
	    		{
	    			exceptionOutput("Load Exception: " + e.toString());
	    		}
}

//   public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//	        // your code here
//	    }

//    @Override
//    public void onNothingSelected(AdapterView<?> parentView) {
//	        // your code here
//	    }
	
	/*
	I'm sorry	Enschuldigung, tut mir leid
	together	zusammen
	her	ihr, ihre
	our	uneser, unsere
	It's my turn.	Ich bin dran.
	wrong	falsch, verkehrt
	Sit with me	Setz dich. Setzt euch zu mir.
	come	komm, kommt
	Don't listen to Dan	Hör nich auf Dan.
	mad	verrückt
	first	zuerst, als erster
	nervous	nervös, aufgeregt
	student	Schüler, Student
	lesson	(Unterrichts-)Stunde
	before	vor (zeitlich)
	mobile phone	Handy, Mobiltetefon
	milk	Milch
	box	Kiste, Kasten, Kästchen
	word	Wort
	in the morning	am morgen, mogrens
	marmalada	Orangenmarmalade
	there are	es sind (vorhanden), es gibt
	there's	es ist (vorhanden), eg gibt
	comic	Comic(-heft)
	lots fo	viele, eine Menge, viel
	You're welcome	Gern geschehen. Nichts zu danken
	back to Germany	zurück nach Deutschland
	trip	Reise, Ausflug
	excuse me	Entschuldigung, entschuldigen sie,
	Good luck (with)	Viel Glück (bei/mit)
	at work	bei der Arbeit, am Arbeitsplatz
	wheelchair	Rollstuhl
	They welcome you to…	Sie heißen dich in … willkommen
	parrot	Papagei
	
	*/

}

package de.albert.bihler.andrvoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.xmlpull.v1.XmlPullParser;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;

public class QuestionActivity extends Activity {
	private TextView textWord;
	private TextView textResult;
	private TextView textStatus;
	private TextView textLog;
	private Spinner answerSpinner;
	private List<Vokabel> vocList;
	private int numTest = 0;
	private int actTest = 0;
	private int numRightAnswers = 0;
	private int numWrongAnswers = 0;
	private Button button;
	private String status ="new";
	private boolean logActive = true;
	private AppPreferences appPrefs;
	private int currentUnitID;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		
    	appPrefs = new AppPreferences(getApplicationContext());
    	//String someString = appPrefs.getUnit();
    	currentUnitID = appPrefs.getUnitID(getApplicationContext());
		
		button = (Button) findViewById(R.id.question_button_main);
		answerSpinner = (Spinner) findViewById(R.id.question_spinner_answer);
		textLog = (TextView) findViewById(R.id.question_field_log);
		textLog.setMovementMethod(new ScrollingMovementMethod());
		
		textStatus = (TextView) findViewById(R.id.question_field_status);
		
		log("onCreate");

		setStatusLine("Status: unbekannt");
		loadVocabulary();
		randomizeList();

		if (numTest > 0)
		{
			setStatusCheck();
			populateFields(actTest);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
		return true;
	}

	// Check answer
	public void doCheck(View view) {
		String answer = answerSpinner.getSelectedItem().toString();
		if (answer.equals(vocList.get(actTest).name)) {
	    	textResult = (TextView) findViewById(R.id.question_field_result);
	    	textResult.setTextColor(Color.rgb(50, 205, 50));
	        textResult.setText("Die Antwort ist richtig!!!");
	        numRightAnswers++;
	        setStatusNext();
	        
	        setStatusLine(getStasiticString());
	        // Ende der Lektion
	        if (actTest == (numTest -1)){
	        	setStatusLine(getStasiticString() + "\nEnde der Lektion erreicht.");
	        	button.setEnabled(false);
	        	//TODO:Statistikausgabe, Button evtl. auf zurück ummappen.
	        }
	    }
	    else {
	    	textResult = (TextView) findViewById(R.id.question_field_result);
	    	textResult.setTextColor(Color.RED);
	        textResult.setText("Die Antwort ist leider falsch.");
	        numWrongAnswers++;
	        setStatusCheck();
	        setStatusLine(getStasiticString());
	    }
	}
	
	public void doMain(View view)
	{
		if ("Next".equals(status))
		{
			doNext(view);
		}
		else if ("Check".equals(status))
		{
			doCheck(view);
		}
		else
		{
			setStatusLine("unknown status: " + status);
		}
	}
	
	// Nächste Frage
	public void doNext(View view) {
		
		if (actTest <= (numTest -2))
		{
			clearResult();
			actTest++;
			populateFields(actTest);
			setStatusCheck();
		}
		else
		{
			// hierher sollten wir aber nie gelangen, weil in doCheck bereits auf das Ende der Lektion geprüft wird.
			setStatusLine(getStasiticString() + "\nEnde");
		}
	}
	
	// Füllt Felder mit Daten aus Vokabel Objekt
	private void populateFields(int index)
	   {
		setStatusLine(getStasiticString());
		
		textWord = (TextView) findViewById(R.id.question_field_word);
        textWord.setText(vocList.get(index).translation);
        textResult = (TextView) findViewById(R.id.question_field_result);
        textResult.setText("");

        int max = vocList.get(index).altList.size();
        	String array_spinner[]=new String[max];
        	
        	try{
        		
        		vocList.get(index).randomizeList();
	        	for (int i = 0; i <= max -1 ; i++ )
	        	{
	        		array_spinner[i]=vocList.get(index).altList.get(i);			
	        	}
        	}
	    		catch (Exception e)
	    		{
	    			exceptionOutput("Exception: " + e.toString());
	    		}
        	
            ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this,
            		R.layout.spinner_list, array_spinner);
                    adapter.setDropDownViewResource(R.layout.spinner);
                    
            answerSpinner.setAdapter(adapter);
        	
	   }
	
	// Schüttelt die Liste der Vokabeln durcheinander
	public void randomizeList()
	{
		Random rand = new Random();
	    for(int i = 1; i<= 100; i++)
	    {
	        int r1 = rand.nextInt(vocList.size());
	        int r2 = rand.nextInt(vocList.size());
	        Vokabel v1 = vocList.get(r1);
	        Vokabel v2 = vocList.get(r2);
	        vocList.set(r1, v2);
	        vocList.set(r2, v1);
	    }
	}
	
	   private void clearResult()
	   {
		   textResult.setText("");
	   }
	   
	   // Setzt Statusfeld
	   private void setStatusLine(String message)
	   {
		   textStatus = (TextView) findViewById(R.id.question_field_status);
		   textStatus.setText(message);		   
	   }
	   
private void exceptionOutput(String s)
{
	textStatus.setText(s);
}

private String getStasiticString()
{
	String stat = "Statistik:" + numRightAnswers + " richtig und " + numWrongAnswers + " falsch.";
	return stat;
}

private void setStatusCheck()
{
	status="Check";
	answerSpinner.setEnabled(true);
	button.setText(R.string.question_button_check);
}

private void setStatusNext()
{
	status="Next";
	answerSpinner.setEnabled(false);
	button.setText(R.string.question_button_next);
}

private void log(String s)
{
	if(logActive){
		textLog.append("\n" + s);
	}
}
private void loadVocabulary()
{
	log("loadVocabulary");
	
	try
	{
		Resources res = this.getResources();
		//XmlResourceParser xrp = res.getXml(R.xml.en_unit01_02);
		XmlResourceParser xrp = res.getXml(currentUnitID);

		int eventType = xrp.getEventType();
		log("CurrentUnit:" + appPrefs.getUnit());
		log("CurrentUnitId:" + currentUnitID);
		
		
		String tag = "";
		   while (eventType != XmlPullParser.END_DOCUMENT)
		   {
//			   if(eventType == XmlPullParser.START_DOCUMENT)
//			    {
//			     log("--- Start XML ---");
//			    }
			    if(eventType == XmlPullParser.START_TAG)
			    {
//			     log("\nSTART_TAG: "+xrp.getName());
			     tag = xrp.getName();
			    }
//			    else if(eventType == XmlPullParser.END_TAG)
//			    {
//			     log("\nEND_TAG: "+xrp.getName());
//			    }
			    else if(eventType == XmlPullParser.TEXT)
			    {
			    //	log("\nTEXT: "+xrp.getText());
			    	String line = xrp.getText();
			    	if("VokLine".equalsIgnoreCase(tag))
			    	{
			    		if(vocList == null){
			    			vocList = new ArrayList<Vokabel>(Arrays.asList(new Vokabel(line)));
			    		}
			    		else{
			    			vocList.add(new Vokabel(line));
			    		}
			    	}
			    }
			   eventType = xrp.next();
		   }
			numTest = vocList.size();	
		}
		catch (Exception e){
			exceptionOutput("Load Exception: " + e.toString());
		}
	}
}

//   public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//	        // your code here
//	    }

//    @Override
//    public void onNothingSelected(AdapterView<?> parentView) {
//	        // your code here
//	    }

package de.albert.bihler.andrvoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import de.albert.bihler.andrvoc.model.Vokabel;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends Activity implements OnCheckedChangeListener {

    private TextView textWord;
    private TextView textResult;
    private TextView textStatus;
    private TextView textLog;
    private TextView textTop;
    // private Spinner answerSpinner;
    private RadioGroup containerGroup;
    private List<Vokabel> vocList;
    // private final int numTest = 0;
    private int actTest = 0;
    private int numRightAnswers = 0;
    private int numWrongAnswers = 0;
    private Button button;
    private String status = "new";
    private final boolean logActive = true;
    private AppPreferences appPrefs;
    private int currentUnitID;
    private String currentSelectedAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Hier muss auch noch ein orientation chek her.
        setContentView(R.layout.activity_question);

        init();
        log("onCreate");

        setStatusLine("Status: unbekannt");

        if (vocList.size() > 0) {
            setStatusCheck();
            populateFields(actTest);
        }
    }

    private void init() {
        appPrefs = new AppPreferences(getApplicationContext());
        currentUnitID = appPrefs.getUnitID(getApplicationContext());

        textTop = (TextView) findViewById(R.id.question_field_top);
        textLog = (TextView) findViewById(R.id.question_field_log);
        textLog.setMovementMethod(new ScrollingMovementMethod());
        textStatus = (TextView) findViewById(R.id.question_field_status);

        button = (Button) findViewById(R.id.question_button_main);
        button.setEnabled(false);
        // answerSpinner = (Spinner) findViewById(R.id.question_spinner_answer);

        containerGroup = (RadioGroup) findViewById(R.id.answerContainer);
        containerGroup.setOnCheckedChangeListener(this);

        textLog.setMovementMethod(new ScrollingMovementMethod());
        textStatus = (TextView) findViewById(R.id.question_field_status);

        log("onCreate");

        setStatusLine("Status: unbekannt");

        // Vokabeln der aktuellen Lektion laden
        this.vocList = loadVocabulary(currentUnitID);

        // Abfragereihenfolge der Vokabeln mischen
        Collections.shuffle(this.vocList);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_question_land);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_question);
        }
        init();
        setStatusCheck();
        populateFields(actTest);
    }

    // Check answer
    public void doCheck(View view) {
        if (currentSelectedAnswer.equals(vocList.get(actTest).getCorrectTranslation())) {
            textResult = (TextView) findViewById(R.id.question_field_result);
            textResult.setTextColor(Color.rgb(50, 205, 50));
            textResult.setText("Die Antwort ist richtig!!!");
            numRightAnswers++;
            setStatusNext();

            setStatusLine(getStasiticString());
            // Ende der Lektion
            if (actTest == (vocList.size() - 1)) {
                setStatusLine(getStasiticString() + "\nEnde der Lektion erreicht.");
                button.setEnabled(false);
                // TODO:Statistikausgabe, Button evtl. auf zurück ummappen.
            }
        } else {
            textResult = (TextView) findViewById(R.id.question_field_result);
            textResult.setTextColor(Color.RED);
            textResult.setText("Die Antwort ist leider falsch.");
            numWrongAnswers++;
            setStatusCheck();
            setStatusLine(getStasiticString());
        }
    }

    public void doMain(View view) {
        if ("Next".equals(status)) {
            doNext(view);
        } else if ("Check".equals(status)) {
            doCheck(view);
        } else {
            setStatusLine("unknown status: " + status);
        }
    }

    // Nächste Frage
    public void doNext(View view) {

        if (actTest <= (vocList.size() - 2)) {
            clearResult();
            button.setEnabled(false);
            actTest++;
            populateFields(actTest);
            setStatusCheck();
        } else {
            // hierher sollten wir aber nie gelangen, weil in doCheck bereits
            // auf das Ende der Lektion gepr�ft wird.
            setStatusLine(getStasiticString() + "\nEnde");
        }
    }

    // Füllt Felder mit Daten aus Vokabel Objekt
    private void populateFields(int index) {
        Vokabel vokabel = vocList.get(index);
        setStatusLine(getStasiticString());
        setTopLine();

        textWord = (TextView) findViewById(R.id.question_field_word);
        textWord.setText(vocList.get(index).getOriginalWord());
        textResult = (TextView) findViewById(R.id.question_field_result);
        textResult.setText("");

        int max = vokabel.getAlternativeTranslations().size();

        // Radiobuttons der letzten Vokabel löschen
        containerGroup.removeAllViews();

        for (int i = 0; i <= max - 1; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(vokabel.getAlternativeTranslations().get(i));
            radioButton.setTextSize(20);
            radioButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            containerGroup.addView(radioButton);
        }
    }

    private void clearResult() {
        textResult.setText("");
    }

    // Setzt Statusfeld
    private void setStatusLine(String message) {
        // textStatus = (TextView) findViewById(R.id.question_field_status);
        textStatus.setText(message);
    }

    // Setzt aktuelle TopLine
    private void setTopLine() {
        textTop.setText("  Benutzer: " + appPrefs.getUser() + " " + (actTest + 1) + "/" + vocList.size());
    }

    private void exceptionOutput(String s) {
        textStatus.setText(s);
    }

    private String getStasiticString() {
        String stat = "Statistik:" + numRightAnswers + " richtig und " + numWrongAnswers + " falsch.";
        return stat;
    }

    private void setStatusCheck() {
        status = "Check";
        containerGroup.setEnabled(true);
        button.setText(R.string.question_button_check);
    }

    private void setStatusNext() {
        status = "Next";
        containerGroup.setEnabled(false);
        button.setText(R.string.question_button_next);
    }

    private void log(String s) {
        if (logActive) {
            textLog.append("\n" + s);
        }
    }

    private ArrayList<Vokabel> loadVocabulary(int currentUnitId) {
        log("loadVocabulary");
        ArrayList<Vokabel> vocList = new ArrayList<Vokabel>();

        try {
            Resources res = this.getResources();
            // XmlResourceParser xrp = res.getXml(R.xml.en_unit01_02);
            XmlResourceParser xrp = res.getXml(currentUnitId);

            int eventType = xrp.getEventType();
            log("CurrentUnit:" + appPrefs.getUnit());
            log("CurrentUnitId:" + currentUnitId);

            String tag = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                // if(eventType == XmlPullParser.START_DOCUMENT)
                // {
                // log("--- Start XML ---");
                // }
                if (eventType == XmlPullParser.START_TAG) {
                    // log("\nSTART_TAG: "+xrp.getName());
                    tag = xrp.getName();
                }
                // else if(eventType == XmlPullParser.END_TAG)
                // {
                // log("\nEND_TAG: "+xrp.getName());
                // }
                else if (eventType == XmlPullParser.TEXT) {
                    // log("\nTEXT: "+xrp.getText());
                    String line = xrp.getText();
                    if ("VokLine".equalsIgnoreCase(tag)) {
                        if (vocList == null) {
                            vocList = new ArrayList<Vokabel>(Arrays.asList(new Vokabel(line)));
                        } else {
                            vocList.add(new Vokabel(line));
                        }
                    }
                }
                eventType = xrp.next();
            }
        } catch (Exception e) {
            exceptionOutput("Load Exception: " + e.toString());
        }
        return vocList;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radioButton = (RadioButton) findViewById(checkedId);
        currentSelectedAnswer = (String) radioButton.getText();
        button.setEnabled(true);
    }
}

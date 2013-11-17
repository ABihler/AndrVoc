package de.albert.bihler.andrvoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import de.albert.bihler.andrvoc.db.LessonDataSource;
import de.albert.bihler.andrvoc.db.TrainingLogDataSource;
import de.albert.bihler.andrvoc.db.VocabularyDataSource;
import de.albert.bihler.andrvoc.model.Lesson;
import de.albert.bihler.andrvoc.model.Vokabel;

public class MainActivity extends Activity implements OnItemSelectedListener {

    public final static String EXTRA_MESSAGE = "de.albert.bihler.MESSAGE";
    private AppPreferences appPrefs;
    private Spinner unitSpinner;
    private Spinner userSpinner;
    private TextView textLog;
    private TextView textTop;
    private final boolean logActive = true;
    private DBHelper db;
    private ApplicationSingleton appSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appPrefs = new AppPreferences(getApplicationContext());
        appSingleton = ApplicationSingleton.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if ("no user defined".equals(appPrefs.getUser())) {
            // Es gibt noch keinen current User. D.h. wir müssen einen anlegen.
            db = new DBHelper(getApplicationContext());
            db.getWritableDatabase();
            List<String> users = db.getAllUsers();
            db.closeDB();
            // Wenn es keinen User in der DB gibt, dann muss man einen anlegen.
            if (0 == users.size()) {
                Intent intent = new Intent(this, CreateUserActivity.class);
                startActivity(intent);
            }
        }

        else if ("none".equals(appPrefs.getVocabularyServer())) {
            // Es wurde noch keine URL hinterlegt (erster Start der Anwendung)
            // Konfigurationsmaske anzeigen
            startActivity(new Intent(this, VocabularyServerConfig.class));
        }
        else {
            init();
            userSpinner.setOnItemSelectedListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    /** Called when the user clicks the start question button */
    public void startQuestion(View view) {
        Lesson lesson = (Lesson) unitSpinner.getSelectedItem();
        appPrefs.saveLesson(lesson.getId());

        // Vokabeln der aktuellen Lektion laden
        List<Vokabel> vocList = loadVocabulary(lesson.getId());
        // Abfragereihenfolge der Vokabeln mischen
        Collections.shuffle(vocList);
        appSingleton.setApplicationVocList(vocList);

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

        LessonDataSource lessonDataSource = new LessonDataSource(getApplicationContext());
        lessonDataSource.open();
        List<Lesson> lessons = lessonDataSource.getLessons();
        lessonDataSource.close();

        Lesson[] array_spinner = new Lesson[lessons.size()];
        lessons.toArray(array_spinner);

        ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_spinner_item, array_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(adapter);

        // Todo wäre es evtl. nich besse die DB offen zu halten und beim finisch zu schließen?
        db = new DBHelper(getApplicationContext());
        db.getWritableDatabase();
        List<String> users = db.getAllUsers();
        db.closeDB();

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
        TrainingLogDataSource trainingLogDataSource = new TrainingLogDataSource(getApplicationContext());
        trainingLogDataSource.open();
        long numLogs = trainingLogDataSource.getNumberOfLogsForUser(appPrefs.getUser());
        long numErrors = trainingLogDataSource.getNumberOfErrorLogsForUser(appPrefs.getUser());
        long numSuc = trainingLogDataSource.getNumberOfSuccessLogsForUser(appPrefs.getUser());
        trainingLogDataSource.close();
        textTop.setText("  Benutzer: " + appPrefs.getUser() + " (Total:" + numLogs + " richtig: " + numSuc + " falsch: " + numErrors + ")");
    }

    /**
     * Diese Methoden werden aufgerufen wenn Spinner ausgewählt werden.
     */
    @Override
    public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
        parentView.getItemAtPosition(position);

        switch (parentView.getId())
        {
            case R.id.main_spinner_user:
                appPrefs.saveUser(userSpinner.getItemAtPosition(position).toString());
                setTopLine();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parentView) {
        //
    }

    private List<Vokabel> loadVocabulary(long currentLesson) {
        log("loadVocabulary");
        List<Vokabel> vocList = new ArrayList<Vokabel>();

        VocabularyDataSource vocabularyDataSource = new VocabularyDataSource(getApplicationContext());
        vocabularyDataSource.open();
        vocList = vocabularyDataSource.getVocabulary(currentLesson);
        vocabularyDataSource.close();

        return vocList;
    }

    /** Called when the user clicks the start question button */
    public void startTest(View view) {
        // Schlechteste Vokabeln holen
        TrainingLogDataSource tds = new TrainingLogDataSource(getApplicationContext());
        tds.open();
        List<Vokabel> vocList = tds.getWorstForUser(appPrefs.getUser());
        // Abfragereihenfolge der Vokabeln mischen
        Collections.shuffle(vocList);
        appSingleton.setApplicationVocList(vocList);

        Intent intent = new Intent(this, QuestionActivity.class);
        startActivity(intent);
    }
}

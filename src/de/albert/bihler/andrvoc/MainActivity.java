package de.albert.bihler.andrvoc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import de.albert.bihler.andrvoc.db.LessonDataSource;
import de.albert.bihler.andrvoc.model.Lesson;

public class MainActivity extends Activity {

    public final static String EXTRA_MESSAGE = "de.albert.bihler.MESSAGE";
    private AppPreferences appPrefs;
    private Spinner unitSpinner;
    private TextView textLog;
    private final boolean logActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appPrefs = new AppPreferences(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if ("none".equals(appPrefs.getVocabularyServer())) {
            // Es wurde noch keine URL hinterlegt (erster Start der Anwendung)
            // Konfigurationsmaske anzeigen
            startActivity(new Intent(this, VocabularyServerConfig.class));
        } else {
            init();
        }

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

        Lesson lesson = (Lesson) unitSpinner.getSelectedItem();
        appPrefs.saveLesson(lesson.getId());

        Intent intent = new Intent(this, QuestionActivity.class);
        startActivity(intent);
    }

    // Zeugs initialisieren.

    public void init() {

        unitSpinner = (Spinner) findViewById(R.id.main_spinner_unit);
        textLog = (TextView) findViewById(R.id.main_field_log);
        log("initialisieren");

        LessonDataSource lessonDataSource = new LessonDataSource(getApplicationContext());
        lessonDataSource.open();
        List<Lesson> lessons = lessonDataSource.getLessons();
        lessonDataSource.close();

        Lesson[] array_spinner = new Lesson[lessons.size()];
        lessons.toArray(array_spinner);

        ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this, R.layout.spinner_list, array_spinner);
        adapter.setDropDownViewResource(R.layout.spinner);

        unitSpinner.setAdapter(adapter);

        appPrefs.saveUser("Erik");
        log("User: " + appPrefs.getUser());
    }

    private void log(String s) {
        if (logActive) {
            textLog.append("\n" + s);
        }
    }
}

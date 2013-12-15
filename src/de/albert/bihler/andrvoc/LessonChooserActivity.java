package de.albert.bihler.andrvoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import de.albert.bihler.andrvoc.adapter.LessonListViewAdapter;
import de.albert.bihler.andrvoc.db.DataSource;
import de.albert.bihler.andrvoc.db.TrainingLogDataSource;
import de.albert.bihler.andrvoc.db.VocabularyDataSource;
import de.albert.bihler.andrvoc.model.Lesson;
import de.albert.bihler.andrvoc.model.Vokabel;
import de.albert.bihler.andrvoc.orangeiron.R;

public class LessonChooserActivity extends Activity implements OnItemClickListener {

    private ListView listView;
    private DataSource db;
    private LessonListViewAdapter lessonAdapter;
    private AppPreferences appPrefs;
    private ApplicationSingleton appSingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_chooser);

        this.appPrefs = new AppPreferences(getApplicationContext());
        appSingleton = ApplicationSingleton.getInstance();

        this.listView = (ListView) findViewById(R.id.lessonList);
        this.listView.setOnItemClickListener(this);

        // set the view when the list is empty
        TextView emtpyView = (TextView) findViewById(R.id.empty_lesson_list);
        Typeface roboto_light = Typeface.createFromAsset(getAssets(), "fonts/Roboto/Roboto-Light.ttf");
        emtpyView.setTypeface(roboto_light);
        this.listView.setEmptyView(emtpyView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Open database
        if (db == null) {
            db = new DataSource(getApplicationContext());
        }
        db.open();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // get lessons, if any present
        List<Lesson> lessons = db.getAllLessons();

        if (lessons != null) {
            // set the adapter to the list view
            this.lessonAdapter = new LessonListViewAdapter(getApplicationContext(), lessons);
            this.listView.setAdapter(lessonAdapter);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lesson_chooser, menu);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
        // save current selected lesson in the preferences for other activities
        appPrefs.saveCurrentLesson(id);

        // Vokabeln der aktuellen Lektion laden
        List<Vokabel> vocList = loadVocabulary((int) id);
        // Abfragereihenfolge der Vokabeln mischen
        Collections.shuffle(vocList);

        appSingleton.setApplicationVocList(vocList);
        // navigate to the question activity
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra(Lesson.LESSON_MODE, Lesson.LESSON_MODE_NORMAL);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_manageServers:
                intent = new Intent(this, VocabularyServerConfigActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_weakestWords:
                intent = new Intent(this, QuestionActivity.class);
                intent.putExtra(Lesson.LESSON_MODE, Lesson.LESSON_MODE_WEAKEST_WORDS);
                startActivity(intent);
                return true;
            case R.id.action_oldestWords:
                intent = new Intent(this, QuestionActivity.class);
                intent.putExtra(Lesson.LESSON_MODE, Lesson.LESSON_MODE_OLDEST_WORDS);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<Vokabel> loadVocabulary(long currentLesson) {
        // log("loadVocabulary");
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

package de.robertmathes.android.orangeiron;

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
import de.robertmathes.android.orangeiron.adapter.LessonListViewAdapter;
import de.robertmathes.android.orangeiron.db.DataSource;
import de.robertmathes.android.orangeiron.model.Lesson;

public class LessonChooserActivity extends Activity implements OnItemClickListener {

    private ListView listView;
    private DataSource db;
    private LessonListViewAdapter lessonAdapter;
    private AppPreferences appPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_chooser);

        this.appPrefs = new AppPreferences(getApplicationContext());

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

}

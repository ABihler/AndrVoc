package de.albert.bihler.andrvoc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import de.albert.bihler.andrvoc.adapter.QuestionListViewAdapter;
import de.albert.bihler.andrvoc.db.DataSource;
import de.albert.bihler.andrvoc.model.Lesson;
import de.albert.bihler.andrvoc.model.User;
import de.albert.bihler.andrvoc.orangeiron.R;

public class QuestionActivity extends Activity implements OnItemClickListener {

    private AppPreferences appPrefs;
    private DataSource db;
    private User user;
    private Lesson lesson;
    private int currentWord;
    private TextView originalWord;
    private ListView translations;
    private TextView correctAnswers;
    private TextView badAnswers;
    private int correctAnswersCount = 0;
    private int badAnswersCount = 0;
    private QuestionListViewAdapter translationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_question_land);
        } else {
            setContentView(R.layout.activity_question);
        }

        appPrefs = new AppPreferences(getApplicationContext());

        originalWord = (TextView) findViewById(R.id.textView_lesson_originalWord);
        translations = (ListView) findViewById(R.id.listView_lesson__translations);
        translations.setOnItemClickListener(this);
        correctAnswers = (TextView) findViewById(R.id.textView_lesson_correctAnswers);
        correctAnswers.setText(correctAnswersCount + "");
        badAnswers = (TextView) findViewById(R.id.textView_lesson_wrong_answers);
        badAnswers.setText(badAnswersCount + "");

        currentWord = 0;
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

        Intent intent = getIntent();
        int lessonMode = intent.getIntExtra(Lesson.LESSON_MODE, Lesson.LESSON_MODE_NORMAL);

        // get the current user and lesson (based on the lesson mode)
        user = db.getUser(appPrefs.getCurrentUser());
        switch (lessonMode) {
            case Lesson.LESSON_MODE_NORMAL:
                lesson = db.getLessonById(appPrefs.getCurrentLesson());
                break;
            case Lesson.LESSON_MODE_WEAKEST_WORDS:
                lesson = db.getWeakestWordsByUser(user, 20);
                break;
            case Lesson.LESSON_MODE_OLDEST_WORDS:
                lesson = db.getOldestWordsByUser(user, 20);
                break;
            default:
                lesson = db.getLessonById(appPrefs.getCurrentLesson());
        }

        // set the list view adapter
        translationsAdapter = new QuestionListViewAdapter(getApplicationContext(), lesson.getVocabulary().get(0));
        translations.setAdapter(translationsAdapter);

        // set the title based on the current lesson
        setTitle(getString(R.string.title_lesson) + " " + lesson.getName());

        originalWord.setText(lesson.getVocabulary().get(currentWord).getOriginalWord());
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Close the db connection
        db.close();
    }

    private void updateCorrectAnswerCount() {
        correctAnswersCount++;
        db.updateCorrectAnswerCount(user.getId(), lesson.getVocabulary().get(currentWord).getLessonId(), lesson.getVocabulary().get(currentWord).getId());
        correctAnswers.setText(correctAnswersCount + "");
    }

    private void updateBadAnswerCount() {
        badAnswersCount++;
        db.updateBadAnswerCount(user.getId(), lesson.getVocabulary().get(currentWord).getLessonId(), lesson.getVocabulary().get(currentWord).getId());
        badAnswers.setText(badAnswersCount + "");
    }

    @SuppressLint("NewApi")
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (translationsAdapter.getItem(position).equals(lesson.getVocabulary().get(currentWord).getCorrectTranslation())) {
            updateCorrectAnswerCount();
            moveToNextWord();
        } else {
            updateBadAnswerCount();
        }
    }

    private void moveToNextWord() {
        currentWord++;
        if (currentWord < lesson.getVocabulary().size()) {
            originalWord.setText(lesson.getVocabulary().get(currentWord).getOriginalWord());
            translationsAdapter.setWord(lesson.getVocabulary().get(currentWord));
            translationsAdapter.notifyDataSetChanged();
        } else {
            finish();
        }
    }
}

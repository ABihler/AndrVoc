package de.robertmathes.android.orangeiron;

import java.util.Collections;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import de.robertmathes.android.orangeiron.adapter.QuestionListViewAdapter;
import de.robertmathes.android.orangeiron.db.DataSource;
import de.robertmathes.android.orangeiron.model.Lesson;
import de.robertmathes.android.orangeiron.model.User;
import de.robertmathes.android.orangeiron.model.Vokabel;

public class QuestionActivity extends Activity implements OnItemClickListener {

    private static final String TAG = "QuestionActivity";

    private AppPreferences appPrefs;
    private DataSource db;
    private User user;
    private Lesson lesson;
    private List<Vokabel> words;
    private int currentWord;
    private TextView originalWord;
    private ListView translations;
    private TextView correctAnswers;
    private TextView wrongAnswers;
    private int correctAnswersCount = 0;
    private int badAnswersCount = 0;
    private QuestionListViewAdapter translationsAdapter;
    private ObjectAnimator correctAnswersAnimation;
    private ObjectAnimator wrongAnswersAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_question_land);
        } else {
            setContentView(R.layout.activity_question);
        }

        appPrefs = new AppPreferences(getApplicationContext());

        originalWord = (TextView) findViewById(R.id.textView_lesson_originalWord);
        translations = (ListView) findViewById(R.id.listView_lesson_translations);
        translations.setOnItemClickListener(this);
        correctAnswers = (TextView) findViewById(R.id.textView_lesson_correctAnswers);
        correctAnswers.setText(correctAnswersCount + "");
        wrongAnswers = (TextView) findViewById(R.id.textView_lesson_wrong_answers);
        wrongAnswers.setText(badAnswersCount + "");

        currentWord = 0;

        // setup animations
        // Scale the button in X and Y. Note the use of PropertyValuesHolder to animate
        // multiple properties on the same object in parallel.
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.SCALE_X, 2);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 2);
        // PropertyValuesHolder pvhGreen = PropertyValuesHolder.ofInt("textColor", Color.GREEN);
        correctAnswersAnimation =
                ObjectAnimator.ofPropertyValuesHolder(correctAnswers, pvhX, pvhY/* , pvhGreen */);
        correctAnswersAnimation.setRepeatCount(1);
        correctAnswersAnimation.setRepeatMode(ValueAnimator.REVERSE);
        wrongAnswersAnimation = ObjectAnimator.ofPropertyValuesHolder(wrongAnswers, pvhX, pvhY);
        wrongAnswersAnimation.setRepeatCount(1);
        wrongAnswersAnimation.setRepeatMode(ValueAnimator.REVERSE);

        // Open database
        if (db == null) {
            db = new DataSource(getApplicationContext());
        }
        db.open();

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

        // Get the words and shuffle them
        words = lesson.getVocabulary();
        Collections.shuffle(words);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Reopen database when reentering the app
        if (db == null) {
            db = new DataSource(getApplicationContext());
        }
        db.open();

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();

        // set the list view adapter
        translationsAdapter = new QuestionListViewAdapter(getApplicationContext());
        translations.setAdapter(translationsAdapter);

        setWordToView(currentWord);

        // set the title based on the current lesson
        setTitle(getString(R.string.title_lesson) + " " + lesson.getName());
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();

        // Close the db connection
        db.close();
    }

    private void updateCorrectAnswerCount() {
        Log.d(TAG, "updateCorrectAnswerCount");
        correctAnswersCount++;
        db.updateCorrectAnswerCount(user.getId(), words.get(currentWord).getLessonId(), words.get(currentWord).getId());
        correctAnswers.setText(correctAnswersCount + "");
    }

    private void updateWrongAnswerCount() {
        Log.d(TAG, "updateWrongAnswerCount");
        badAnswersCount++;
        db.updateBadAnswerCount(user.getId(), words.get(currentWord).getLessonId(), words.get(currentWord).getId());
        wrongAnswers.setText(badAnswersCount + "");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, final View view, int position, long id) {
        Log.d(TAG, "onItemClick");
        if (translationsAdapter.getItem(position).equals(words.get(currentWord).getCorrectTranslation())) {
            correctAnswersAnimation.addListener(new AnimatorListener() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.d(TAG, "onAnimationEnd");
                    setDefaultTextColor(view);
                    setWordToView(currentWord);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }

                @Override
                public void onAnimationStart(Animator animation) {
                }
            });
            markCorrectAnswer(view);
            updateCorrectAnswerCount();
            incrementWordIndex();
            correctAnswersAnimation.start();

        } else {
            wrongAnswersAnimation.addListener(new AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.d(TAG, "onAnimationEnd");
                    setDefaultTextColor(view);
                    setWordToView(currentWord);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }
            });
            updateWrongAnswerCount();
            markWrongAnswer(view);
            wrongAnswersAnimation.start();
        }
    }

    private void incrementWordIndex() {
        Log.d(TAG, "incrementWordIndex");
        currentWord++;
    }

    private void setWordToView(int currentWord) {
        Log.d(TAG, "setWordToView");
        Log.d(TAG, "Current word index: " + currentWord);
        if (currentWord < words.size()) {
            // set the word
            originalWord.setText(words.get(currentWord).getOriginalWord());
            translationsAdapter.setWord(words.get(currentWord));
        } else {
            finish();
        }
    }

    private void markWrongAnswer(View view) {
        Log.d(TAG, "markWrongAnswer");
        TextView textView = (TextView) view.findViewById(R.id.translation);
        textView.setTextColor(Color.RED);
    }

    private void markCorrectAnswer(View view) {
        Log.d(TAG, "markCorrectAnswer");
        TextView textView = (TextView) view.findViewById(R.id.translation);
        textView.setTextColor(Color.GREEN);
    }

    private void setDefaultTextColor(View view) {
        Log.d(TAG, "setDefaultTextColor");
        TextView textView = (TextView) view.findViewById(R.id.translation);
        textView.setTextColor(Color.BLACK);
    }
}

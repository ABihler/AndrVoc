package de.robertmathes.android.orangeiron;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import de.robertmathes.android.orangeiron.adapter.QuestionListViewAdapter;
import de.robertmathes.android.orangeiron.db.DataSource;
import de.robertmathes.android.orangeiron.model.Lesson;
import de.robertmathes.android.orangeiron.model.User;

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

        // get the current user and lesson
        user = db.getUser(appPrefs.getCurrentUser());
        lesson = db.getLesson(appPrefs.getCurrentLesson());

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
        correctAnswers.setText(correctAnswersCount + "");
    }

    private void updateBadAnswerCount() {
        badAnswersCount++;
        badAnswers.setText(badAnswersCount + "");
    }

    // private void init() {
    // appPrefs = new AppPreferences(getApplicationContext());
    // currentLesson = appPrefs.getCurrentLesson();
    //
    // textTop = (TextView) findViewById(R.id.question_field_top);
    // textLog = (TextView) findViewById(R.id.question_field_log);
    // textLog.setMovementMethod(new ScrollingMovementMethod());
    // textStatus = (TextView) findViewById(R.id.question_field_status);
    //
    // button = (Button) findViewById(R.id.question_button_main);
    // button.setEnabled(false);
    // // answerSpinner = (Spinner) findViewById(R.id.question_spinner_answer);
    //
    // containerGroup = (RadioGroup) findViewById(R.id.answerContainer);
    // containerGroup.setOnCheckedChangeListener(this);
    //
    // textLog.setMovementMethod(new ScrollingMovementMethod());
    // textStatus = (TextView) findViewById(R.id.question_field_status);
    //
    // log("onCreate");
    //
    // setStatusLine("Status: unbekannt");
    //
    // // Vokabeln der aktuellen Lektion lade
    // this.vocList = loadVocabulary(currentLesson);
    //
    // // Abfragereihenfolge der Vokabeln mischen
    // Collections.shuffle(this.vocList);
    // }
    //
    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    // // Inflate the menu; this adds items to the action bar if it is present.
    // getMenuInflater().inflate(R.menu.question, menu);
    // return true;
    // }
    //
    // @Override
    // public void onConfigurationChanged(Configuration newConfig) {
    // super.onConfigurationChanged(newConfig);
    //
    // // Checks the orientation of the screen
    // if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
    // setContentView(R.layout.activity_question_land);
    // } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
    // setContentView(R.layout.activity_question);
    // }
    // init();
    // setStatusCheck();
    // populateFields(actTest);
    // }
    //
    // // Check answer
    // public void doCheck(View view) {
    // if (currentSelectedAnswer.equals(vocList.get(actTest).getCorrectTranslation())) {
    // textResult = (TextView) findViewById(R.id.question_field_result);
    // textResult.setTextColor(Color.rgb(50, 205, 50));
    // textResult.setText("Die Antwort ist richtig!!!");
    // numRightAnswers++;
    // setStatusNext();
    //
    // setStatusLine(getStasiticString());
    // // Ende der Lektion
    // if (actTest == (vocList.size() - 1)) {
    // setStatusLine(getStasiticString() + "\nEnde der Lektion erreicht.");
    // button.setEnabled(false);
    // // TODO:Statistikausgabe, Button evtl. auf zurück ummappen.
    // }
    // } else {
    // textResult = (TextView) findViewById(R.id.question_field_result);
    // textResult.setTextColor(Color.RED);
    // textResult.setText("Die Antwort ist leider falsch.");
    // numWrongAnswers++;
    // setStatusCheck();
    // setStatusLine(getStasiticString());
    // }
    // }
    //
    // public void doMain(View view) {
    // if ("Next".equals(status)) {
    // doNext(view);
    // } else if ("Check".equals(status)) {
    // doCheck(view);
    // } else {
    // setStatusLine("unknown status: " + status);
    // }
    // }
    //
    // // Nächste Frage
    // public void doNext(View view) {
    //
    // if (actTest <= (vocList.size() - 2)) {
    // clearResult();
    // button.setEnabled(false);
    // actTest++;
    // populateFields(actTest);
    // setStatusCheck();
    // } else {
    // // hierher sollten wir aber nie gelangen, weil in doCheck bereits
    // // auf das Ende der Lektion gepr�ft wird.
    // setStatusLine(getStasiticString() + "\nEnde");
    // }
    // }
    //
    // // Füllt Felder mit Daten aus Vokabel Objekt
    // private void populateFields(int index) {
    // Vokabel vokabel = vocList.get(index);
    // setStatusLine(getStasiticString());
    //
    // textWord = (TextView) findViewById(R.id.question_field_word);
    //
    // textWord.setText(vokabel.getOriginalWord());
    // textResult = (TextView) findViewById(R.id.question_field_result);
    // textResult.setText("");
    //
    // // Anzahl der Antwortmöglichkeiten = Falsche Möglichkeiten + richtige Übersetzung
    // int max = vokabel.getAlternativeTranslations().size() + 1;
    //
    // // Radiobuttons der letzten Vokabel löschen
    // containerGroup.removeAllViews();
    //
    // // Antworten zusammenstellen
    // String[] answers = new String[max];
    // vokabel.getAlternativeTranslations().toArray(answers);
    // answers[max - 1] = vokabel.getCorrectTranslation();
    // // und mischen
    // List<String> shuffleList = Arrays.asList(answers);
    // Collections.shuffle(shuffleList);
    // shuffleList.toArray(answers);
    //
    // for (int i = 0; i <= max - 1; i++) {
    // RadioButton radioButton = new RadioButton(this);
    // radioButton.setText(answers[i]);
    // radioButton.setTextSize(20);
    // radioButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    // containerGroup.addView(radioButton);
    // }
    // }
    //
    // private void clearResult() {
    // textResult.setText("");
    // }
    //
    // // Setzt Statusfeld
    // private void setStatusLine(String message) {
    // // textStatus = (TextView) findViewById(R.id.question_field_status);
    // textStatus.setText(message);
    // }
    //
    // private void exceptionOutput(String s) {
    // textStatus.setText(s);
    // }
    //
    // private String getStasiticString() {
    // String stat = "Statistik:" + numRightAnswers + " richtig und " + numWrongAnswers + " falsch.";
    // return stat;
    // }
    //
    // private void setStatusCheck() {
    // status = "Check";
    // containerGroup.setEnabled(true);
    // button.setText(R.string.question_button_check);
    // }
    //
    // private void setStatusNext() {
    // status = "Next";
    // containerGroup.setEnabled(false);
    // button.setText(R.string.question_button_next);
    // }
    //
    // private void log(String s) {
    // if (logActive) {
    // textLog.append("\n" + s);
    // }
    // }
    //
    // private List<Vokabel> loadVocabulary(long currentLesson) {
    // log("loadVocabulary");
    // List<Vokabel> vocList = new ArrayList<Vokabel>();
    //
    // DataSource vocabularyDataSource = new DataSource(getApplicationContext());
    // vocabularyDataSource.open();
    // vocList = vocabularyDataSource.getVocabulary(currentLesson);
    // vocabularyDataSource.close();
    //
    // return vocList;
    // }
    //

    @SuppressLint("NewApi")
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (translationsAdapter.getItem(position).equals(lesson.getVocabulary().get(currentWord).getCorrectTranslation())) {
            Toast.makeText(this, "Correct", Toast.LENGTH_LONG).show();
            updateCorrectAnswerCount();
            moveToNextWord();
        } else {
            Toast.makeText(this, "Wrong", Toast.LENGTH_LONG).show();
            updateBadAnswerCount();
        }
    }

    private void moveToNextWord() {
        currentWord++;
        if (currentWord < lesson.getVocabulary().size()) {
            originalWord.setText(lesson.getVocabulary().get(currentWord).getOriginalWord());
            translationsAdapter.setWord(lesson.getVocabulary().get(currentWord));
            translationsAdapter.notifyDataSetChanged();
        }
    }
}

package de.albert.bihler.andrvoc;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import de.albert.bihler.andrvoc.db.DataSource;
import de.albert.bihler.andrvoc.model.Lesson;
import de.albert.bihler.andrvoc.model.Vokabel;
import de.albert.bihler.andrvoc.orangeiron.R;

/**
 * Demonstrates a "card-flip" animation using custom fragment transactions ( {@link android.app.FragmentTransaction#setCustomAnimations(int, int)}).
 * 
 * <p>
 * This sample shows an "info" action bar button that shows the back of a "card", rotating the front of the card out and the back of the card in. The reverse animation is played when the user presses the system Back button or the "photo" action bar button.
 * </p>
 */
public class LearningActivity extends Activity implements FragmentManager.OnBackStackChangedListener, OnInitListener {

    /**
     * A handler object, used for deferring UI operations.
     */
    private final Handler mHandler = new Handler();
    /**
     * Whether or not we're showing the back of the card (otherwise showing the front).
     */
    private boolean mShowingBack = false;
    private AppPreferences appPrefs;
    private DataSource db;
    private Lesson lesson;
    private static List<Vokabel> words;
    private static int currentWord;
    private CardFrontFragment frontCard;
    private CardBackFragment backCard;
    private final int MY_DATA_CHECK_CODE = 0;
    private static TextToSpeech myTTS;
    private final Locale SPANISH = new Locale("es", "ES");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        // Open database
        if (db == null) {
            db = new DataSource(getApplicationContext());
        }
        db.open();
        appPrefs = new AppPreferences(getApplicationContext());
        // get the current lesson
        lesson = db.getLessonById(appPrefs.getCurrentLesson());
        // Get the words and shuffle them
        words = lesson.getVocabulary();
        Collections.shuffle(words);
        currentWord = 0;
        if (savedInstanceState == null) {
            // If there is no saved instance state, add a fragment representing
            // the
            // front of the card to this activity. If there is saved instance
            // state,
            // this fragment will have already been added to the activity.
            frontCard = new CardFrontFragment();
            getFragmentManager().beginTransaction().add(R.id.learning_container, frontCard).commit();
        } else {
            mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        }
        // Monitor back stack changes to ensure the action bar shows the
        // appropriate
        // button (either "photo" or "info").
        getFragmentManager().addOnBackStackChangedListener(this);
        // handle touches on screen
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.learning_container);
        frameLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                flipCard();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Intialize the TTS engine
        if (myTTS == null) {
            Intent checkTTSIntent = new Intent();
            checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                myTTS = new TextToSpeech(this, this);
            }
            else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    @Override
    public void onInit(int initStatus) {
        if (initStatus == TextToSpeech.SUCCESS) {
            if ("en".equals(lesson.getLanguage())) {
                if (myTTS.isLanguageAvailable(Locale.UK) == TextToSpeech.LANG_AVAILABLE) {
                    myTTS.setLanguage(Locale.UK);
                } else {
                    myTTS.setLanguage(Locale.US);
                }
            } else if ("es".equals(lesson.getLanguage())) {
                // if (myTTS.isLanguageAvailable(SPANISH) == TextToSpeech.LANG_AVAILABLE) {
                myTTS.setLanguage(SPANISH);
                // } else {
                // myTTS.setLanguage(Locale.US);
                // }
            } else if ("fr".equals(lesson.getLanguage())) {
                if (myTTS.isLanguageAvailable(Locale.FRENCH) == TextToSpeech.LANG_AVAILABLE) {
                    myTTS.setLanguage(Locale.FRENCH);
                } else {
                    myTTS.setLanguage(Locale.US);
                }
            }
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.learning, menu);
        MenuItem previousWord = menu.findItem(R.id.action_previousWord);
        MenuItem nextWord = menu.findItem(R.id.action_nextWord);
        if (previousWord != null) {
            previousWord.setEnabled(isFirstWord() ? false : true);
            previousWord.setVisible(isFirstWord() ? false : true);
        }
        if (nextWord != null) {
            nextWord.setEnabled(isLastWord() ? false : true);
            nextWord.setVisible(isLastWord() ? false : true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_nextWord:
                showNextWord();
                return true;
            case R.id.action_previousWord:
                showPreviousWord();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isFirstWord() {
        return currentWord == 0;
    }

    private boolean isLastWord() {
        return currentWord == words.size() - 1;
    }

    private void showNextWord() {
        currentWord++;
        if (mShowingBack) {
            flipCard();
        } else {
            frontCard.updateView();
            new Handler().post(new Runnable() {

                @Override
                public void run() {
                    invalidateOptionsMenu();
                }
            });
        }
    }

    private void showPreviousWord() {
        currentWord--;
        if (mShowingBack) {
            flipCard();
        } else {
            frontCard.updateView();
            new Handler().post(new Runnable() {

                @Override
                public void run() {
                    invalidateOptionsMenu();
                }
            });
        }
    }

    private void flipCard() {
        if (mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }
        // Flip to the back.
        mShowingBack = true;
        // Create and commit a new fragment transaction that adds the fragment
        // for the back of
        // the card, uses custom animations, and is part of the fragment
        // manager's back stack.
        backCard = new CardBackFragment();
        getFragmentManager().beginTransaction()
                // Replace the default fragment animations with animator resources
                // representing
                // rotations when switching to the back of the card, as well as animator
                // resources representing rotations when flipping back to the front
                // (e.g. when
                // the system Back button is pressed).
                .setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out, R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                // Replace any fragments currently in the container view with a
                // fragment
                // representing the next page (indicated by the just-incremented
                // currentPage
                // variable).
                .replace(R.id.learning_container, backCard)
                // Add this transaction to the back stack, allowing users to
                // press Back
                // to get to the front of the card.
                .addToBackStack(null)
                // Commit the transaction.
                .commit();
        // Defer an invalidation of the options menu (on modern devices, the
        // action bar). This
        // can't be done immediately because the transaction may not yet be
        // committed. Commits
        // are asynchronous in that they are posted to the main thread's message
        // loop.
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
        // When the back stack changes, invalidate the options menu (action
        // bar).
        invalidateOptionsMenu();
    }

    /**
     * A fragment representing the front of the card.
     */
    public static class CardFrontFragment extends Fragment {

        public CardFrontFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_front, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            updateView();
        }

        public void updateView() {
            TextView correctTranslation = (TextView) getActivity().findViewById(R.id.learning_originalWord);
            correctTranslation.setText(words.get(currentWord).getOriginalWord());
        }
    }

    /**
     * A fragment representing the back of the card.
     */
    public static class CardBackFragment extends Fragment {

        public CardBackFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_card_back, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            updateView();
            attachListeners();
        }

        public void updateView() {
            TextView correctTranslation = (TextView) getActivity().findViewById(R.id.learning_correctTranslation);
            correctTranslation.setText(words.get(currentWord).getCorrectTranslation());
        }

        public void attachListeners() {
            ImageButton speakButton = (ImageButton) getActivity().findViewById(R.id.learning_speakTranslation);
            speakButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    speakWords(words.get(currentWord).getCorrectTranslation());
                }
            });
        }
    }

    private static void speakWords(String words) {
        myTTS.speak(words, TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myTTS != null) {
            myTTS.shutdown();
            myTTS = null;
        }
    }
}
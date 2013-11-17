package de.robertmathes.android.orangeiron;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {

    public final static String VOCABULARY_SERVER_URL = "vocabulary_server_url";

    public static String CURRENT_LESSON = "current_lesson";
    public static String CURRENT_USER = "current_user";
    // Name of the file -.xml
    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName();
    private final SharedPreferences sharedPrefs;
    private final Editor prefsEditor;

    public AppPreferences(Context context) {
        this.sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = sharedPrefs.edit();
    }

    public long getCurrentLesson() {
        return sharedPrefs.getLong(CURRENT_LESSON, 1);
    }

    public void saveCurrentLesson(long lessonId) {
        prefsEditor.putLong(CURRENT_LESSON, lessonId);
        prefsEditor.commit();
    }

    public long getCurrentUser() {
        return sharedPrefs.getLong(CURRENT_USER, 1);
    }

    public void saveCurrentUser(long userId) {
        prefsEditor.putLong(CURRENT_USER, userId);
        prefsEditor.commit();
    }

    public String getVocabularyServer() {
        String url = sharedPrefs.getString(VOCABULARY_SERVER_URL, "none");
        return url;
    }

    public void saveVocabularyServer(String vocabularyServer) {
        prefsEditor.putString(VOCABULARY_SERVER_URL, vocabularyServer);
        prefsEditor.commit();
    }
}
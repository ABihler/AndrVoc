package de.albert.bihler.andrvoc;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {

    private final static String VOCABULARY_SERVER_URL = "vocabulary_server_url";

    public static String CURRENT_UNIT;
    public static String CURRENT_USER;
    public static int CURRENT_UNIT_ID;
    // Name of the file -.xml
    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName();
    private final SharedPreferences sharedPrefs;
    private final Editor prefsEditor;

    public AppPreferences(Context context) {
	this.sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
	this.prefsEditor = sharedPrefs.edit();

	CURRENT_UNIT = sharedPrefs.getString(CURRENT_UNIT, "en_unit00_01");
	CURRENT_UNIT_ID = context.getResources().getIdentifier(CURRENT_UNIT, "xml", context.getPackageName());
    }

    public String getUnit() {
	return sharedPrefs.getString(CURRENT_UNIT, "en_unit00_01");
    }

    public int getUnitID(Context context) {
	return context.getResources().getIdentifier(CURRENT_UNIT, "xml", context.getPackageName());
    }

    public void saveUnit(String text) {
	prefsEditor.putString(CURRENT_UNIT, text);
	prefsEditor.commit();
    }

    public void saveUser(String text) {
	prefsEditor.putString(CURRENT_USER, text);
	prefsEditor.commit();
    }

    public String getUser() {
	return sharedPrefs.getString(CURRENT_USER, "unknown");
    }

    public URL getVocabularyServer() {
	String url = sharedPrefs.getString(VOCABULARY_SERVER_URL, null);
	if (url != null) {
	    try {
		return new URL(url);
	    } catch (MalformedURLException e) {
		return null;
	    }
	}
	return null;
    }
}
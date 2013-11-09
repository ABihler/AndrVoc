package de.albert.bihler.andrvoc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
    public static String CURRENT_UNIT;
    public static String CURRENT_USER;
    public static int CURRENT_UNIT_ID;
    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName(); //  Name of the file -.xml
    private SharedPreferences sharedPrefs;
    private Editor prefsEditor;

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
    	return  context.getResources().getIdentifier(CURRENT_UNIT, "xml", context.getPackageName());
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
}
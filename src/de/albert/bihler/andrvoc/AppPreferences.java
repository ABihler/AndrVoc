package de.albert.bihler.andrvoc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferences {
    public static String CURRENT_UNIT = "en_unit00_01";
    public static int CURRENT_UNIT_ID = 0;
    private static final String APP_SHARED_PREFS = AppPreferences.class.getSimpleName(); //  Name of the file -.xml
    private SharedPreferences sharedPrefs;
    private Editor prefsEditor;

    public AppPreferences(Context context) {
        this.sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
        this.prefsEditor = sharedPrefs.edit();
    
        CURRENT_UNIT_ID = context.getResources().getIdentifier(CURRENT_UNIT, "xml", context.getPackageName());
    }

    public String getUnit() {
        return sharedPrefs.getString(CURRENT_UNIT, "en_unit00_01");
    }

    public int getUnitID() {
        return CURRENT_UNIT_ID;
    }    
    
    public void saveUnit(String text) {
        prefsEditor.putString(CURRENT_UNIT, text);
        prefsEditor.commit();
        //TODO:Hier muss die Unit gespeichet werden. Sonst wird sie nicht richtig übernommen.
       // CURRENT_UNIT_ID = getResources().getIdentifier(CURRENT_UNIT, "xml", getPackageName());
    }
}
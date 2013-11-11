package de.albert.bihler.andrvoc;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AndrVoc";
    private static final String USER_TABLE_NAME = "user";
    private static final String USER_NAME_COLUMN = "username";
    private static final String USER_TABLE_CREATE = "CREATE TABLE " + USER_TABLE_NAME + " (" + USER_NAME_COLUMN + " TEXT );";

    // private final SQLiteDatabase db;

    DBHelper(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	// this.db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	db.execSQL(USER_TABLE_CREATE);
	db.execSQL("insert into user values (\'Erik\')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	// TODO Auto-generated method stub
    }

    public void closeDB() {
	SQLiteDatabase db = this.getReadableDatabase();
	if (db != null && db.isOpen())
	    db.close();
    }

    public List<String> getAllUsers() {
	List<String> users = new ArrayList<String>();
	String selectQuery = "SELECT " + USER_NAME_COLUMN + " FROM " + USER_TABLE_NAME;
	SQLiteDatabase db = this.getReadableDatabase();
	Cursor c = db.rawQuery(selectQuery, null);
	// Alle User durchlaufen
	if (c.moveToFirst()) {
	    do {
		String u = c.getString(c.getColumnIndex(USER_NAME_COLUMN));
		users.add(u);
	    } while (c.moveToNext());
	}
	return users;
    }

    public void insertUser(String username) {
	ContentValues values = new ContentValues();
	values.put(USER_NAME_COLUMN, username);
	SQLiteDatabase db = this.getWritableDatabase();
	db.insert(USER_TABLE_NAME, null, values);
    }
}
package de.albert.bihler.andrvoc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LessonOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "AndrVoc.db";

	public static final String TABLE_NAME = "lessons";

	public interface Column {
		static final String ID = "_id";
		static final String LESSON_NAME = "lessonName";
		static final String LESSON_LANGUAGE = "lessonLanguage";
		static final String LESSON_VERSION = "lessonVersion";
	}

	private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + Column.ID + " integer primary key autoincrement, " + Column.LESSON_NAME + " text not null, " + Column.LESSON_LANGUAGE + " text not null, " + Column.LESSON_VERSION + " integer not null);";

	LessonOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LessonOpenHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}

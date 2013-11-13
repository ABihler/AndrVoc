package de.albert.bihler.andrvoc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class VocabularyOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "AndrVoc.db";

	private static final String TABLE_NAME = "vocabulary";

	public interface Column {
		static final String ID = "_id";
		static final String ORIGINAL_WORD = "originalWord";
		static final String CORRECT_TRANSLATION = "correctTranslation";
		static final String LESSON_ID = "lessonId";
	}

	private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + Column.ID + " integer primary key autoincrement, " + Column.ORIGINAL_WORD + " text not null, " + Column.CORRECT_TRANSLATION + " text not null, " + Column.LESSON_ID + " integer not null);";

	VocabularyOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(VocabularyOpenHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}

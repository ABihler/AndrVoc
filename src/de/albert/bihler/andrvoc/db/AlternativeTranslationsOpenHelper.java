package de.albert.bihler.andrvoc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlternativeTranslationsOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "AndrVoc.db";

	private static final String TABLE_NAME = "alternativeTranslations";

	public interface Column {
		static final String ID = "_id";
		static final String TRANSLATION = "translation";
		static final String VOCABULARY_ID = "vocabularyId";
	}

	private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + Column.ID + " integer primary key autoincrement, " + Column.TRANSLATION + " text not null, " + Column.VOCABULARY_ID + " integer not null);";

	AlternativeTranslationsOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(AlternativeTranslationsOpenHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}

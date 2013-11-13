package de.albert.bihler.andrvoc.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.albert.bihler.andrvoc.model.Lesson;

public class LessonDataSource {

    private static final String TAG = "LessonDataSource";

    private SQLiteDatabase database;
    private final AndrVocOpenHelper dbHelper;
    private final Context ctx;

    // private final String[] allColumns = { MySQLiteHelper.COLUMN_ID,
    // MySQLiteHelper.COLUMN_COMMENT };

    public LessonDataSource(Context context) {
	this.ctx = context;
	dbHelper = new AndrVocOpenHelper(context);
    }

    public void open() throws SQLException {
	database = dbHelper.getWritableDatabase();
    }

    public void close() {
	dbHelper.close();
    }

    /**
     * Liefert eine komplette Lektion zurück inklusive aller Vokabeln und
     * Übersetzungen.
     * 
     * @param lessonId
     * @return Lektion mit der id lessonId
     */
    public Lesson getLesson(int lessonId) {
	Log.i(TAG, "Loading lesson " + lessonId);
	// Lektion laden
	Lesson lesson = new Lesson();

	Cursor cursor = database.query(AndrVocOpenHelper.TABLE_NAME_LESSONS, AndrVocOpenHelper.ALL_COLUMNS_LESSONS, AndrVocOpenHelper.LessonColumn.ID + "="
		+ lessonId, null, null, null, null);

	cursor.moveToFirst();
	while (!cursor.isAfterLast()) {
	    lesson.setId(cursor.getLong(0));
	    lesson.setName(cursor.getString(1));
	    lesson.setLanguage(cursor.getString(2));
	    lesson.setVersion(cursor.getInt(3));
	    cursor.moveToNext();
	}
	cursor.close();

	// Vokabeln der Lektion laden
	VocabularyDataSource vocabularyDataSource = new VocabularyDataSource(ctx);
	vocabularyDataSource.open();
	lesson.setVocabulary(vocabularyDataSource.getVocabulary(lesson.getId()));
	vocabularyDataSource.close();

	return lesson;
    }

    /**
     * Liefert eine Liste von Lessons-Objekten ohne Vokabeln.
     * 
     * @return Liste von Lektionen ohne Vokabeln
     */
    public List<Lesson> getLessons() {
	Log.i(TAG, "Loading all lessons");

	List<Lesson> lessons = new ArrayList<Lesson>();

	Cursor cursor = database.query(AndrVocOpenHelper.TABLE_NAME_LESSONS, AndrVocOpenHelper.ALL_COLUMNS_LESSONS, null, null, null, null, null);

	cursor.moveToFirst();
	while (!cursor.isAfterLast()) {
	    Lesson lesson = new Lesson();
	    lesson.setId(cursor.getLong(0));
	    lesson.setName(cursor.getString(1));
	    lesson.setLanguage(cursor.getString(2));
	    lesson.setVersion(cursor.getInt(3));
	    lessons.add(lesson);
	    cursor.moveToNext();
	}
	cursor.close();

	return lessons;
    }

    /**
     * Sichert eine Lektion, inklusive aller Vokabeln und falschen Übersetzungen
     * 
     * @param lesson
     *            zu sichernde Lektion
     * @return die Id der erzeugten Lektion
     */
    public long saveLesson(Lesson lesson) {
	Log.i(TAG, "Saving lesson " + lesson.getName());
	// Lektion sichern
	ContentValues values = new ContentValues();
	values.put(AndrVocOpenHelper.LessonColumn.LESSON_NAME, lesson.getName());
	values.put(AndrVocOpenHelper.LessonColumn.LESSON_LANGUAGE, lesson.getLanguage());
	values.put(AndrVocOpenHelper.LessonColumn.LESSON_VERSION, lesson.getVersion());
	long lessonId = database.insert(AndrVocOpenHelper.TABLE_NAME_LESSONS, null, values);

	// Vokabeln der Lektion sichern
	VocabularyDataSource vocabularyDataSource = new VocabularyDataSource(this.ctx);
	vocabularyDataSource.open();
	vocabularyDataSource.saveVocabulary(lesson.getVocabulary(), lessonId);
	vocabularyDataSource.close();

	return lessonId;
    }
}

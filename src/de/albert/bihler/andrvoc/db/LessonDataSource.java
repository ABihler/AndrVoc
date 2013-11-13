package de.albert.bihler.andrvoc.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import de.albert.bihler.andrvoc.model.Lesson;
import de.albert.bihler.andrvoc.model.Vokabel;

public class LessonDataSource {

	private SQLiteDatabase database;
	private final LessonOpenHelper dbHelper;
	private final Context ctx;

	// private final String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_COMMENT };

	public LessonDataSource(Context context) {
		this.ctx = context;
		dbHelper = new LessonOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Lesson getLesson(int lessonId) {
		return null;
	}

	public void saveLesson(Lesson lesson) {
		// Lektion sichern
		ContentValues values = new ContentValues();
		values.put(LessonOpenHelper.Column.LESSON_NAME, lesson.getName());
		values.put(LessonOpenHelper.Column.LESSON_VERSION, lesson.getVersion());
		long lessonId = database.insert(LessonOpenHelper.TABLE_NAME, null, values);

		// Vokabeln der Lektion sichern
		VocabularyDataSource vocabularyDataSource = new VocabularyDataSource(this.ctx);
		for (Vokabel vokabel : lesson.getVocabulary()) {
			vocabularyDataSource.saveWord(vokabel, lessonId);
		}

		// TODO: lesson wieder holen und zurück geben
	}
}

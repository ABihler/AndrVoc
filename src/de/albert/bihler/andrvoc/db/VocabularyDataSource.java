package de.albert.bihler.andrvoc.db;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Comment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import de.albert.bihler.andrvoc.model.Vokabel;

public class VocabularyDataSource {

	// Database fields
	private SQLiteDatabase database;
	private final VocabularyOpenHelper dbHelper;
	private final String[] allColumns = { VocabularyOpenHelper.Column.ID, VocabularyOpenHelper.Column.ORIGINAL_WORD, VocabularyOpenHelper.Column.CORRECT_TRANSLATION, VocabularyOpenHelper.Column.LESSON_ID };

	public VocabularyDataSource(Context context) {
		dbHelper = new VocabularyOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void saveWord(Vokabel vokabel, long lessonId) {
		ContentValues values = new ContentValues();
		values.put(VocabularyOpenHelper.Column.ORIGINAL_WORD, vokabel.getOriginalWord());
		values.put(VocabularyOpenHelper.Column.CORRECT_TRANSLATION, vokabel.getCorrectTranslation());
		values.put(VocabularyOpenHelper.Column.LESSON_ID, lessonId);
	}

	// public Comment createComment(String comment) {
	// ContentValues values = new ContentValues();
	// values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
	// long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null,
	// values);
	// Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
	// allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
	// null, null, null);
	// cursor.moveToFirst();
	// Comment newComment = cursorToComment(cursor);
	// cursor.close();
	// return newComment;
	// }
	//
	// public void deleteComment(Comment comment) {
	// long id = comment.getId();
	// System.out.println("Comment deleted with id: " + id);
	// database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID
	// + " = " + id, null);
	// }
	//
	// public List<Comment> getAllComments() {
	// List<Comment> comments = new ArrayList<Comment>();
	//
	// Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
	// allColumns, null, null, null, null, null);
	//
	// cursor.moveToFirst();
	// while (!cursor.isAfterLast()) {
	// Comment comment = cursorToComment(cursor);
	// comments.add(comment);
	// cursor.moveToNext();
	// }
	// // make sure to close the cursor
	// cursor.close();
	// return comments;
	// }
	//
	// private Comment cursorToComment(Cursor cursor) {
	// Comment comment = new Comment();
	// comment.setId(cursor.getLong(0));
	// comment.setComment(cursor.getString(1));
	// return comment;
	// }
}

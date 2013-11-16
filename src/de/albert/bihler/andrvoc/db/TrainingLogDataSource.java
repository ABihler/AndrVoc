package de.albert.bihler.andrvoc.db;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TrainingLogDataSource {

    private static final String TAG = "TrainingLogDataSource";

    private SQLiteDatabase database;
    private final AndrVocOpenHelper dbHelper;
    private final Context ctx;

    public TrainingLogDataSource(Context context) {
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
     * Sichert einen Logeintrag
     * 
     * @param user
     *            , lesson, word, correct_result (0/1)
     * @return die Id des erzeugten Logeintrags
     */
    // TODO: Das hier evtl. umbauen für die Vokabel-ID
    public long saveTrainingLog(String user, String lesson, String word, int res) {
        Log.i(TAG, "Saving Training Log");
        // Training Log sichern
        ContentValues values = new ContentValues();
        values.put(AndrVocOpenHelper.TrainingLogColumn.USER, user);
        values.put(AndrVocOpenHelper.TrainingLogColumn.LESSON_NAME, lesson);
        values.put(AndrVocOpenHelper.TrainingLogColumn.WORD, word);
        values.put(AndrVocOpenHelper.TrainingLogColumn.CORRECT_RESULT, res);

        long TrainLogId = database.insert(AndrVocOpenHelper.TABLE_NAME_TRAINING_LOG, null, values);
        return TrainLogId;
    }

    public long getNumberOfLogsForUser(String user) {
        Cursor c = database.rawQuery("select count(*) from " + AndrVocOpenHelper.TABLE_NAME_TRAINING_LOG + " where " + AndrVocOpenHelper.TrainingLogColumn.USER
                + " = '" + user + "'", null);
        c.moveToFirst();
        long count = c.getInt(0);
        c.close();
        return count;
    }

    public long getNumberOfErrorLogsForUser(String user) {
        Cursor c = database.rawQuery("select count(*) from " + AndrVocOpenHelper.TABLE_NAME_TRAINING_LOG + " where " + AndrVocOpenHelper.TrainingLogColumn.USER
                + " = '" + user + "' AND " + AndrVocOpenHelper.TrainingLogColumn.CORRECT_RESULT + " = 0", null);
        c.moveToFirst();
        long count = c.getInt(0);
        c.close();
        return count;
    }

    public long getNumberOfSuccessLogsForUser(String user) {
        Cursor c = database.rawQuery("select count(*) from " + AndrVocOpenHelper.TABLE_NAME_TRAINING_LOG + " where " + AndrVocOpenHelper.TrainingLogColumn.USER
                + " = '" + user + "' AND " + AndrVocOpenHelper.TrainingLogColumn.CORRECT_RESULT + " = 1", null);
        c.moveToFirst();
        long count = c.getInt(0);
        c.close();
        return count;
    }
}

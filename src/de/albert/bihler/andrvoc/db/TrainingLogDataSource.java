package de.albert.bihler.andrvoc.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.albert.bihler.andrvoc.model.Vokabel;

public class TrainingLogDataSource {

    private static final String TAG = "TrainingLogDataSource";

    private SQLiteDatabase database;
    private final DbOpenHelper dbHelper;
    private final Context ctx;

    public TrainingLogDataSource(Context context) {
        this.ctx = context;
        dbHelper = new DbOpenHelper(context);
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
    public long saveTrainingLog(int user, int vocID, int res) {
        Log.i(TAG, "Saving Training Log");
        // Training Log sichern
        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.TrainingLogColumn.USER, user);
        values.put(DbOpenHelper.TrainingLogColumn.VOKABEL_ID, vocID);
        // values.put(DbOpenHelper.TrainingLogColumn.WORD, word);
        values.put(DbOpenHelper.TrainingLogColumn.CORRECT_RESULT, res);

        long TrainLogId = database.insert(DbOpenHelper.TABLE_NAME_TRAINING_LOG, null, values);
        return TrainLogId;
    }

    public long getNumberOfLogsForUser(String user) {
        Cursor c = database.rawQuery("select count(*) from " + DbOpenHelper.TABLE_NAME_TRAINING_LOG + " where " + DbOpenHelper.TrainingLogColumn.USER
                + " = '" + user + "'", null);
        c.moveToFirst();
        long count = c.getInt(0);
        c.close();
        return count;
    }

    public long getNumberOfErrorLogsForUser(String user) {
        Cursor c = database.rawQuery("select count(*) from " + DbOpenHelper.TABLE_NAME_TRAINING_LOG + " where " + DbOpenHelper.TrainingLogColumn.USER
                + " = '" + user + "' AND " + DbOpenHelper.TrainingLogColumn.CORRECT_RESULT + " = 0", null);
        c.moveToFirst();
        long count = c.getInt(0);
        c.close();
        return count;
    }

    public long getNumberOfSuccessLogsForUser(String user) {
        Cursor c = database.rawQuery("select count(*) from " + DbOpenHelper.TABLE_NAME_TRAINING_LOG + " where " + DbOpenHelper.TrainingLogColumn.USER
                + " = '" + user + "' AND " + DbOpenHelper.TrainingLogColumn.CORRECT_RESULT + " = 1", null);
        c.moveToFirst();
        long count = c.getInt(0);
        c.close();
        return count;
    }

    // Holt die Vokabeln mit den meisten falsche Antworten
    public List<Vokabel> getWorstForUser(long user) {

        List<Vokabel> vocabulary = new ArrayList<Vokabel>();
        Cursor c = database.rawQuery("select count(*) as Anzahl, " + DbOpenHelper.TrainingLogColumn.VOKABEL_ID + " from "
                + DbOpenHelper.TABLE_NAME_TRAINING_LOG + " where "
                + DbOpenHelper.TrainingLogColumn.USER
                + " = '" + user + "' AND " + DbOpenHelper.TrainingLogColumn.CORRECT_RESULT + " = 0 group by "
                + DbOpenHelper.TrainingLogColumn.VOKABEL_ID + " order by Anzahl desc", null);
        // TODO Auf x begrenzen
        if (c != null & c.getCount() > 0) {
            c.moveToFirst();
            do {

                VocabularyDataSource vocDS = new VocabularyDataSource(ctx);
                vocDS.open();
                Vokabel v = vocDS.getVocabularyById(c.getInt(1));
                vocDS.close();
                vocabulary.add(v);

            } while (c.moveToNext());
        }
        c.close();
        return vocabulary;
    }
}

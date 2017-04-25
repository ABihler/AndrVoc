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

public class VocabularyDataSource {

    private static final String TAG = "VocabularyDataSource";

    private SQLiteDatabase database;
    private final DbOpenHelper dbHelper;
    private final Context ctx;

    public VocabularyDataSource(Context context) {
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
     * Sichert eine Vokabel inklusive der falschen Übersetzungen
     * 
     * @param vokabel
     *            Vokabel, die gesichert werden soll
     * @param lessonId
     *            Id der Lektion zu der die Vokabel gehört
     */
    public void saveWord(Vokabel vokabel, long lessonId) {
        Log.i(TAG, "Saving word " + vokabel.getOriginalWord() + " for lesson " + lessonId);
        // Vokabel sichern
        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.VocabularyColumn.ORIGINAL_WORD, vokabel.getOriginalWord());
        values.put(DbOpenHelper.VocabularyColumn.CORRECT_TRANSLATION, vokabel.getCorrectTranslation());
        values.put(DbOpenHelper.VocabularyColumn.LESSON_ID, lessonId);
        long vokabelId = database.insert(DbOpenHelper.TABLE_NAME_VOCABULARY, null, values);
        // Alternative Übersetzungen sichern
        AlternativeTranslationsDataSource altTranslationsDataSource = new AlternativeTranslationsDataSource(ctx);
        altTranslationsDataSource.open();
        altTranslationsDataSource.saveAlternativeTranslations(vokabel.getAlternativeTranslations(), vokabelId);
        altTranslationsDataSource.open();
    }

    /**
     * Sichert eine Liste von Vokabeln inklusive der falschen Übersetzungen
     * 
     * @param vocabulary
     *            Liste der Vokabeln
     * @param lessonId
     *            Id der Lektion zu der die Vokabeln gehören
     */
    public void saveVocabulary(List<Vokabel> vocabulary, long lessonId) {
        Log.i(TAG, "Saving " + vocabulary.size() + " words for lesson " + lessonId);
        for (Vokabel vokabel : vocabulary) {
            saveWord(vokabel, lessonId);
        }
    }

    /**
     * Gibt alle Vokabeln, inklusive der falschen Übersetzungen, einer Lektion zurück
     * 
     * @param lessonId
     *            Id der Lektion
     * @return eine Liste von Vokabeln
     */
    public List<Vokabel> getVocabulary(long lessonId) {
        Log.i(TAG, "Loading vocabulary for lesson " + lessonId);
        AlternativeTranslationsDataSource altTranslationsDataSource = new AlternativeTranslationsDataSource(ctx);
        altTranslationsDataSource.open();

        List<Vokabel> vocabulary = new ArrayList<Vokabel>();

        Cursor cursor = database.query(DbOpenHelper.TABLE_NAME_VOCABULARY, DbOpenHelper.ALL_COLUMNS_VOCABULARY,
                DbOpenHelper.VocabularyColumn.LESSON_ID + "=" + lessonId, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Vokabel vokabel = new Vokabel();
            vokabel.setId(cursor.getLong(0));
            vokabel.setOriginalWord(cursor.getString(1));
            vokabel.setCorrectTranslation(cursor.getString(2));
            vokabel.setLessonId(lessonId);
            vokabel.setAlternativeTranslations(altTranslationsDataSource.getAlternativeTranslations(vokabel.getId()));
            vocabulary.add(vokabel);
            cursor.moveToNext();
        }
        cursor.close();

        altTranslationsDataSource.close();

        return vocabulary;
    }

    public Vokabel getVocabularyById(int vocId) {
        AlternativeTranslationsDataSource altTranslationsDataSource = new AlternativeTranslationsDataSource(ctx);
        altTranslationsDataSource.open();

        Cursor cursor = database.query(DbOpenHelper.TABLE_NAME_VOCABULARY, DbOpenHelper.ALL_COLUMNS_VOCABULARY,
                DbOpenHelper.VocabularyColumn.ID + "=" + vocId, null, null, null, null);
        cursor.moveToFirst();
        Vokabel vokabel = new Vokabel();
        vokabel.setId(cursor.getLong(0));
        vokabel.setOriginalWord(cursor.getString(1));
        vokabel.setCorrectTranslation(cursor.getString(2));
        // TODO fix dirty 999 hack
        vokabel.setLessonId(999);
        vokabel.setAlternativeTranslations(altTranslationsDataSource.getAlternativeTranslations(vokabel.getId()));
        cursor.close();

        return vokabel;

    }
}

package de.robertmathes.android.orangeiron.db;

import java.util.ArrayList;
import java.util.List;

import de.robertmathes.android.orangeiron.model.Lesson;
import de.robertmathes.android.orangeiron.model.User;
import de.robertmathes.android.orangeiron.model.Vokabel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataSource {

    private static final String TAG = "DataSource";

    private SQLiteDatabase database;
    private final DbOpenHelper dbHelper;
    private final Context ctx;

    public DataSource(Context context) {
        this.ctx = context;
        dbHelper = new DbOpenHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public User getUser(long userId) {
        return null;
    }

    public List<User> getAllUsers() {
        return null;
    }

    public long saveUser(User user) {

        return -1;
    }

    /**
     * Liefert eine komplette Lektion zurück inklusive aller Vokabeln und Übersetzungen.
     * 
     * @param lessonId
     * @return Lektion mit der id lessonId
     */
    public Lesson getLesson(int lessonId) {
        Log.i(TAG, "Loading lesson " + lessonId);
        // Lektion laden
        Lesson lesson = new Lesson();

        Cursor cursor = database.query(DbOpenHelper.TABLE_NAME_LESSONS, DbOpenHelper.ALL_COLUMNS_LESSONS, DbOpenHelper.LessonColumn.ID + "="
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
        lesson.setVocabulary(getVocabulary(lesson.getId()));

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

        Cursor cursor = database.query(DbOpenHelper.TABLE_NAME_LESSONS, DbOpenHelper.ALL_COLUMNS_LESSONS, null, null, null, null, null);

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
        values.put(DbOpenHelper.LessonColumn.LESSON_NAME, lesson.getName());
        values.put(DbOpenHelper.LessonColumn.LESSON_LANGUAGE, lesson.getLanguage());
        values.put(DbOpenHelper.LessonColumn.LESSON_VERSION, lesson.getVersion());
        long lessonId = database.insert(DbOpenHelper.TABLE_NAME_LESSONS, null, values);

        // Vokabeln der Lektion sichern
        saveVocabulary(lesson.getVocabulary(), lessonId);

        return lessonId;
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
        saveAlternativeTranslations(vokabel.getAlternativeTranslations(), vokabelId);
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
            vokabel.setAlternativeTranslations(getAlternativeTranslations(vokabel.getId()));
            vocabulary.add(vokabel);
            cursor.moveToNext();
        }
        cursor.close();

        return vocabulary;
    }

    public List<String> getAlternativeTranslations(long vocabularyId) {
        Log.i(TAG, "Loading alternative translations for word " + vocabularyId);
        List<String> altTranslations = new ArrayList<String>();

        Cursor cursor = database.query(DbOpenHelper.TABLE_NAME_ALTTRANSLATIONS, DbOpenHelper.ALL_COLUMNS_ALTTRANSLATIONS,
                DbOpenHelper.AltTranslationsColumn.VOCABULARY_ID + "=" + vocabularyId, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            altTranslations.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();

        return altTranslations;
    }

    public void saveAlternativeTranslations(List<String> altTranslations, long vocabularyId) {
        Log.i(TAG, "Saving " + altTranslations.size() + " alternative translations for word " + vocabularyId);
        for (String altTranslation : altTranslations) {
            ContentValues values = new ContentValues();
            values.put(DbOpenHelper.AltTranslationsColumn.TRANSLATION, altTranslation);
            values.put(DbOpenHelper.AltTranslationsColumn.VOCABULARY_ID, vocabularyId);
            database.insert(DbOpenHelper.TABLE_NAME_ALTTRANSLATIONS, null, values);
        }
    }

}

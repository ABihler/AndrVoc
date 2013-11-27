package de.robertmathes.android.orangeiron.db;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.robertmathes.android.orangeiron.db.DbOpenHelper.LessonColumn;
import de.robertmathes.android.orangeiron.db.DbOpenHelper.ServerColumn;
import de.robertmathes.android.orangeiron.db.DbOpenHelper.StatisticColumn;
import de.robertmathes.android.orangeiron.db.DbOpenHelper.UserColumn;
import de.robertmathes.android.orangeiron.db.DbOpenHelper.VocabularyColumn;
import de.robertmathes.android.orangeiron.model.Lesson;
import de.robertmathes.android.orangeiron.model.Server;
import de.robertmathes.android.orangeiron.model.User;
import de.robertmathes.android.orangeiron.model.Vokabel;

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

    public Server getServer(long serverId) {
        Log.i(TAG, "Loading server with id " + serverId);

        Server server = null;

        Cursor cursor = database.query(DbOpenHelper.TABLE_NAME_SERVER, DbOpenHelper.ALL_COLUMNS_SERVER, ServerColumn.ID + "=" + serverId, null, null, null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            server = new Server();
            server.setId(cursor.getLong(0));
            server.setName(cursor.getString(1));
            server.setUrl(cursor.getString(2));
            server.setServerVersion(cursor.getInt(3));
            server.setDataVersion(cursor.getInt(4));
            cursor.moveToNext();
        }
        cursor.close();

        return server;
    }

    public List<Server> getAllServers() {
        Log.i(TAG, "Getting all servers");

        List<Server> servers = new ArrayList<Server>();

        Cursor cursor = database.query(DbOpenHelper.TABLE_NAME_SERVER, DbOpenHelper.ALL_COLUMNS_SERVER, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Server server = new Server();
            server.setId(cursor.getLong(0));
            server.setName(cursor.getString(1));
            server.setUrl(cursor.getString(2));
            server.setServerVersion(cursor.getInt(3));
            server.setDataVersion(cursor.getInt(4));
            servers.add(server);
            cursor.moveToNext();
        }
        cursor.close();

        return servers;
    }

    public long saveServer(Server server) {
        Log.i(TAG, "Saving server " + server.getName());

        ContentValues values = new ContentValues();
        values.put(ServerColumn.NAME, server.getName());
        values.put(ServerColumn.SERVER_VERSION, server.getServerVersion());
        values.put(ServerColumn.DATA_VERSION, server.getDataVersion());
        values.put(ServerColumn.URL, server.getUrl());
        long serverId = database.insert(DbOpenHelper.TABLE_NAME_SERVER, null, values);

        return serverId;
    }

    public User getUser(long userId) {
        Log.i(TAG, "Loading user with id " + userId);

        User user = null;

        Cursor cursor = database.query(DbOpenHelper.TABLE_NAME_USER, DbOpenHelper.ALL_COLUMNS_USER, UserColumn.ID + "=" + userId, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            user = new User();
            user.setId(cursor.getLong(0));
            user.setName(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();

        return user;
    }

    public User getUserByName(String name) {
        Log.i(TAG, "Loading user with name " + name);

        User user = null;

        Cursor cursor = database.query(DbOpenHelper.TABLE_NAME_USER, DbOpenHelper.ALL_COLUMNS_USER, UserColumn.NAME + "= '" + name + "'", null, null, null,
                null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            user = new User();
            user.setId(cursor.getLong(0));
            user.setName(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();

        return user;
    }

    public List<User> getAllUsers() {
        Log.i(TAG, "Loading all users");

        List<User> users = new ArrayList<User>();

        Cursor cursor = database.query(DbOpenHelper.TABLE_NAME_USER, DbOpenHelper.ALL_COLUMNS_USER, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = new User();
            user.setId(cursor.getLong(0));
            user.setName(cursor.getString(1));
            users.add(user);
            cursor.moveToNext();
        }
        cursor.close();

        return users;
    }

    public long saveUser(User user) {
        Log.i(TAG, "Saving user " + user.getName());

        ContentValues values = new ContentValues();
        values.put(UserColumn.NAME, user.getName());
        long lessonId = database.insert(DbOpenHelper.TABLE_NAME_USER, null, values);

        return lessonId;
    }

    /**
     * Liefert eine komplette Lektion zurück inklusive aller Vokabeln und Übersetzungen.
     * 
     * @param lessonId
     * @return Lektion mit der id lessonId
     */
    public Lesson getLesson(long lessonId) {
        Log.i(TAG, "Loading lesson " + lessonId);
        // Lektion laden
        Lesson lesson = new Lesson();

        Cursor cursor = database.query(DbOpenHelper.TABLE_NAME_LESSONS, DbOpenHelper.ALL_COLUMNS_LESSONS, LessonColumn.ID + "="
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
     * Gibt alle gespeicherten Lektionen zurück
     * 
     * @return Liste von Lektionen mit Vokabeln
     */
    public List<Lesson> getAllLessons() {
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

            // Vokabeln der Lektion laden
            lesson.setVocabulary(getVocabulary(lesson.getId()));

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

    public Vokabel getWord(long wordId) {
        Cursor cursor = database.query(DbOpenHelper.TABLE_NAME_VOCABULARY, DbOpenHelper.ALL_COLUMNS_VOCABULARY,
                DbOpenHelper.VocabularyColumn.ID + "=" + wordId, null, null, null, null);
        Vokabel vokabel = new Vokabel();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            vokabel.setId(cursor.getLong(0));
            vokabel.setOriginalWord(cursor.getString(1));
            vokabel.setCorrectTranslation(cursor.getString(2));
            vokabel.setLessonId(cursor.getLong(3));
            vokabel.setAlternativeTranslations(getAlternativeTranslations(vokabel.getId()));
            cursor.moveToNext();
        }
        cursor.close();

        return vokabel;
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

    // TODO: extract identical code from updateCorrect... and updateBad... methods into helper method
    public void updateCorrectAnswerCount(long userId, long lessonId, long wordId) {
        Log.i(TAG, "Updating correctAnswerCount for user/lesson/word: " + userId + "/" + lessonId + "/" + wordId);
        String whereClause = StatisticColumn.USER_ID + "=" + userId + " and " + StatisticColumn.LESSON_ID + "=" + lessonId + " and "
                + StatisticColumn.VOCABULARY_ID + "=" + wordId;
        Cursor cursor = database.query(DbOpenHelper.TABLE_NAME_STATISTIC, DbOpenHelper.ALL_COLUMNS_STATISTIC,
                whereClause, null, null, null, null);

        ContentValues values = new ContentValues();
        // Check, if combination of user.id, lesson.id and word.id already exists
        if (cursor.moveToFirst()) {
            // statistic for this word already exists -> update the correct count
            Log.d(TAG, "Statistics for this word already exist. Incrementing correctAnswerCount to " + (cursor.getInt(4) + 1));
            values.put(StatisticColumn.CORRECT_ANSWERS, cursor.getInt(4) + 1);
            values.put(StatisticColumn.TIMESTAMP, new Timestamp(new Date().getTime()).toString());
            database.update(DbOpenHelper.TABLE_NAME_STATISTIC, values, whereClause, null);
        } else {
            // no existing record -> insert row
            Log.d(TAG, "No statistics present for this word. Initializing now.");
            values.put(StatisticColumn.USER_ID, userId);
            values.put(StatisticColumn.LESSON_ID, lessonId);
            values.put(StatisticColumn.VOCABULARY_ID, wordId);
            values.put(StatisticColumn.CORRECT_ANSWERS, 1);
            values.put(StatisticColumn.WRONG_ANSWERS, 0);
            database.insert(DbOpenHelper.TABLE_NAME_STATISTIC, null, values);
        }
        cursor.close();
    }

    public void updateBadAnswerCount(long userId, long lessonId, long wordId) {
        Log.i(TAG, "Updating badAnswerCount for user/lesson/word: " + userId + "/" + lessonId + "/" + wordId);
        String whereClause = StatisticColumn.USER_ID + "=" + userId + " and " + StatisticColumn.LESSON_ID + "=" + lessonId + " and "
                + StatisticColumn.VOCABULARY_ID + "=" + wordId;
        Cursor cursor = database.query(DbOpenHelper.TABLE_NAME_STATISTIC, DbOpenHelper.ALL_COLUMNS_STATISTIC,
                whereClause, null, null, null, null);

        ContentValues values = new ContentValues();
        // Check, if combination of user.id, lesson.id and word.id already exists
        if (cursor.moveToFirst()) {
            // statistic for this word already exists -> update the bad count
            Log.d(TAG, "Statistics for this word already exist. Incrementing badAnswerCount to " + (cursor.getInt(5) + 1));
            values.put(StatisticColumn.WRONG_ANSWERS, cursor.getInt(5) + 1);
            values.put(StatisticColumn.TIMESTAMP, new Timestamp(new Date().getTime()).toString());
            database.update(DbOpenHelper.TABLE_NAME_STATISTIC, values, whereClause, null);
        } else {
            // no existing record -> insert row
            Log.d(TAG, "No statistics present for this word. Initializing now.");
            values.put(StatisticColumn.USER_ID, userId);
            values.put(StatisticColumn.LESSON_ID, lessonId);
            values.put(StatisticColumn.VOCABULARY_ID, wordId);
            values.put(StatisticColumn.CORRECT_ANSWERS, 0);
            values.put(StatisticColumn.WRONG_ANSWERS, 1);
            database.insert(DbOpenHelper.TABLE_NAME_STATISTIC, null, values);
        }
        cursor.close();
    }

    /**
     * Returns a lesson with words that have the hightest bad answer count for a given user.
     * 
     * @param user
     * @param maxCount
     *            max. number of words to return for this lesson
     * @return
     */
    public Lesson getWeakestWordsByUser(User user, int maxCount) {
        Log.i(TAG, "Getting weakest word for user " + user.getName());
        List<Vokabel> weakestWords = new ArrayList<Vokabel>();

        String rawQueryClause = "select w." + VocabularyColumn.ID + " from " + DbOpenHelper.TABLE_NAME_VOCABULARY + " as W join "
                + DbOpenHelper.TABLE_NAME_STATISTIC + " as S on W." + VocabularyColumn.ID + " = S." + StatisticColumn.VOCABULARY_ID + " and S."
                + StatisticColumn.USER_ID + "=" + user.getId() + " order by S." + StatisticColumn.WRONG_ANSWERS + " desc limit " + maxCount;
        Log.d(TAG, "SQL query is: " + rawQueryClause);

        Cursor cursor = database.rawQuery(rawQueryClause, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            weakestWords.add(getWord(cursor.getLong(0)));
            cursor.moveToNext();
        }
        cursor.close();

        Lesson lesson = new Lesson();
        lesson.setName(ctx.getString(de.robertmathes.android.orangeiron.R.string.lesson_name_weakest_words));
        lesson.setVocabulary(weakestWords);

        return lesson;
    }

    public Lesson getOldestWordsByUser(User user, int maxCount) {
        Log.i(TAG, "Getting oldest words for user " + user.getName());
        List<Vokabel> oldestWords = new ArrayList<Vokabel>();

        String rawQueryClause = "select w." + VocabularyColumn.ID + " from " + DbOpenHelper.TABLE_NAME_VOCABULARY
                + " as W join "
                + DbOpenHelper.TABLE_NAME_STATISTIC + " as S on W." + VocabularyColumn.ID + " = S." + StatisticColumn.VOCABULARY_ID + " and S."
                + StatisticColumn.USER_ID + "=" + user.getId() + " order by S." + StatisticColumn.TIMESTAMP + " limit " + maxCount;
        Log.d(TAG, "SQL query is: " + rawQueryClause);

        Cursor cursor = database.rawQuery(rawQueryClause, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            oldestWords.add(getWord(cursor.getLong(0)));
            cursor.moveToNext();
        }
        cursor.close();

        Lesson lesson = new Lesson();
        lesson.setName(ctx.getString(de.robertmathes.android.orangeiron.R.string.lesson_name_oldest_words));
        lesson.setVocabulary(oldestWords);

        return lesson;
    }
}

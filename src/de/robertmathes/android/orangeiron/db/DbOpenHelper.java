package de.robertmathes.android.orangeiron.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AndrVoc.db";

    public static final String TABLE_NAME_SERVER = "servers";
    public static final String[] ALL_COLUMNS_SERVER = {};

    public static final String TABLE_NAME_USER = "users";
    public static final String[] ALL_COLUMNS_USER = { UserColumn.ID, UserColumn.NAME };

    public static final String TABLE_NAME_LESSONS = "lessons";
    public static final String[] ALL_COLUMNS_LESSONS = { LessonColumn.ID, LessonColumn.LESSON_NAME, LessonColumn.LESSON_LANGUAGE, LessonColumn.LESSON_VERSION,
            LessonColumn.SERVER_ID };

    public static final String TABLE_NAME_VOCABULARY = "vocabulary";
    public static final String[] ALL_COLUMNS_VOCABULARY = { VocabularyColumn.ID, VocabularyColumn.ORIGINAL_WORD, VocabularyColumn.CORRECT_TRANSLATION,
            VocabularyColumn.LESSON_ID };

    public static final String TABLE_NAME_ALTTRANSLATIONS = "alternativeTranslations";
    public static final String[] ALL_COLUMNS_ALTTRANSLATIONS = { AltTranslationsColumn.ID, AltTranslationsColumn.TRANSLATION,
            AltTranslationsColumn.VOCABULARY_ID };

    public static final String TABLE_NAME_STATISTIC = "statistics";
    public static final String[] ALL_COLUMNS_STATISTIC = { StatisticColumn.ID, StatisticColumn.USER_ID, StatisticColumn.LESSON_ID,
            StatisticColumn.VOCABULARY_ID, StatisticColumn.CORRECT_ANSWERS, StatisticColumn.WRONG_ANSWERS, StatisticColumn.TIMESTAMP };

    public interface ServerColumn {

        String ID = "_id";
        String NAME = "name";
        String URL = "url";
        String SERVER_VERSION = "serverVersion";
        String DATA_VERSION = "dataVersion";
    }

    public interface UserColumn {

        String ID = "_id";
        String NAME = "name";
    }

    public interface LessonColumn {

        String ID = "_id";
        String LESSON_NAME = "lessonName";
        String LESSON_LANGUAGE = "lessonLanguage";
        String LESSON_VERSION = "lessonVersion";
        String SERVER_ID = "serverId";
    }

    public interface VocabularyColumn {

        String ID = "_id";
        String ORIGINAL_WORD = "originalWord";
        String CORRECT_TRANSLATION = "correctTranslation";
        String LESSON_ID = "lessonId";
    }

    public interface AltTranslationsColumn {

        String ID = "_id";
        String TRANSLATION = "translation";
        String VOCABULARY_ID = "vocabularyId";
    }

    public interface StatisticColumn {

        String ID = "_id";
        String USER_ID = "userId";
        String LESSON_ID = "lessonId";
        String VOCABULARY_ID = "vocabularyId";
        String CORRECT_ANSWERS = "correctAnswers";
        String WRONG_ANSWERS = "wrongAnswers";
        String TIMESTAMP = "timestamp";
    }

    private static final String TABLE_CREATE_SERVER = "CREATE TABLE " + TABLE_NAME_SERVER + " (" + ServerColumn.ID + " integer primary key autoincrement, "
            + ServerColumn.NAME + " text not null, " + ServerColumn.URL + " text not null, " + ServerColumn.SERVER_VERSION + " integer not null default 1, "
            + ServerColumn.DATA_VERSION + " integer not null default 1);";

    private static final String TABLE_CREATE_USER = "CREATE TABLE " + TABLE_NAME_USER + " (" + UserColumn.ID + " integer primary key autoincrement, "
            + UserColumn.NAME + " text not null);";

    private static final String TABLE_CREATE_LESSON = "CREATE TABLE " + TABLE_NAME_LESSONS + " (" + LessonColumn.ID + " integer primary key autoincrement, "
            + LessonColumn.LESSON_NAME + " text not null, " + LessonColumn.LESSON_LANGUAGE + " text not null, " + LessonColumn.LESSON_VERSION
            + " integer not null, " + LessonColumn.SERVER_ID + " integer);";

    private static final String TABLE_CREATE_VOCABULARY = "CREATE TABLE " + TABLE_NAME_VOCABULARY + " (" + VocabularyColumn.ID
            + " integer primary key autoincrement, " + VocabularyColumn.ORIGINAL_WORD + " text not null, " + VocabularyColumn.CORRECT_TRANSLATION
            + " text not null, " + VocabularyColumn.LESSON_ID + " integer not null);";

    private static final String TABLE_CREATE_ALTTRANSLATIONS = "CREATE TABLE " + TABLE_NAME_ALTTRANSLATIONS + " (" + AltTranslationsColumn.ID
            + " integer primary key autoincrement, " + AltTranslationsColumn.TRANSLATION + " text not null, " + AltTranslationsColumn.VOCABULARY_ID
            + " integer not null);";

    private static final String TABLE_CREATE_STATISTIC = "CREATE TABLE " + TABLE_NAME_STATISTIC + " (" + StatisticColumn.ID
            + " integer primary key autoincrement, "
            + StatisticColumn.USER_ID + " integer not null, " + StatisticColumn.LESSON_ID + " integer not null, " + StatisticColumn.VOCABULARY_ID
            + " integer not null, "
            + StatisticColumn.CORRECT_ANSWERS + " integer not null default 0, " + StatisticColumn.WRONG_ANSWERS + " integer not null default 0, "
            + StatisticColumn.TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

    DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_SERVER);
        db.execSQL(TABLE_CREATE_USER);
        db.execSQL(TABLE_CREATE_LESSON);
        db.execSQL(TABLE_CREATE_VOCABULARY);
        db.execSQL(TABLE_CREATE_ALTTRANSLATIONS);
        db.execSQL(TABLE_CREATE_STATISTIC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DbOpenHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SERVER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LESSONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_VOCABULARY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ALTTRANSLATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STATISTIC);
        onCreate(db);
    }

}

package de.albert.bihler.andrvoc.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AndrVocOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AndrVoc.db";

    public static final String TABLE_NAME_USER = "users";
    public static final String[] ALL_COLUMNS_USER = { UserColumn.ID, UserColumn.NAME };

    public static final String TABLE_NAME_LESSONS = "lessons";
    public static final String[] ALL_COLUMNS_LESSONS = { LessonColumn.ID, LessonColumn.LESSON_NAME, LessonColumn.LESSON_LANGUAGE, LessonColumn.LESSON_VERSION };

    public static final String TABLE_NAME_VOCABULARY = "vocabulary";
    public static final String[] ALL_COLUMNS_VOCABULARY = { VocabularyColumn.ID, VocabularyColumn.ORIGINAL_WORD, VocabularyColumn.CORRECT_TRANSLATION,
            VocabularyColumn.LESSON_ID };

    public static final String TABLE_NAME_ALTTRANSLATIONS = "alternativeTranslations";
    public static final String[] ALL_COLUMNS_ALTTRANSLATIONS = { AltTranslationsColumn.ID, AltTranslationsColumn.TRANSLATION,
            AltTranslationsColumn.VOCABULARY_ID };

    public static final String TABLE_NAME_STATISTIC = "statistics";
    public static final String[] ALL_COLUMNS_STATISTIC = {};

    public interface UserColumn {

        static final String ID = "_id";
        static final String NAME = "name";
    }

    public interface LessonColumn {

        static final String ID = "_id";
        static final String LESSON_NAME = "lessonName";
        static final String LESSON_LANGUAGE = "lessonLanguage";
        static final String LESSON_VERSION = "lessonVersion";
    }

    public interface VocabularyColumn {

        static final String ID = "_id";
        static final String ORIGINAL_WORD = "originalWord";
        static final String CORRECT_TRANSLATION = "correctTranslation";
        static final String LESSON_ID = "lessonId";
    }

    public interface AltTranslationsColumn {

        static final String ID = "_id";
        static final String TRANSLATION = "translation";
        static final String VOCABULARY_ID = "vocabularyId";
    }

    public interface StatisticColumn {

        static final String ID = "_id";
        static final String USER_ID = "userId";
        static final String LESSON_ID = "lessonId";
        static final String VOCABULARY_ID = "vocabularyId";
        static final String CORRECT_ANSWERS = "correctAnswers";
        static final String WRONG_ANSWERS = "wrongAnswers";
    }

    private static final String TABLE_CREATE_USER = "CREATE TABLE " + TABLE_NAME_USER + " (" + UserColumn.ID + " integer primary key autoincrement, "
            + UserColumn.NAME + " text not null);";

    private static final String TABLE_CREATE_LESSON = "CREATE TABLE " + TABLE_NAME_LESSONS + " (" + LessonColumn.ID + " integer primary key autoincrement, "
            + LessonColumn.LESSON_NAME + " text not null, " + LessonColumn.LESSON_LANGUAGE + " text not null, " + LessonColumn.LESSON_VERSION
            + " integer not null);";

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
            + StatisticColumn.CORRECT_ANSWERS + " integer not null default 0, " + StatisticColumn.WRONG_ANSWERS + " integer not null default 0);";

    AndrVocOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_USER);
        db.execSQL(TABLE_CREATE_LESSON);
        db.execSQL(TABLE_CREATE_VOCABULARY);
        db.execSQL(TABLE_CREATE_ALTTRANSLATIONS);
        db.execSQL(TABLE_CREATE_STATISTIC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(AndrVocOpenHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LESSONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_VOCABULARY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ALTTRANSLATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STATISTIC);
        onCreate(db);
    }

}

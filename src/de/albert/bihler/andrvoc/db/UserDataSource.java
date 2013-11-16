package de.albert.bihler.andrvoc.db;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import de.albert.bihler.andrvoc.model.User;

public class UserDataSource {

    private static final String TAG = "LessonDataSource";

    private SQLiteDatabase database;
    private final DbOpenHelper dbHelper;
    private final Context ctx;

    public UserDataSource(Context context) {
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

}

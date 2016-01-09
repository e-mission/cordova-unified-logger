package edu.berkeley.eecs.emission.cordova.unifiedlogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * Created by shankari on 7/16/15.
 */
public class DatabaseLogHandler extends SQLiteOpenHelper {
    private String TABLE_LOG = "logTable";
    private String KEY_ID = "ID";
    private String KEY_TS = "ts";
    private String KEY_LEVEL = "level";
    private String KEY_MESSAGE = "message";
    private static final int DATABASE_VERSION = 1;

    private Context cachedContext;
    Formatter formatter;
    SQLiteDatabase writeDB;

    public DatabaseLogHandler(Context context) {
        super(context, "loggerDB", null, DATABASE_VERSION);
        cachedContext = context;
        writeDB = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_LOG_TABLE = "CREATE TABLE " + TABLE_LOG +" (" +
                KEY_ID + " INTEGER PRIMARY_KEY, "+ KEY_TS + " REAL, " +
                KEY_LEVEL + " TEXT, " + KEY_MESSAGE +" TEXT)";
        System.out.println("CREATE_LOG_TABLE = " + CREATE_LOG_TABLE);
        sqLiteDatabase.execSQL(CREATE_LOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG);
        onCreate(sqLiteDatabase);
    }

    public void log(String level, String message) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TS, ((double)System.currentTimeMillis())/1000);
        cv.put(KEY_LEVEL, level);
        cv.put(KEY_MESSAGE, message);
        writeDB.insert(TABLE_LOG, null, cv);
    }

    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOG, null, null);
        db.close();
    }
}
